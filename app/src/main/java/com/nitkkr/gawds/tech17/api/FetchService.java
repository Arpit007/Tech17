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
import com.nitkkr.gawds.tech17.src.UpdateCheck;

import java.util.Calendar;
import java.util.Date;

public class FetchService extends IntentService
{
	//1 hour
	public static int intervalMillis = 60 * 60 * 1000;
	private static int refreshMin = 40;
	private boolean isNewDb = false;

	private int maxFetchCount=6;
	private int fetchCount=0;
	private boolean Failed=false;
	private boolean Forced = false;

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
		Forced = intent.getBooleanExtra("Forced",false);

		SharedPreferences preferences = getApplicationContext().getSharedPreferences("Fetch_Service", Context.MODE_PRIVATE);

		boolean LastRunStatus = preferences.getBoolean("Status",false);

		long LastRun=preferences.getLong("LastRun",(new Date()).getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(LastRun));
		calendar.add(Calendar.MINUTE,5);

		if(LastRunStatus && LastRun>new Date().getTime())
			Forced=false;

		if(isRunTime() || Forced)
		{
			Log.i("Fetch Service", "===================Task Started========================");

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

	private void RunTask(final Database database)
	{
		if ( AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext()) && AppUserModel.MAIN_USER.isUserSignedUp(getBaseContext()))
			maxFetchCount=6;
		else maxFetchCount=4;

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
					Log.i("Fetch Service","=======================Task Completed=========================");
				}
			}

			@Override
			public void onResponse(ResponseStatus status, Object object)
			{
				this.onResponse(status);
			}
		};

		UpdateCheck.getInstance().checkForUpdate(getApplicationContext());
		RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
		requestQueue.add(FetchData.fetchInterests(getApplicationContext(),database,callback));
		requestQueue.add(FetchData.fetchData(getApplicationContext(),database,callback));
		requestQueue.add(FetchData.getSocieties(getApplicationContext(),database,callback));

		if ( AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext()) && AppUserModel.MAIN_USER.isUserSignedUp(getBaseContext()))
		{
			requestQueue.add(FetchData.fetchUserInterests(getApplicationContext(), database, callback));
			requestQueue.add(FetchData.getUserDetails(getApplicationContext(), database, callback));
		}
	}
}
