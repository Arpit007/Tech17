package com.nitkkr.gawds.tech17.api;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.nitkkr.gawds.tech17.database.Database;

import java.util.Calendar;
import java.util.Date;

public class FetchService extends IntentService
{
	//1 hour
	public static int intervalMillis = 60 * 60 * 1000;
	private static int refreshMin = 40;
	private boolean isNewDb = false;

	public FetchService()
	{
		super("FetchService");
		Log.i("Fetch Service","======================Service Constructed=============");
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
		Log.i("Fetch Service","======================Service Started=============");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		Log.i("Fetch Service","======================Service Stopped=============");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		boolean Forced = intent.getBooleanExtra("Forced",false);

		if(isRunTime() || Forced)
		{
			Log.i("Fetch Service", "===================Task Started========================");

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
		else Log.i("Fetch Service", "===================Service Called Early========================");
	}

	private boolean isRunTime()
	{
		SharedPreferences preferences = getApplicationContext().getSharedPreferences("Fetch_Service", Context.MODE_PRIVATE);

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
		SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Fetch_Service", Context.MODE_PRIVATE).edit();
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
