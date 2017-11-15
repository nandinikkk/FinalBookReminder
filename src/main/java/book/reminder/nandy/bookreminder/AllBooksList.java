package book.reminder.nandy.bookreminder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nandy.bookreminder.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class AllBooksList extends AppCompatActivity {
    private ListView obj;
    DBHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_books);

        mydb = new DBHelper(this);
        final List array_list = mydb.getAllBooks();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.list_black_text,R.id.list_content, array_list);
        int position=0;

        obj = (ListView)findViewById(R.id.listView1);
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
                Log.e("ID",id.toString());
                int unique_id = Integer.parseInt(id);
              int id_To_Search = unique_id;

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(),DisplayBookDetails.class);

                intent.putExtras(dataBundle);
               startActivity(intent);
                finish();
            }
        });


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


}

