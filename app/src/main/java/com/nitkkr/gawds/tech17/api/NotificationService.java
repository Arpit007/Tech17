package com.nitkkr.gawds.tech17.api;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Dell on 29-Dec-16.
 */
public class NotificationService extends IntentService {

    public NotificationService(String name) {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //TODO: poll on server
        FetchData.getInstance().getNotifications(getBaseContext());

        //TODO:instantiate all the notifications received from API
        Log.i("MyTestService", "Service running");
    }
}
