package book.reminder.nandy.bookreminder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import book.google.zxing.integration.android.IntentIntegrator;
import book.google.zxing.integration.android.IntentResult;


public class ReturnBook extends AppCompatActivity {

    private ListView obj;
    DBHelper mydb;
    EditText book_name;
    Button check,scanBtn;
    List array_list;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        book_name = (EditText) findViewById(R.id.bookName_r);
        scanBtn= (Button)findViewById(R.id.scan_r);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(v.getId()==R.id.scan_r){
                    //instantiate ZXing integration class
                    IntentIntegrator scanIntegrator = new IntentIntegrator(ReturnBook.this);
                    //start scanning
                    scanIntegrator.initiateScan();
                }
            }
        });
        if (savedInstanceState != null){
            book_name.setText(savedInstanceState.getString("title"));


        }
        mydb = new DBHelper(this);
        check = (Button)findViewById(R.id.checkReturn);

        check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                array_list= mydb.getAllBooks_bookName(book_name.getText().toString());
                if(array_list.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "No Books with the given title", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayAdapter arrayAdapter=new ArrayAdapter(v.getContext(),R.layout.list_black_text,R.id.list_content, array_list);
                int position=0;

                obj = (ListView)findViewById(R.id.listView1_r);
                obj.setAdapter(arrayAdapter);
                obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        // TODO Auto-generated method stub

                        ArrayList values_tokken = new ArrayList();
                        String s = obj.getItemAtPosition(arg2).toString();// {PersonName=DZCSDd, BookName=sdcdc}

                        StringTokenizer tok = new StringTokenizer(s, "=,}");
                        while (tok.hasMoreTokens()) {
                            values_tokken.add(tok.nextToken());
                        }



                        Cursor rs2 = mydb.unique_id1(values_tokken.get(1).toString(),values_tokken.get(3).toString());
                        rs2.moveToFirst();
                        String id = rs2.getString(rs2.getColumnIndex(DBHelper.COLUMN_ID));
                        int unique_id = Integer.parseInt(id);
                        int id_To_Search = unique_id;

                        Bundle dataBundle = new Bundle();
                        dataBundle.putInt("id", id_To_Search);

                        Intent intent = new Intent(getApplicationContext(),DisplayBookDetails.class);

                        intent.putExtras(dataBundle);
                        startActivity(intent);

                    }
                });
            }
        });





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check we have a valid result
        if (scanningResult != null) {
            //get content from Intent Result
            String scanContent = scanningResult.getContents();
            //get format name of data scanned
            String scanFormat = scanningResult.getFormatName();
            if(scanContent!=null && scanFormat!=null && scanFormat.equalsIgnoreCase("EAN_13")){

                String bookSearchString = "https://www.googleapis.com/books/v1/volumes?" +
                        "q=isbn:"+scanContent;
                //fetch search results
                new GetBookInfo().execute(bookSearchString);
            }
            else{
                //not ean
          //      Toast toast = Toast.makeText(getApplicationContext(),
           //             "Not a valid scan!", Toast.LENGTH_SHORT);
          //      toast.show();
            }
        }
        else{
            //invalid scan data or scan canceled
         //   Toast toast = Toast.makeText(getApplicationContext(),
         //           "No book scan data received!", Toast.LENGTH_SHORT);
        //    toast.show();
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
                    book_name.setText("TITLE: "+volumeObject.getString("title")); }
                catch(JSONException jse){
                    book_name.setText("");
                    jse.printStackTrace();
                }
                //author can be multiple
                StringBuilder authorBuild = new StringBuilder("");
            }
            catch (Exception e) {
                //no result
                e.printStackTrace();
                book_name.setText("NOT FOUND");
            }
        }
    }


    //save state
    protected void onSaveInstanceState(Bundle savedBundle) {
        savedBundle.putString("title", ""+book_name.getText());

    }
}
