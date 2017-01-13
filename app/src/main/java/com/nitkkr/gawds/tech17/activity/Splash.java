package com.nitkkr.gawds.tech17.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.api.FetchService;
import com.nitkkr.gawds.tech17.api.NotificationReceiver;
import com.nitkkr.gawds.tech17.api.NotificationService;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.model.AppUserModel;
import com.nitkkr.gawds.tech17.src.RateApp;
import com.nitkkr.gawds.tech17.src.Typewriter;
import com.nitkkr.gawds.tech17.src.UpdateCheck;

import io.fabric.sdk.android.Fabric;

import static com.nitkkr.gawds.tech17.activity.Login.mGoogleApiClient;

public class Splash extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
	boolean secondPermissionRequest = false;
	private static final int STORAGE_PERMISSION_CODE = 1;
	private Handler handler = new Handler();
	View upper, lower;
	Animation slideup, slidedown, imageSLideUp;
	Thread runAnimationUp = new Thread()
	{
		@Override
		public void run()
		{
			upper.startAnimation(slideup);
		}
	};
	Thread runAnimationDown = new Thread()
	{
		@Override
		public void run()
		{
			lower.startAnimation(slidedown);
		}
	};
	Typewriter splashTypewriter;
	ImageView ts_logo;

	private Runnable runnable = new Runnable()
	{
		public void run()
		{
			SharedPreferences preferences = getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE);
			boolean Skip = preferences.getBoolean("Skip", false);

			if (Skip)
			{
				startActivity(new Intent(Splash.this, Home.class));
				AppUserModel.MAIN_USER.logoutUser(getApplicationContext());
			}
			else if (AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext()))
			{
				if (!AppUserModel.MAIN_USER.isUserSignedUp(getBaseContext()))
				{
					Toast.makeText(Splash.this.getApplicationContext(), "Continuing From Where You Left", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(Splash.this, SignUp.class);
					intent.putExtra("Start_Home", getIntent().getBooleanExtra("Start_Home", true));
					startActivity(intent);
				}
				else if (Database.getInstance().getInterestDB().getSelectedInterests().size() == 0)
				{
					Toast.makeText(Splash.this.getApplicationContext(), "Continiuing From Where You Left", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(Splash.this, Interests.class);
					intent.putExtra("Start_Home", getIntent().getBooleanExtra("Start_Home", true));
					startActivity(intent);
				}
				else
				{
					startActivity(new Intent(Splash.this, Home.class));
				}
			}
			else
			{
				Intent intent = new Intent(Splash.this, About.class);
				intent.putExtra("Login", true);
				startActivity(intent);
			}

			finish();
			ActivityHelper.setExitAnimation(Splash.this);
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		ActivityHelper.setUpHelper(getApplicationContext());

		Database.deleteDatabase(getApplicationContext());

		ActivityHelper.setCreateAnimation(this);

		if (!ActivityHelper.isDebugMode(getApplicationContext()))
		{
			Fabric.with(this, new Crashlytics());
		}

		setContentView(R.layout.activity_splash);

		startNotify();

		ActivityHelper.setStatusBarColor(this);

		ts_logo = (ImageView) findViewById(R.id.ts_logo);
		upper = findViewById(R.id.upper_splash);
		lower = findViewById(R.id.lower_splash);

		splashTypewriter = (Typewriter) findViewById(R.id.splash_text);
		splashTypewriter.setText("");
		splashTypewriter.setCharacterDelay(70);

		slidedown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
		slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
		imageSLideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_slide_up);

		ts_logo.startAnimation(imageSLideUp);
		runAnimationDown.start();
		runAnimationUp.start();
		splashTypewriter.animateText("      Techspardha '17");

		if (!ActivityHelper.isInternetConnected() && Database.getInstance().getEventsDB().getRowCount() == 0)
		{
			Toast.makeText(getApplicationContext(), "First Run Requires Network Connection\nExiting...", Toast.LENGTH_LONG).show();
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					System.exit(-1);
				}
			}, 5000);
			return;
		}
		else if(Database.getInstance().getEventsDB().getRowCount()==0)
		{
			Intent intent=new Intent(Splash.this,FetchService.class);
			intent.putExtra("Forced", true);
			startService(intent);
			intent=new Intent(Splash.this,NotificationService.class);
			intent.putExtra("Forced", true);
			startService(intent);
		}

		Thread thread = new Thread()
		{
			@Override
			public void run()
			{
				if (!isReadStorageAllowed())
				{
					ActivityCompat.requestPermissions(Splash.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, STORAGE_PERMISSION_CODE);
				}
				else
				{
					start_app();
				}
			}
		};

		thread.setPriority(4);
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
		final AlertDialog alertDialog = builder.create();
		alertDialog.setOnShowListener(
				new DialogInterface.OnShowListener()
				{
					@Override
					public void onShow(DialogInterface arg0)
					{

						alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(Splash.this, R.color.button_color));
						alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(Splash.this, R.color.button_color));
						alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(Splash.this, R.color.button_color));
					}
				});
		alertDialog.getWindow().getAttributes().windowAnimations = R.style.CloseDialogTheme;
		alertDialog.show();
	}

	public void start_app()
	{
		Database database = new Database(getApplicationContext());
		Log.d("Instance: ", database.toString() + " Started");
		AppUserModel.MAIN_USER.loadAppUser(getApplicationContext());

		if (!ActivityHelper.isDebugMode(getApplicationContext()))
		{
			if (AppUserModel.MAIN_USER.isUserLoggedIn(this))
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

		UpdateCheck.getInstance().checkForUpdate(getApplicationContext());

		RateApp.getInstance().incrementAppStartCount(getApplicationContext());

		if (AppUserModel.MAIN_USER.isUserLoggedIn(this))
		{
			try
			{
				GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
						.requestEmail()
						.build();

				mGoogleApiClient = new GoogleApiClient.Builder(this)
						.enableAutoManage(this, this)
						.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
						.build();
				OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
				if (opr.isDone())
				{
					GoogleSignInResult result = opr.get();
					if (result.isSuccess())
					{
						GoogleSignInAccount acct = result.getSignInAccount();
						try
						{
							AppUserModel.MAIN_USER.setImageResource(acct.getPhotoUrl().toString());
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		handler.postDelayed(runnable, getResources().getInteger(R.integer.SplashDuration));
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		switch (requestCode)
		{
			case STORAGE_PERMISSION_CODE:
				if (isReadStorageAllowed())
				{
					start_app();
				}
				else if (!secondPermissionRequest)
				{
					requestStoragePermission();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Failed to Get Write Permissions.\nExiting...", Toast.LENGTH_LONG).show();
					new Handler().postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
							finish();
							System.exit(-1);
						}
					}, getResources().getInteger(R.integer.AppCloseDuration));
				}
				break;
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void onBackPressed()
	{
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{
	}

	public void startNotify()
	{
		Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
		intent.putExtra("NotificationService",true);
		final PendingIntent pIntent = PendingIntent.getBroadcast(this, NotificationReceiver.NotificationServiceCode,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, 10, NotificationService.intervalMillis, pIntent);


		Intent intent1 = new Intent(getApplicationContext(), NotificationReceiver.class);
		intent1.putExtra("NotificationService",false);
		final PendingIntent pIntent1 = PendingIntent.getBroadcast(this, NotificationReceiver.FetchServiceCode,
				intent1, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarm1 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		alarm1.setInexactRepeating(AlarmManager.RTC_WAKEUP, 10, FetchService.intervalMillis, pIntent1);
	}
}
