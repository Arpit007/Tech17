package com.nitkkr.gawds.tech17.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectivityReceiver extends BroadcastReceiver
{
	public ConnectivityReceiver()
	{
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{

		Intent i = new Intent(context, FetchService.class);
		context.startService(i);
		Intent i1 = new Intent(context, NotificationService.class);
		context.startService(i1);
	}
}
