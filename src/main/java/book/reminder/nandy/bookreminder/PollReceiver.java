package book.reminder.nandy.bookreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.util.Log;

import static android.content.Context.ALARM_SERVICE;

public class PollReceiver extends BroadcastReceiver
{

    String daterem,email_to,frmemail,bname,pwd;
    int id;

    @Override
    public void onReceive(Context context, Intent i)
    {
        Log.d("Poll receiver","i am in poll ");
        scheduleAlarms(context,daterem,email_to,frmemail,id,bname,pwd);


    }

    static void scheduleAlarms(Context context,String dateremind,String email,String from_email,int i,String bookName,String pwd)
    {
        Log.d("Poll receiver","i am in schedule Alarms ");

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        String emailid = email;
        String fromemail=from_email;
        Intent emailIntent = new Intent(context, AlarmReceive.class);
        emailIntent.putExtra("email_to", emailid);
        emailIntent.putExtra("email_from", fromemail);
        emailIntent.putExtra("book_name", bookName);
        emailIntent.putExtra("pwd", pwd);
        emailIntent.putExtra("Content", "This is the first reminder");
        PendingIntent emailAlarm = PendingIntent.getBroadcast(context, i,
                emailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String insertDate =dateremind;
        String[] items1 = insertDate.split("-");
        String y1=items1[0];
        String m1=items1[1];
        String d1=items1[2];
        int day = Integer.parseInt(d1);
        int month1 = Integer.parseInt(m1);
        int month = month1-1;
        int year = Integer.parseInt(y1);
        Calendar alarm= Calendar.getInstance();
        Log.d("year","year ");
        System.out.println(year);
        Log.d("month","month");
        System.out.println(month);
        Log.d("day","day");
        System.out.println(day);
       alarm.set(Calendar.YEAR, year);
        alarm.set(Calendar.MONTH, month);
        alarm.set(Calendar.DAY_OF_MONTH, day);
        alarm.set(Calendar.HOUR_OF_DAY, 8);
        alarm.set(Calendar.MINUTE, 0);
        alarm.set(Calendar.SECOND, 0);

        alarmManager.set(AlarmManager.RTC,
                alarm.getTimeInMillis(), emailAlarm);
        Log.d("Poll receiver","i am after schedule Alarms ");


    }
    static void unscheduleAlarms(Context context,int i)
    {
        Log.d("Poll receiver","i am in unschedule Alarms ");
        AlarmManager alarmManagerstop = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent emailIntentstop = new Intent(context, AlarmReceive.class);
        PendingIntent emailAlarmstop = PendingIntent.getBroadcast(context, i,emailIntentstop, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManagerstop.cancel(emailAlarmstop);


    }
    static void scheduleRepeatingAlarms(Context context,String dateremind,String email,String from_email,int i,long repeat,String bookName,String pwd)
    {
        Log.d("Poll receiver","i am in schedule Alarms ");
        String insertDate =dateremind;
        String[] items1 = insertDate.split("-");
        String y1=items1[0];
        String m1=items1[1];
        String d1=items1[2];
        int day = Integer.parseInt(d1);
        int month1 = Integer.parseInt(m1);
        int month = month1-1;
        int year = Integer.parseInt(y1);
        Calendar alarm= Calendar.getInstance();
        Log.d("year","year ");
        System.out.println(year);
        Log.d("month","month");
        System.out.println(month);
        Log.d("day","day");
        System.out.println(day);
        alarm.set(Calendar.YEAR, year);
        alarm.set(Calendar.MONTH, month);
        alarm.set(Calendar.DAY_OF_MONTH, day);
        alarm.set(Calendar.HOUR_OF_DAY, 11);
        alarm.set(Calendar.MINUTE, 13);
        alarm.set(Calendar.SECOND, 0);
        if(System.currentTimeMillis() ==  alarm.getTimeInMillis()+ 7 *24 * 60 * 60 * 1000) {

            unscheduleAlarms(context,i);

        }
        else if(System.currentTimeMillis() <  alarm.getTimeInMillis()+ 7 *24 * 60 * 60 * 1000)
        {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            String emailid = email;
            String fromemail=from_email;
            Intent emailIntent = new Intent(context, AlarmReceive.class);
            emailIntent.putExtra("email_to", emailid);
            emailIntent.putExtra("email_from", fromemail);
            emailIntent.putExtra("book_name", bookName);
            emailIntent.putExtra("pwd", pwd);
            emailIntent.putExtra("Content", "This is reminder after due date  till one week");
            PendingIntent emailAlarm = PendingIntent.getBroadcast(context, i,
                    emailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC,
                    alarm.getTimeInMillis() + repeat, repeat, emailAlarm);
            Log.d("Poll receiver", "i am after schedule Alarms ");
        }
    }
    static void scheduleRepeatingAlarms_afterWeek(Context context,String dateremind,String email,String from_email,int i,long repeat,String bookName,String pwd)
    {
        Log.d("Poll receiver","i am in schedule Alarms ");
        String insertDate =dateremind;
        String[] items1 = insertDate.split("-");
        String y1=items1[0];
        String m1=items1[1];
        String d1=items1[2];
        int day = Integer.parseInt(d1);
        int month1 = Integer.parseInt(m1);
        int month = month1-1;
        int year = Integer.parseInt(y1);
        Calendar alarm= Calendar.getInstance();
        Log.d("year","year ");
        System.out.println(year);
        Log.d("month","month");
        System.out.println(month);
        Log.d("day","day");
        System.out.println(day);
        alarm.set(Calendar.YEAR, year);
        alarm.set(Calendar.MONTH, month);
        alarm.set(Calendar.DAY_OF_MONTH, day);
        alarm.set(Calendar.HOUR_OF_DAY, 11);
        alarm.set(Calendar.MINUTE, 13);
        alarm.set(Calendar.SECOND, 0);


            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            String emailid = email;
            String fromemail=from_email;
            Intent emailIntent = new Intent(context, AlarmReceive.class);
            emailIntent.putExtra("email_to", emailid);
            emailIntent.putExtra("email_from", fromemail);
            emailIntent.putExtra("book_name", bookName);
        emailIntent.putExtra("pwd", pwd);
            emailIntent.putExtra("Content", "This is reminder after one week");
            PendingIntent emailAlarm = PendingIntent.getBroadcast(context, i,
                    emailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
             long afterweek = 7 * 24 * 60 * 60 * 1000;
            alarmManager.setRepeating(AlarmManager.RTC,
                    alarm.getTimeInMillis() + afterweek+ repeat, repeat, emailAlarm);
            Log.d("Poll receiver", "i am after schedule Alarms ");

    }



}