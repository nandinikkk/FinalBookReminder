package book.reminder.nandy.bookreminder;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nandy.bookreminder.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import book.google.zxing.integration.android.IntentIntegrator;
import book.google.zxing.integration.android.IntentResult;


public class ManualActivity extends Activity{

    TextView name;
    TextView lender;
    TextView email;

    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    CheckBox sendReminder;
    LinearLayout spinner1,spinner2;
    Spinner s1,s2;

    String from_Email_Id, pwd;
    String spinner1_text= "Not assigned";
    String spinner2_text ="Not assigned" ;

    DBHelper mydb;

    Button takePictureButton,scanBtn;
    ImageView cameraView;
    Uri file;
    private static String path;
    private static final int CAMERA_PIC_REQUEST = 1337;
    Bitmap bmp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.manual_activity);


        name = (TextView) findViewById(R.id.bookName);
        lender = (TextView) findViewById(R.id.lender);
        email = (TextView) findViewById(R.id.email);
        dateView = (TextView) findViewById(R.id.dateText);
        sendReminder = (CheckBox) findViewById(R.id.reminders);
        // dateView = (TextView) findViewById(R.id.dateText);
        takePictureButton = (Button)findViewById(R.id.camera);
        scanBtn= (Button)findViewById(R.id.scan);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(v.getId()==R.id.scan){
                    //instantiate ZXing integration class
                    IntentIntegrator scanIntegrator = new IntentIntegrator(ManualActivity.this);
                    //start scanning
                    scanIntegrator.initiateScan();
                }
            }
        });


        //ui items



        //retrieve state
        if (savedInstanceState != null){
            name.setText(savedInstanceState.getString("title"));


        }
        cameraView = (ImageView) findViewById(R.id.cameraView);
        bmp= BitmapFactory.decodeResource(getResources(), R.drawable.npa);
        cameraView.setImageBitmap(bmp);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 0);

        }


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        spinner1 = (LinearLayout) findViewById(R.id.layoutspinner1);
        spinner2 = (LinearLayout) findViewById(R.id.layoutspinner2);
        sendReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                        if (sendReminder.isChecked()) {
                                                            spinner1.setVisibility(View.VISIBLE);
                                                            spinner2.setVisibility(View.VISIBLE);
                                                        } else {
                                                            spinner1.setVisibility(View.INVISIBLE);
                                                            spinner2.setVisibility(View.INVISIBLE);
                                                        }
                                                    }

                                                }
        );

        s1 =(Spinner)findViewById(R.id.spinner1);
        s2 =(Spinner)findViewById(R.id.spinner2);
        s1.setOnItemSelectedListener(new MyOnItemSelectedListener());
        s2.setOnItemSelectedListener(new MyOnItemSelectedListener1());

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.items1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter1);

        dateView.setInputType(InputType.TYPE_NULL);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        mydb = new DBHelper(this);

        dateView.setInputType(InputType.TYPE_NULL);
        Cursor rs = mydb.getData_id();
        rs.moveToFirst();
        from_Email_Id = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_USER_EMAIL));
        pwd = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_PWD));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu1:
                finish();
                Intent intent = new Intent(this,ManualActivity.class);
                startActivity(intent);

                return true;

            case R.id.menu2:
                finish();
                Intent intent1 = new Intent(this,AllBooksList.class);
                startActivity(intent1);
                return true;

            case R.id.menu3:
                finish();
                Intent intent2 = new Intent(this,UpdateUserID.class);
                startActivity(intent2);
                return true;
            case R.id.menu4:
                finish();
                Intent intent4 = new Intent(this,ReturnBook.class);
                startActivity(intent4);
                return true;
            case R.id.menu5:
                finish();
                Intent intent3 = new Intent(this,MainActivity.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            spinner1_text = s1.getSelectedItem().toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
    public class MyOnItemSelectedListener1 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            spinner2_text = s2.getSelectedItem().toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
    public void setDate(View view) {
        showDialog(999);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {

        dateView.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    public void takePicture(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "testingcamera");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){

                return null;
            }
        }
        String test=String.valueOf(mediaStorageDir.exists());
       String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image=new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+timeStamp+".jpg");

        path=image.getPath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1337) {

            Log.i("request code loop", "request code loop");

            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                bmp = (Bitmap) extras.get("data");
                cameraView = (ImageView) findViewById(R.id.cameraView);
                cameraView.setImageBitmap(bmp);
            }
        } else
        {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check we have a valid result
        if (scanningResult != null) {
            //get content from Intent Result
            String scanContent = scanningResult.getContents();
            //get format name of data scanned
            String scanFormat = scanningResult.getFormatName();
            if (scanContent != null && scanFormat != null && scanFormat.equalsIgnoreCase("EAN_13")) {

                String bookSearchString = "https://www.googleapis.com/books/v1/volumes?" +
                        "q=isbn:" + scanContent;
                //fetch search results
                new GetBookInfo().execute(bookSearchString);
            } else {
                //not ean
              //  Toast toast = Toast.makeText(getApplicationContext(),
              //          "Not a valid scan!", Toast.LENGTH_SHORT);
             //   toast.show();
            }
        } else {
            //invalid scan data or scan canceled
          //  Toast toast = Toast.makeText(getApplicationContext(),
          //          "No book scan data received!", Toast.LENGTH_SHORT);
          //  toast.show();
        }
    }
    }
    private class GetBookInfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... bookURLs) {
            StringBuilder bookBuilder = new StringBuilder();
            for (String bookSearchURL : bookURLs) {
                HttpClient bookClient = new DefaultHttpClient();
                try {
                    //get the data
                    HttpGet bookGet = new HttpGet(bookSearchURL);
                    HttpResponse bookResponse = bookClient.execute(bookGet);
                    StatusLine bookSearchStatus = bookResponse.getStatusLine();
                    if (bookSearchStatus.getStatusCode()==200) {
                        //we have a result
                        HttpEntity bookEntity = bookResponse.getEntity();
                        InputStream bookContent = bookEntity.getContent();
                        InputStreamReader bookInput = new InputStreamReader(bookContent);
                        BufferedReader bookReader = new BufferedReader(bookInput);
                        String lineIn;
                        while ((lineIn=bookReader.readLine())!=null) {
                            bookBuilder.append(lineIn);
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            return bookBuilder.toString();
        }
        protected void onPostExecute(String result) {
            try{
                //parse results
                JSONObject resultObject = new JSONObject(result);
                JSONArray bookArray = resultObject.getJSONArray("items");
                JSONObject bookObject = bookArray.getJSONObject(0);
                JSONObject volumeObject = bookObject.getJSONObject("volumeInfo");
                //try for title
                try{
                    name.setText("TITLE: "+volumeObject.getString("title")); }
                catch(JSONException jse){
                    name.setText("");
                    jse.printStackTrace();
                }
                //author can be multiple
                StringBuilder authorBuild = new StringBuilder("");
            }
            catch (Exception e) {
                //no result
                e.printStackTrace();
                name.setText("NOT FOUND");
            }
        }
    }

    private class GetBookThumb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... thumbURLs) {
            try{
                //attempt to download thumbnail image using passed URL
                URL thumbURL = new URL(thumbURLs[0]);
                URLConnection thumbConn = thumbURL.openConnection();
                thumbConn.connect();
                InputStream thumbIn = thumbConn.getInputStream();
                BufferedInputStream thumbBuff = new BufferedInputStream(thumbIn);

                thumbBuff.close();
                thumbIn.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return "";
        }

    }
    //save state
    protected void onSaveInstanceState(Bundle savedBundle) {
        savedBundle.putString("title", ""+name.getText());

    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public void save(View view) {


        if (name.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter book name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lender.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter Lender name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter lender email Id", Toast.LENGTH_SHORT).show();
            return;
        }
        String email1=email.getText().toString();
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (email1.matches(emailPattern))
        {
            //do nothing
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] data = getBytes(bmp);


        if(mydb.insertBookDetails(name.getText().toString(), lender.getText().toString(),
                email.getText().toString(), dateView.getText().toString(), spinner1_text,spinner2_text,data)){
            Cursor rs2 = mydb.unique_id( email.getText().toString(),name.getText().toString());
            rs2.moveToFirst();
            String id = rs2.getString(rs2.getColumnIndex(DBHelper.COLUMN_ID));
            mydb.close();
            int unique_id = Integer.parseInt(id);
            long i=0,j=0;
            int unique_id1=0, unique_id2=0;
            System.out.println("id is "+id);
            if(sendReminder.isChecked())
            {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                stopService(new Intent(getApplicationContext(), ScheduledService.class));
                Log.d("main activity","i checked");
                PollReceiver.scheduleAlarms(ManualActivity.this,dateView.getText().toString(),email.getText().toString(),from_Email_Id,unique_id,name.getText().toString(),pwd);
            }
            if(spinner1_text.equals("one day"))
            {
                i=  24 * 1000 * 60 * 60;
                unique_id1 = unique_id + 1000;

            }
            else if(spinner1_text.equals("one hour"))
            {
                i = 60 * 60 * 1000;
                unique_id1 = unique_id + 1000;
            }
            else if(spinner1_text.equals("10 minutes"))
            {
                i = 10 * 60 * 1000;
                unique_id1 = unique_id + 1000;
            }
            else if(spinner1_text.equals("5 minutes"))
            {
                i = 5 * 60 * 1000;
                unique_id1 = unique_id + 1000;
            }
            else
            {
                i = 0;
            }
            if ( i !=0) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                stopService(new Intent(getApplicationContext(), ScheduledService.class));
                PollReceiver.scheduleRepeatingAlarms(ManualActivity.this, dateView.getText().toString(), email.getText().toString(), from_Email_Id, unique_id1, i,name.getText().toString(),pwd);
            }
            if(spinner2_text.equals("one day"))
            {
                j =  24 * 1000 * 60 * 60;
                unique_id2 = unique_id + 1001;

            }
            else if(spinner2_text.equals("one hour"))
            {
                j =60 * 60 * 1000;
                unique_id2 = unique_id + 1001;
            }
            else if(spinner2_text.equals("10 minutes"))
            {
                j = 10 * 60 * 1000;
                unique_id2 = unique_id + 1001;
            }
            else if(spinner2_text.equals("5 minutes"))
            {
                j = 5 * 60 * 1000;
                unique_id2 = unique_id + 1001;
            }
            else
            {
                j = 0;
            }
            if ( j !=0) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                stopService(new Intent(getApplicationContext(), ScheduledService.class));
                PollReceiver.scheduleRepeatingAlarms_afterWeek(ManualActivity.this, dateView.getText().toString(), email.getText().toString(), from_Email_Id, unique_id2, j,name.getText().toString(),pwd);
            }
            Toast.makeText(getApplicationContext(), "Book Details Saved",
                    Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(getApplicationContext(), "Book Details Not Saved, Try again!!",
                    Toast.LENGTH_SHORT).show();
        }
        finish();
    }


}
