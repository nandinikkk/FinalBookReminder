package book.reminder.nandy.bookreminder;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ScheduledService extends IntentService {
    String to,from,book,content,pwd;
    public ScheduledService() {
        super("ScheduledService");
        new BackGround().execute(to,from,book,content,pwd);
        Log.d(" ScheduledService","on constructor ");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        //this is asynk task class
      //  SendAttachment.main(to,from,book,content);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    //    SendAttachment.main(to,from,book,content);

       // SendAttachment.main();
       Log.d(getClass().getSimpleName(), "I ran!");
    }
}
