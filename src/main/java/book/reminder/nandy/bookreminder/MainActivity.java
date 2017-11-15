package book.reminder.nandy.bookreminder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nandy.bookreminder.R;

public class MainActivity extends AppCompatActivity {

     public  TextView email_user;
    public  TextView pwd_user;
    Button btnid;
    private String UID="";
   DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
     //   email_user = (TextView) findViewById(R.id.email_user);
     //   pwd_user = (TextView) findViewById(R.id.pwd_user);
        db = new DBHelper(this);
        RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.loginlayout);
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.manualLayout);
        if(db.isEMpty()){
           setContentView(R.layout.login_activity);
            email_user = (TextView) findViewById(R.id.email_user);
            pwd_user = (TextView) findViewById(R.id.pwd_user);

        }
else {
            Cursor rs = db.getData_id();
            rs.moveToFirst();

            String Email_Id = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_USER_EMAIL));
            set_UID(Email_Id);
            System.out.println(UID);
            if (!rs.isClosed()) {
                rs.close();

            }

        }

    }

    public void save_id(View view) {
        pwd_user = (TextView) findViewById(R.id.pwd_user);
        String email1=email_user.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email_user.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter your email Id", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd_user.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email1.matches(emailPattern))
        {
           //do nothing
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(db.insertID(email_user.getText().toString(),pwd_user.getText().toString())){
            set_UID(email_user.getText().toString());
            Toast.makeText(getApplicationContext(), "User Details Saved",
                    Toast.LENGTH_SHORT).show();


        } else{
            Toast.makeText(getApplicationContext(), "User Details Not Saved, Try again!!",
                    Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    public String get_UID()
    {
        return this.UID;
    }
    public void set_UID(String UID)
    {
        this.UID=UID;
    }


    public void ManualActivity_1(View view){
        Intent intent = new Intent(this,ManualActivity.class);
        startActivity(intent);

    }
    public void AllBooksList(View view){
        Intent intent = new Intent(this,AllBooksList.class);
        startActivity(intent);

    }

    public void UpdateID(View view){
        Intent intent = new Intent(this,UpdateUserID.class);
        startActivity(intent);

    }
    public void ReturnBook(View view){
        Intent intent = new Intent(this,ReturnBook.class);
        startActivity(intent);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        btnid = (Button)findViewById(R.id.changeID);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        if(findViewById(R.id.loginlayout) != null)
        {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        else {
            imm.hideSoftInputFromWindow(new View(this).getWindowToken(), 0);
        }

        return true;
    }
}
