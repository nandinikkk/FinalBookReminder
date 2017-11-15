package book.reminder.nandy.bookreminder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nandy.bookreminder.R;

public class UpdateUserID extends AppCompatActivity {

TextView email_existing;
    EditText email_Updated, pwd_updated;
    DBHelper db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_id);
        db = new DBHelper(this);

        email_existing=(TextView) findViewById(R.id.email_existing);
        email_Updated=(EditText) findViewById(R.id.email_user_update);
        pwd_updated=(EditText) findViewById(R.id.pwd_user_update);


    Cursor rs = db.getData_id();
       rs.moveToFirst();

        String Email_Id = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_USER_EMAIL));
        if (!rs.isClosed()) {
            rs.close();
            email_existing.setText((CharSequence) Email_Id);

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

    public void Update_Id(View view) {
        if (email_Updated.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter  email Id", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd_updated.getText().toString().matches("")) {
            Toast.makeText(this, "You did not password", Toast.LENGTH_SHORT).show();
            return;
        }
        String email1=email_Updated.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email1.matches(emailPattern))
        {
            //do nothing
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

                if (db.UpdateID(email_Updated.getText().toString(),pwd_updated.getText().toString())) {
                     MainActivity m = new MainActivity();
                    m.set_UID(email_Updated.getText().toString());
                    Toast.makeText(getApplicationContext(), "Email Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }


