package com.nitkkr.gawds.tech16.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.Src.CheckUpdate;

public class Splash extends AppCompatActivity
{
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable()
	{
		public void run()
		{
			AppUserModel.MAIN_USER.loadAppUser(getApplicationContext());

			CheckUpdate.CHECK_UPDATE.checkForUpdate();

			SharedPreferences preferences=getSharedPreferences("App_Prefs", Context.MODE_PRIVATE);
			boolean Skip=preferences.getBoolean("Skip",false);

			if (AppUserModel.MAIN_USER.isUserLoaded() || Skip)
			{
				startActivity(new Intent(Splash.this, Home.class));
			}
			else
				AppUserModel.MAIN_USER.LoginUser(Splash.this,false);
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
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
