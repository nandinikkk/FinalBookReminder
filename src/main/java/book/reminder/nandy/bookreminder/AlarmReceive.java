package book.reminder.nandy.bookreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String email_to=intent.getStringExtra("email_to");
        String email_from= intent.getStringExtra("email_from");
        String book_name=intent.getStringExtra("book_name");
        String content = intent.getStringExtra("Content");
        String pwd = intent.getStringExtra("pwd");

        Log.d("Alarm receiver","on receive ");

    try {
new BackGround().execute(email_to,email_from,book_name,content,pwd);

        Log.d(getClass().getSimpleName(), "I ran!");
      } catch (Exception e) {
          Log.d("Alarm receiver","error ");
          e.printStackTrace();
      }
    }

}