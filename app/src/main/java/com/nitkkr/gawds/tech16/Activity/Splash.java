package com.nitkkr.gawds.tech16.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.nitkkr.gawds.tech16.Database.Database;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.fetch_data;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.Src.CheckUpdate;
import io.fabric.sdk.android.Fabric;
import com.nitkkr.gawds.tech16.Src.RateApp;
import com.nitkkr.gawds.tech16.Src.Typewriter;

public class Splash extends AppCompatActivity
{
	private Handler handler = new Handler();
	fetch_data f=fetch_data.getInstance();
	View upper, lower;
	Animation slideup, slidedown,imageSLideUp;
	Thread runAnimationUp = new Thread() {
		@Override
		public void run() {
			upper.startAnimation(slideup);
		}
	};
	Thread runAnimationDown = new Thread() {
		@Override
		public void run() {
			lower.startAnimation(slidedown);
		}
	};
	Typewriter splashTypewriter;
	ImageView ts_logo;

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

			CheckUpdate.getInstance().checkForUpdate(getApplicationContext());

			RateApp.getInstance().incrementAppStartCount(getApplicationContext());

			SharedPreferences preferences=getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE);
			boolean Skip=preferences.getBoolean("Skip",false);

			//fetch events in background
			//and store it in table and update the existing ones

			//Starting Database  No Fetching
			Database database=new Database(getApplicationContext());
			Log.d("Instance: ",database.toString() +" Started");

			//if skip or logged in
			if(Skip)
			{
				startActivity(new Intent(Splash.this,Home.class));
				f.fetch_events(getBaseContext());

			}
			else if(AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext()) && !AppUserModel.MAIN_USER.isUserSignedUp(getBaseContext())){
				startActivity(new Intent(Splash.this,Login.class));
				f.fetch_events(getBaseContext());
			}
			//if  logged in
			else if(AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext())){
				startActivity(new Intent(Splash.this,Home.class));
				f.fetch_interests(getBaseContext());
				f.fetch_events(getBaseContext());
			}
			else{
				startActivity(new Intent(Splash.this,Login.class));
				f.fetch_events(getBaseContext());
			}

			finish();
		}
	};



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		ActivityHelper.setApplictionContext(getApplicationContext());

		if(!ActivityHelper.isDebugMode(getApplicationContext()))
		{
			Fabric.with(this, new Crashlytics());
		}

		setContentView(R.layout.activity_splash);

		ts_logo = (ImageView) findViewById(R.id.ts_logo);
		upper = findViewById(R.id.upper_splash);
		lower = findViewById(R.id.lower_splash);

		splashTypewriter = (Typewriter) findViewById(R.id.splash_text);
		splashTypewriter.setText("");
		splashTypewriter.setCharacterDelay(80);

		slidedown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
		slideup=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
		imageSLideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.image_slide_up);
		ts_logo.startAnimation(imageSLideUp);

		runAnimationDown.start();
		runAnimationUp.start();
		splashTypewriter.animateText("     TechSpardha    ");


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
