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
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class DisplayBookDetails extends Activity {
    DBHelper mydb;

    TextView name;
    TextView lender;
    TextView email,text_week,text_week_after;
    TextView date;
    CheckBox cancelReminder;
    CheckBox sendReminder;


    LinearLayout hidelayout;
    LinearLayout spinner1,spinner2;
    Spinner s1,s2;

    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    String from_Email_Id,pwd;
    String spinner1_text= "Not assigned";
    String spinner2_text ="Not assigned" ;
    int id_To_Update = 0;

    Button takePictureButton,savePictureButton,displayPictureButton;
    ImageView cameraView;
    Uri file;
    private static String path;
    private static final int CAMERA_PIC_REQUEST = 1337;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_activity);

        name = (TextView) findViewById(R.id.bookName);
        lender = (TextView) findViewById(R.id.lender);
        email = (TextView) findViewById(R.id.email);
        date = (TextView) findViewById(R.id.dateText1);
        dateView = (TextView) findViewById(R.id.dateText);
        text_week = (TextView) findViewById(R.id.text_week);
        text_week_after = (TextView) findViewById(R.id.text_week_after);
        takePictureButton = (Button)findViewById(R.id.camera);
        cameraView = (ImageView) findViewById(R.id.cameraView);
        bmp= BitmapFactory.decodeResource(getResources(), R.drawable.npa);
       cameraView.setImageBitmap(bmp);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 0);

        }
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        sendReminder = (CheckBox) findViewById(R.id.reminders);
        spinner1 = (LinearLayout) findViewById(R.id.layoutspinner1);
        spinner2 = (LinearLayout) findViewById(R.id.layoutspinner2);


       spinner1.setVisibility(View.VISIBLE);
      spinner2.setVisibility(View.VISIBLE);

        s1 =(Spinner)findViewById(R.id.spinner1);
        s2 =(Spinner)findViewById(R.id.spinner2);
        s1.setOnItemSelectedListener(new MyOnItemSelectedListener());
        s2.setOnItemSelectedListener(new MyOnItemSelectedListener1());

        cancelReminder = (CheckBox) findViewById(R.id.cancelreminders);
        cancelReminder.setVisibility(View.VISIBLE);
        cancelReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                        if (cancelReminder.isChecked()) {
                                                            spinner1.setVisibility(View.INVISIBLE);
                                                            spinner2.setVisibility(View.INVISIBLE);
                                                        } else {
                                                            spinner1.setVisibility(View.VISIBLE);
                                                            spinner2.setVisibility(View.VISIBLE);
                                                        }
                                                    }

                                                }
        );

        hidelayout = (LinearLayout) findViewById(R.id.hideLayout);
        hidelayout.setVisibility(View.VISIBLE);

        Button b = (Button) findViewById(R.id.saveBtn);
        b.setVisibility(View.INVISIBLE);
        Button b2 = (Button) findViewById(R.id.scan);
        b2.setVisibility(View.INVISIBLE);

        mydb = new DBHelper(this);
        Cursor rs1 = mydb.getData_id();
        rs1.moveToFirst();
        from_Email_Id = rs1.getString(rs1.getColumnIndex(DBHelper.COLUMN_USER_EMAIL));
        pwd = rs1.getString(rs1.getColumnIndex(DBHelper.COLUMN_PWD));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");

            if (Value > 0) {
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String book_name = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_Book_Name));
                String person_name = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_NAME));
                String email_add = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_EMAIL));
                String date_s = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_DATE_LEND));
                String date_remind = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_DATE_REMIND));
                spinner1_text = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_FREQUENCY_TILL_WEEK));
                spinner2_text = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_FREQUENCY_AFTER_WEEK));
                byte[] imgByte = rs.getBlob(rs.getColumnIndex(DBHelper.COLUMN_Image));
                ByteArrayInputStream imageStream = new ByteArrayInputStream(imgByte);
                Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                if (!rs.isClosed()) {
                    rs.close();
                }
                Button b1 = (Button) findViewById(R.id.updateBtn);
                b1.setVisibility(View.VISIBLE);
                Button b3 = (Button) findViewById(R.id.returnBtn);
                b3.setVisibility(View.VISIBLE);

                cameraView = (ImageView) findViewById(R.id.cameraView);
                cameraView.setImageBitmap(bmp);
               name.setText((CharSequence) book_name);
               lender.setText((CharSequence) person_name);
               email.setText((CharSequence) email_add);
                dateView.setText((CharSequence) date_remind);
                date.setText((CharSequence) date_s);

                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.items, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s1.setAdapter(adapter);
                s1.setSelection(adapter.getPosition(spinner1_text));

                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                        R.array.items1, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s2.setAdapter(adapter1);
                s2.setSelection(adapter1.getPosition(spinner2_text));

                if(spinner1_text.equals("Dont send"))
                {
                    text_week.setText("Reminder till week is NOT SET, Set it now ");
                }
                else
                {
                    text_week.setText("Reminder are set till week for every");
                }
                if(spinner2_text.equals("Dont send"))
                {
                    text_week_after.setText("Reminder after week is NOT SET, Set it now ");
                }
                else
                {
                    text_week_after.setText("Reminder are set after week for every ");
                }

            }
        }

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
    @SuppressWarnings("deprecation")
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
                "IMG_"+ timeStamp + ".jpg");
        Log.i("MainActivity", "new image path is ============" + image.getPath());
        path=image.getPath();

        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("request code is ", "" + requestCode);
        Log.i("result code is ", "" + resultCode);
        Log.i("hello4444", "" + this.RESULT_OK);
        if (requestCode == 1337) {

            Log.i("request code loop", "request code loop");

            if (resultCode == RESULT_OK) {
                //imageView.setImageURI(file);

                // Get Extra from the intent
                Bundle extras = data.getExtras();
                // Get the returned image from extra
                bmp = (Bitmap) extras.get("data");
                cameraView = (ImageView) findViewById(R.id.cameraView);
                cameraView.setImageBitmap(bmp);



            }
        }
    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public void run_update(View view) {
        if(cancelReminder.isChecked())
        {
            spinner1_text = "Dont send";
            spinner2_text = "Dont send";
        }
        Bundle extras = getIntent().getExtras();
        byte[] data = getBytes(bmp);
        Cursor rs2 = mydb.unique_id( email.getText().toString(),name.getText().toString());
        rs2.moveToFirst();
        String id = rs2.getString(rs2.getColumnIndex(DBHelper.COLUMN_ID));
        int unique_id = Integer.parseInt(id);
                if (mydb.updateBookDetails(unique_id, name.getText().toString(), lender.getText().toString(),
                       email.getText().toString(),dateView.getText().toString(),spinner1_text,spinner2_text,data)) {

                    mydb.close();
                  //  String id = rs2.getString(rs2.getColumnIndex(DBHelper.COLUMN_ID));

                    long i,j;
                            int unique_id1=0, unique_id2=0;
                    System.out.println("id is "+id);
                    //Sending reminder on due date
                    if(sendReminder.isChecked())
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        stopService(new Intent(getApplicationContext(), ScheduledService.class));
                        Log.d("main activity","i checked");
                        PollReceiver.unscheduleAlarms(DisplayBookDetails.this,unique_id);
                        PollReceiver.scheduleAlarms(DisplayBookDetails.this,dateView.getText().toString(),email.getText().toString(),from_Email_Id,unique_id,name.getText().toString(),pwd);
                    }
                    //cancels all three reminders
                    if(cancelReminder.isChecked())
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        stopService(new Intent(getApplicationContext(), ScheduledService.class));
                        Log.d("main activity","i checked");
                        PollReceiver.unscheduleAlarms(DisplayBookDetails.this,unique_id);
                        PollReceiver.unscheduleAlarms(DisplayBookDetails.this,unique_id+1000);
                        PollReceiver.unscheduleAlarms(DisplayBookDetails.this,unique_id+1001);
                        spinner1.setVisibility(View.INVISIBLE);
                        spinner2.setVisibility(View.INVISIBLE);
                    }
                    else {
                        if (spinner1_text.equals("one day")) {
                           i= 24 * 1000 * 60 * 60;
                            unique_id1 = unique_id + 1000;

                        } else if (spinner1_text.equals("one hour")) {
                            i = 1000 * 60 * 60;
                            unique_id1 = unique_id + 1000;
                        } else if (spinner1_text.equals("10 minutes")) {
                            i = 1000 * 60 * 10;
                            unique_id1 = unique_id + 1000;
                        } else if (spinner1_text.equals("5 minutes")) {
                            i = 1000 * 60 * 5;
                            unique_id1 = unique_id + 1000;
                        } else {
                            i = 0;
                            unique_id1 = unique_id + 1000;
                        }
                        if (i != 0) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            stopService(new Intent(getApplicationContext(), ScheduledService.class));
                            PollReceiver.scheduleRepeatingAlarms(DisplayBookDetails.this, dateView.getText().toString(), email.getText().toString(), from_Email_Id, unique_id1, i,name.getText().toString(),pwd);
                        } else {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            stopService(new Intent(getApplicationContext(), ScheduledService.class));
                            PollReceiver.unscheduleAlarms(DisplayBookDetails.this, unique_id1);
                        }

                        if (spinner2_text.equals("one day")) {
                            j =24 * 1000 * 60 * 60;
                            unique_id2 = unique_id + 1001;

                        } else if (spinner2_text.equals("one hour")) {
                            j =  1000 * 60 * 60;
                            unique_id2 = unique_id + 1001;
                        } else if (spinner2_text.equals("10 minutes")) {
                            j = 24 * 1000 * 60 * 10;
                            unique_id2 = unique_id + 1001;
                        } else if (spinner2_text.equals("5 minutes")) {
                            j = 24 * 1000 * 60 * 5;
                            unique_id2 = unique_id + 1001;
                        } else {
                            j = 0;
                            unique_id2 = unique_id + 1001;
                        }
                        if (j != 0) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            stopService(new Intent(getApplicationContext(), ScheduledService.class));
                            PollReceiver.scheduleRepeatingAlarms_afterWeek(DisplayBookDetails.this, dateView.getText().toString(), email.getText().toString(), from_Email_Id, unique_id2, j,name.getText().toString(),pwd);
                        } else {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            stopService(new Intent(getApplicationContext(), ScheduledService.class));
                            PollReceiver.unscheduleAlarms(DisplayBookDetails.this, unique_id2);
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            }


    public void return_book(View view) {
        Cursor rs2 = mydb.unique_id( email.getText().toString(),name.getText().toString());
        rs2.moveToFirst();
        String id = rs2.getString(rs2.getColumnIndex(DBHelper.COLUMN_ID));
        int unique_id = Integer.parseInt(id);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        stopService(new Intent(getApplicationContext(), ScheduledService.class));
        Log.d("main activity","i checked");
        PollReceiver.unscheduleAlarms(DisplayBookDetails.this,unique_id);
        PollReceiver.unscheduleAlarms(DisplayBookDetails.this,unique_id+1000);
        PollReceiver.unscheduleAlarms(DisplayBookDetails.this,unique_id+1001);
        spinner1.setVisibility(View.INVISIBLE);
        spinner2.setVisibility(View.INVISIBLE);
        if (mydb.deletebookdetails(name.getText().toString(), lender.getText().toString(),
                email.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

            finish();
        }else {
            Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_SHORT).show();
        }
    }
}
