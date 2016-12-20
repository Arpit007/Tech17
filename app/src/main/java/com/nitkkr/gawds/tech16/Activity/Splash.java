package com.nitkkr.gawds.tech16.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

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
	private static final int STORAGE_PERMISSION_CODE =1 ;
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


		}
	};


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

			switch (requestCode) {
				case STORAGE_PERMISSION_CODE: {
					// If request is cancelled, the result arrays are empty.
					if (grantResults.length > 0
							&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

						//Database database=new Database(getApplicationContext());
						//Log.d("Instance: ",database.toString() +" Started");


					} else {


						// permission denied, boo! Disable the
						// functionality that depends on this permission.
						Toast.makeText(getBaseContext(),"Hey , You kicked me out, we need that to save Events",Toast.LENGTH_LONG).show();
						finish();
					}
					return;
				}

				// other 'case' lines to check for other
				// permissions this app might request
			}
	}

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
		splashTypewriter.animateText("        Techspardha");

		//First checking if the app is already having the permission
		if(isReadStorageAllowed()){

			//Database database=new Database(getApplicationContext());
			//Log.d("Instance: ",database.toString() +" Started");
			start_app();
		}else{

			//If the app has not the permission then asking for the permission
			requestStoragePermission();
		}



	}
	private boolean isReadStorageAllowed() {
		//Getting the permission status
		int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		//If permission is granted returning true
		if (result == PackageManager.PERMISSION_GRANTED)
			return true;

		//If permission is not granted returning false
		return false;
	}

	//Requesting permission
	private void requestStoragePermission(){

		if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
			//If the user has denied the permission previously your code will come to this block
			//Here you can explain why you need this permission
			//Explain here why you need this permission
		}

		//And finally ask for the permission
		ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
	}

	public void start_app(){

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
