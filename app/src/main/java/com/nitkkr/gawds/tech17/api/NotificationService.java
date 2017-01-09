package com.nitkkr.gawds.tech17.api;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.database.Database;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dell on 29-Dec-16.
 */
public class NotificationService extends IntentService
{
    //4 Minutes
    public static int intervalMillis = 4*60*1000;
    private static int refreshMin = 2;
    private boolean isNewDb = false;

    public NotificationService()
    {
        super("NotificationService");
        Log.i("Notification Service","======================Service Constructed=============");
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        Log.i("Notification Service","======================Service Started=============");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Log.i("Notification Service","======================Service Stopped=============");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        boolean Forced = intent.getBooleanExtra("Forced",false);

        if(isRunTime() || Forced)
        {
            Log.i("Notification Service", "===================Task Started========================");

            isNewDb=false;
            Database database = Database.getServiceInstance();

            if(database==null)
            {
                isNewDb=true;
                database=new Database(getApplicationContext());
            }
            synchronized (database)
            {
                try
                {
                    RunTask(database);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else Log.i("Notification Service", "===================Service Called Early========================");
    }

    private boolean isRunTime()
    {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Notification_Service", Context.MODE_PRIVATE);

        boolean LastRunStatus = preferences.getBoolean("Status",false);

        long LastRun=preferences.getLong("LastRun",(new Date()).getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(LastRun));
        calendar.add(Calendar.MINUTE,refreshMin);

        LastRun=calendar.getTimeInMillis();
        long CurrentRun = new Date().getTime();

        return (CurrentRun > LastRun || !LastRunStatus);
    }

    private void saveRunTime(boolean Status)
    {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Notification_Service", Context.MODE_PRIVATE).edit();
        editor.putLong("LastRun",new Date().getTime());
        editor.putBoolean("Status",Status);
        editor.commit();
    }

    private void RunTask(Database database)
    {


        saveRunTime(true);
        if(isNewDb && database != null)
            database.closeDatabase();
    }
}
