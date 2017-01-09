package com.nitkkr.gawds.tech17.api;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.model.AppUserModel;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Dell on 29-Dec-16.
 */
public class NotificationService extends IntentService
{
    //5 Minutes
    public static int intervalMillis = 5*60*1000;
    private static int refreshMin = 2;
    private boolean isNewDb = false;

    private final int maxFetchCount=4;
    private int fetchCount=0;
    private boolean Failed=false;
    private boolean Forced = false;

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
        Forced = intent.getBooleanExtra("Forced",false);

        if(isRunTime() || Forced)
        {
            Log.i("Notification Service", "===================Task Started========================");

            if(!ActivityHelper.isInternetConnected(getApplicationContext()))
            {
                if(Forced)
                    Toast.makeText(getApplicationContext(),"No Network Connection",Toast.LENGTH_SHORT).show();
                saveRunTime(false);
                return;
            }

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

    private void RunTask(final Database database)
    {
        if (!(AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext()) && AppUserModel.MAIN_USER.isUserSignedUp(getBaseContext())))
            return;

        iResponseCallback callback=new iResponseCallback()
        {
            @Override
            public void onResponse(ResponseStatus status)
            {
                fetchCount++;
                if(fetchCount==maxFetchCount)
                {
                    fetchCount=0;

                    if(status==ResponseStatus.FAILED || status== ResponseStatus.NONE)
                        Failed=true;


                    saveRunTime(!Failed);

                    if(isNewDb && database != null)
                        database.closeDatabase();

                    if(Forced && Failed)
                    {
                        Toast.makeText(getApplicationContext(),"Failed Fetching Data",Toast.LENGTH_SHORT).show();
                    }
                    Failed=false;
                    Forced=false;
                    Log.i("Notification Service","=======================Task Completed=========================");
                }
            }

            @Override
            public void onResponse(ResponseStatus status, Object object)
            {
                this.onResponse(status);
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(FetchData.fetchUserWishlist(getApplicationContext(),database,callback));
        requestQueue.add(FetchData.getNotifications(getApplicationContext(),database,callback));
        requestQueue.add(FetchData.getMyTeams(getApplicationContext(), database, callback));
    }
}
