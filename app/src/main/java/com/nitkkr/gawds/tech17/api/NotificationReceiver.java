package com.nitkkr.gawds.tech17.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Dell on 29-Dec-16.
 */
public class NotificationReceiver extends BroadcastReceiver {

    // Triggered by the Alarm periodically (starts the service to run task)
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent i = new Intent(context, NotificationService.class);
        context.startService(i);
    }
}
