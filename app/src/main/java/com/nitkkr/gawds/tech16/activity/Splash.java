package com.nitkkr.gawds.tech16.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.FetchData;
import com.nitkkr.gawds.tech16.model.AppUserModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.src.CheckUpdate;
import io.fabric.sdk.android.Fabric;
import com.nitkkr.gawds.tech16.src.RateApp;
import com.nitkkr.gawds.tech16.src.Typewriter;

public class Splash extends AppCompatActivity
{
	boolean secondPermissionRequest=false;
	private static final int STORAGE_PERMISSION_CODE =1 ;
	private Handler handler = new Handler();
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
			SharedPreferences preferences=getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE);
			boolean Skip=preferences.getBoolean("Skip",false);

			if(Skip)
			{
				startActivity(new Intent(Splash.this,Home.class));
			}
			else if(AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext()) && !AppUserModel.MAIN_USER.isUserSignedUp(getBaseContext())){
				startActivity(new Intent(Splash.this,Login.class));
			}
			//if  logged in
			else if(AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext())){
				startActivity(new Intent(Splash.this,Home.class));
			}
			else{
				startActivity(new Intent(Splash.this,Login.class));
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
		splashTypewriter.animateText("      Techspardha 17");

		Thread thread=new Thread()
		{
			@Override
			public void run()
			{
				if(!isReadStorageAllowed())
				{
					ActivityCompat.requestPermissions(Splash.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
				}
				else start_app();
			}
		};
		thread.start();
	}

	private boolean isReadStorageAllowed()
	{
		//Getting the permission status
		int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		//If permission is granted returning true
		return result == PackageManager.PERMISSION_GRANTED;
	}

	private void requestStoragePermission()
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
		builder.setCancelable(false);
		builder.setTitle("Permission Request");
		builder.setMessage("SD Card Write Permission Needed for App's Functioning");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				dialogInterface.dismiss();
				secondPermissionRequest = true;
				ActivityCompat.requestPermissions(Splash.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, STORAGE_PERMISSION_CODE);
			}
		});
		builder.create().show();
	}

	public void start_app()
	{
		Database database=new Database(getApplicationContext());
		Log.d("Instance: ",database.toString() +" Started");

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
				/*
				Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
				{
					@Override
					public void uncaughtException(Thread thread, Throwable throwable)
					{
						throwable.printStackTrace();
					}
				});*/
		}

		CheckUpdate.getInstance().checkForUpdate(getApplicationContext());

		RateApp.getInstance().incrementAppStartCount(getApplicationContext());

		SharedPreferences preferences=getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE);
		boolean Skip=preferences.getBoolean("Skip",false);

		if(Skip)
		{
			FetchData.getInstance().fetchAllEvents(getBaseContext());
			FetchData.getInstance().fetchAllInterests(getBaseContext());
		}
		else if(AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext()) && !AppUserModel.MAIN_USER.isUserSignedUp(getBaseContext()))
		{
			FetchData.getInstance().fetchAllEvents(getBaseContext());
			FetchData.getInstance().fetchAllInterests(getBaseContext());
		}
		//if  logged in
		else if(AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext()))
		{
			FetchData.getInstance().fetchUserInterests(getBaseContext());
			FetchData.getInstance().fetchAllEvents(getBaseContext());
		}
		else
		{
			FetchData.getInstance().fetchAllEvents(getBaseContext());
			FetchData.getInstance().fetchAllInterests(getBaseContext());
		}

		handler.postDelayed(runnable, getResources().getInteger(R.integer.SplashDuration));
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		switch (requestCode)
		{
			case STORAGE_PERMISSION_CODE:
				if(isReadStorageAllowed())
					start_app();
				else if(!secondPermissionRequest)
					requestStoragePermission();
				else
				{
					Toast.makeText(getApplicationContext(),"Failed to Get Write Permissions.\nExiting...",Toast.LENGTH_LONG).show();
					new Handler().postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
							finish();
							System.exit(-1);
						}
					},getResources().getInteger(R.integer.AppCloseDuration));
				}
				break;
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void onBackPressed()
	{
	}
}
