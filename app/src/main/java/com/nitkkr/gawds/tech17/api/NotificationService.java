package com.nitkkr.gawds.tech17.api;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.model.AppUserModel;
import com.nitkkr.gawds.tech17.src.NotificationGenerator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dell on 29-Dec-16.
 */
public class NotificationService extends IntentService
{
    //5 Minutes
    public static int intervalMillis = 10*60*1000;
    private static int refreshMin = 3;

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

        synchronized (this)
        {
            if (isRunTime() || Forced)
            {
                if (Forced)
                    Log.i("Notification Service", "===================Forced Task Started========================");

                Log.i("Notification Service", "===================Task Started========================");

                if (!ActivityHelper.isInternetConnected(getApplicationContext()))
                {
                    if (Forced)
                        Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
                    saveRunTime(false);
                    return;
                }

                synchronized (Database.getInstance())
                {
                    try
                    {
                        RunTask();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else
                Log.i("Notification Service", "===================Service Called Early========================");
        }
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

        //return (CurrentRun > LastRun || !LastRunStatus);
        return true;
    }

    private void saveRunTime(boolean Status)
    {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Notification_Service", Context.MODE_PRIVATE).edit();
        editor.putLong("LastRun",new Date().getTime());
        editor.putBoolean("Status",Status);

        if(Status)
            editor.putLong("SuccessTime",new Date().getTime());

        editor.commit();
    }

    private void RunTask()
    {
        if (!(AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext()) && AppUserModel.MAIN_USER.isUserSignedUp(getBaseContext())))
            return;

        final long oldInviteCount=Database.getInstance().getTeamDB().getInviteCount();
        final long oldNotificationCount=Database.getInstance().getNotificationDB().getUnreadNotificationCount();

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
                    else Failed=false;


                    saveRunTime(!Failed);

                    long newInviteCount=Database.getInstance().getTeamDB().getInviteCount();
                    long newNotificationCount=Database.getInstance().getNotificationDB().getUnreadNotificationCount();

                    if(newInviteCount>oldInviteCount)
                    {
                        NotificationGenerator generator = new NotificationGenerator(getApplicationContext());
                        generator.inviteNotification(newInviteCount-oldInviteCount);
                    }
                    if(newNotificationCount>oldNotificationCount)
                    {
                        NotificationGenerator generator = new NotificationGenerator(getApplicationContext());
                        generator.messageNotification(newNotificationCount-oldNotificationCount);
                    }

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

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Notification_Service", Context.MODE_PRIVATE);

        Date date=new Date(preferences.getLong("SuccessTime",0));
        if(date.getTime()==0)
        {
            Calendar calendar=Calendar.getInstance();
            calendar.set(Calendar.YEAR,2016);
            date.setTime(calendar.getTimeInMillis());
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(FetchData.fetchUserWishlist(getApplicationContext(),callback));
        requestQueue.add(FetchData.getNotifications(getApplicationContext(),date,callback));
        requestQueue.add(FetchData.getMyTeams(getApplicationContext(), callback));
    }
}
