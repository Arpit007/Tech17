package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;

public class Splash extends AppCompatActivity
{
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable()
	{
		public void run()
		{
			AppUserModel.MAIN_USER.loadUser(getApplicationContext());

			/*if (AppUserModel.MAIN_USER.isUserLoaded())
			{
				if (AppUserModel.MAIN_USER.getInterests().size() == 0)
				{
					Toast.makeText(Splash.this, "Please Complete this", Toast.LENGTH_LONG).show();
					startActivity(new Intent(Splash.this, Interests.class));
				}
				else
				{
					startActivity(new Intent(Splash.this, Home.class));
				}
			}
			else
			{
				startActivity(new Intent(Splash.this, Login.class));
			}*/
			startActivity(new Intent(Splash.this, Event.class));
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
