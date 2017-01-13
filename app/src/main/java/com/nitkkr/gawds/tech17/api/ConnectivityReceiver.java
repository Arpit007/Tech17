package com.nitkkr.gawds.tech17.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.model.AppUserModel;

import io.fabric.sdk.android.Fabric;

public class ConnectivityReceiver extends BroadcastReceiver
{
	public ConnectivityReceiver()
	{
	}

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
		Intent i = new Intent(context, FetchService.class);
		context.startService(i);
		Intent i1 = new Intent(context, NotificationService.class);
		context.startService(i1);
	}
}
