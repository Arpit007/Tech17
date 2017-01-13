package com.nitkkr.gawds.tech17.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.model.AppUserModel;

import io.fabric.sdk.android.Fabric;

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
        if(!Fabric.isInitialized() && !ActivityHelper.isDebugMode(context))
        {
            Fabric.with(context, new Crashlytics());
            if(AppUserModel.MAIN_USER.isUserLoggedIn(context))
            {
                Crashlytics.setUserName(AppUserModel.MAIN_USER.getName());
                Crashlytics.setUserEmail(AppUserModel.MAIN_USER.getEmail());
            }
        }

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
