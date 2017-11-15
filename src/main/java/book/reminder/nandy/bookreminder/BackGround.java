package book.reminder.nandy.bookreminder;

import android.os.AsyncTask;
import android.util.Log;


public class BackGround extends AsyncTask<String, Void, String>  {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("Hi", "Download Commencing");
    }

    @Override
    protected String doInBackground(String... params) {
        String email_to = params[0];
        String email_from = params[1];
        String book_name = params[2];
        String content = params[3];
        String pwd = params[4];


        SendAttachment.main(email_to,email_from,book_name,content,pwd);

        return "Executed!";

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("Hi", "Done Downloading.");


    }
}

