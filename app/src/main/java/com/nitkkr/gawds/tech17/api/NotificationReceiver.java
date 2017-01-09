package com.nitkkr.gawds.tech17.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Dell on 29-Dec-16.
 */
public class NotificationReceiver extends BroadcastReceiver
{
    public static final int NotificationServiceCode = 12345;
    public static final int FetchServiceCode = 12785;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        boolean notificationService=intent.getBooleanExtra("NotificationService",true);
        if(notificationService)
        {
            Intent i = new Intent(context, NotificationService.class);
            context.startService(i);
        }
        else
        {
            Intent i = new Intent(context, FetchService.class);
            context.startService(i);
        }
    }
}
