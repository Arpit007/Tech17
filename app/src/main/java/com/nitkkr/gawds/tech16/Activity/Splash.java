package com.nitkkr.gawds.tech16.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.Src.CheckUpdate;
import io.fabric.sdk.android.Fabric;
import com.nitkkr.gawds.tech16.Src.RateApp;

public class Splash extends AppCompatActivity
{
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable()
	{
		public void run()
		{
			AppUserModel.MAIN_USER.loadAppUser(getApplicationContext());

			if(!ActivityHelper.isDebugMode(getApplicationContext()))
			{
				if (AppUserModel.MAIN_USER.isUserLoaded())
				{
					Crashlytics.setUserName(AppUserModel.MAIN_USER.getName());
					Crashlytics.setUserEmail(AppUserModel.MAIN_USER.getEmail());
				}
			}
			else
			{
				Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
				{
					@Override
					public void uncaughtException(Thread thread, Throwable throwable)
					{
						//throwable.printStackTrace();
					}
				});
			}

			CheckUpdate.CHECK_UPDATE.checkForUpdate(getApplicationContext());

			RateApp.rateApp.incrementAppStartCount(getApplicationContext());

			SharedPreferences preferences=getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE);
			boolean Skip=preferences.getBoolean("Skip",false);

			if (AppUserModel.MAIN_USER.isUserLoaded() || Skip)
			{
				startActivity(new Intent(Splash.this, Home.class));
			}
			else
				AppUserModel.MAIN_USER.LoginUserHome(Splash.this,false);
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if(!ActivityHelper.isDebugMode(getApplicationContext()))
		{
			Fabric.with(this, new Crashlytics());
		}

		setContentView(R.layout.activity_splash);

		final Thread timerThread = new Thread()
		{
			public void run()
			{
				handler.postDelayed(runnable, getResources().getInteger(R.integer.SplashDuration));
			}
		};

		timerThread.start();
	}

	@Override
	public void onBackPressed()
	{
		return;
	}
}
