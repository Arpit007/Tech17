package com.nitkkr.gawds.tech17.api;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.util.Log;

import com.nitkkr.gawds.tech17.database.Database;

/**
 * Created by Dell on 29-Dec-16.
 */
public class NotificationService extends IntentService
{
    private static NotificationService service;

    public NotificationService()
    {
        super("NotificationService");
        Log.i("Service","======================Service Constructed=============");
        service=this;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        Log.i("Service","======================Service Started=============");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Log.i("Service","======================Service Stopped=============");
    }

    public static NotificationService getService(){return service;}

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.i("Service", "Intent Received");
        Database database = Database.getServiceInstance();
        if(database==null)
        {
            database=new Database(getApplicationContext());
        }
        synchronized (Database.getInstance())
        {
            try
            {
                Log.i("Service",Database.getInstance().getEventsDB().getEvent(6).getEventName());
                Log.i("Service", "Query Success");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
