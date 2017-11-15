package book.reminder.nandy.bookreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import java.sql.Date;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BooksData";
    public static final String TABLE_NAME = "Book_Details";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_Book_Name = "Book_Name";
    public static final String COLUMN_NAME = "Name";
   // public static final String COLUMN_PHONE_NUMBER = "Phone_Number";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_DATE_LEND = "Date_lend";
    public static final String COLUMN_DATE_REMIND = "Date_remind";
   public static final String COLUMN_FREQUENCY_TILL_WEEK = "Email_Reminders";
   public static final String COLUMN_FREQUENCY_AFTER_WEEK = "Email_Reminders1";
    public static final String COLUMN_Image = "imagefield";


    public static final String TABLE_NAME_ID = "User_Details";
    public static final String COLUMN_USER_EMAIL = "User_Email";
   public static final String COLUMN_PWD = "User_pwd";

   private static final String CREATE_TABLE =
           "CREATE TABLE IF NOT EXISTS " + TABLE_NAME+ "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + COLUMN_Book_Name  + " VARCHAR(150), "
                   + COLUMN_NAME + " VARCHAR(150), "

                     + COLUMN_EMAIL + " VARCHAR(150),"
                   + COLUMN_DATE_REMIND + " DATE,"
               + COLUMN_DATE_LEND + " VARCHAR(150), "
                   + COLUMN_FREQUENCY_TILL_WEEK + " VARCHAR(150), "
                   + COLUMN_FREQUENCY_AFTER_WEEK + " VARCHAR(150), "
                   + COLUMN_Image  + " BLOB) ";



    private static final String CREATE_TABLE_ID =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ID+ "("
                    + COLUMN_USER_EMAIL  + " VARCHAR(150),"
    + COLUMN_PWD + " VARCHAR(150)) ";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 14);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ID);
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Book_Details");
        db.execSQL("DROP TABLE IF EXISTS User_Details");
        onCreate(db);
    }
    public boolean insertID (String ID,String PWD) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("User_Email", ID);
        contentValues.put("User_pwd", PWD);

        db.insert("User_Details", null, contentValues);
        return true;
    }
    public boolean UpdateID (String ID,String PWD) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME_ID);
        ContentValues contentValues = new ContentValues();

        contentValues.put("User_Email", ID);
        contentValues.put("User_pwd", PWD);
        db.insert("User_Details", null, contentValues);
        return true;
    }
    public boolean isEMpty () {

        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM User_Details";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0)
            return false;
        else
            return true;

    }
    public Cursor getData_id() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from User_Details LIMIT 1", null );
        return res;
    }
    public Cursor unique_id(String emaill,String bookname) {
        String query = "SELECT id FROM Book_Details WHERE Book_Name ='" + bookname + "'" + "and Email ='" + emaill + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( query, null );
        return res;
    }
    public Cursor unique_id1(String personname,String bookname) {

        String query = "SELECT id FROM Book_Details WHERE Book_Name ='" + bookname + "'" + "and Name ='" + personname + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( query, null );
        if (res == null)
        {
            Log.e("DB","null");
        }
        return res;
    }


    public boolean insertBookDetails (String Book_Name,String Name, String Email,String date_remind,String beforeweek,String afterweek,byte[] image) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Book_Name", Book_Name);
        contentValues.put("Name", Name);
        contentValues.put("Email", Email);
        contentValues.put("Date_remind", date_remind);
        contentValues.put("Date_lend", sdf.format(new Date(System.currentTimeMillis())));
        contentValues.put("Date_remind", date_remind);
        contentValues.put("Email_Reminders", beforeweek);
        contentValues.put("Email_Reminders1", afterweek);
        contentValues.put("imagefield",   image);
        db.insert("Book_Details", null, contentValues);
        return true;
    }
    public boolean updateBookDetails (Integer id, String Book_Name,String Name, String Email,String date_remind,String beforeweek,String afterweek,byte[] image) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Book_Name", Book_Name);
        contentValues.put("Name", Name);
        contentValues.put("Email", Email);
        contentValues.put("Date_remind", date_remind);
        contentValues.put("Date_lend", sdf.format(new Date(System.currentTimeMillis())));
        contentValues.put("Email_Reminders", beforeweek);
        contentValues.put("Email_Reminders1", afterweek);
        contentValues.put("imagefield",   image);
        db.update("Book_Details", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
    public boolean deletebookdetails (String Book_Name,String Name, String Email) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Book_Name", Book_Name);
        contentValues.put("Name", Name);
        contentValues.put("Email", Email);
        String query = "DELETE  FROM Book_Details WHERE Book_Name ='" + Book_Name + "'" + "and Email ='" + Email + "'" + "and Name ='" + Name + "'";
        db.execSQL(query);
        return true;
    }
    public List<Map<String, String>> getAllBooks() {
        ArrayList<String> array_list = new ArrayList<String>();

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Book_Details", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            map = new HashMap<String, String>();
            map.put("BookName", res.getString(res.getColumnIndex(COLUMN_Book_Name)));
            map.put("PersonName", res.getString(res.getColumnIndex(COLUMN_NAME)));
            list.add(map);

            array_list.add(res.getString(res.getColumnIndex(COLUMN_Book_Name))) ;
          //  array_list.add(res.getString(res.getColumnIndex(COLUMN_ID)));
            res.moveToNext();
        }
        return list;
     //   return array_list;
    }
    public List<Map<String, String>> getAllBooks_bookName(String bookName) {
        ArrayList<String> array_list = new ArrayList<String>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Book_Details where Book_Name='"+bookName+"'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            map = new HashMap<String, String>();
            map.put("BookName", res.getString(res.getColumnIndex(COLUMN_Book_Name)));
            map.put("PersonName", res.getString(res.getColumnIndex(COLUMN_NAME)));
            list.add(map);

            array_list.add(res.getString(res.getColumnIndex(COLUMN_Book_Name))) ;
            //  array_list.add(res.getString(res.getColumnIndex(COLUMN_ID)));
            res.moveToNext();
        }
        return list;
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Book_Details where id="+id+"", null );
        return res;
    }

}
