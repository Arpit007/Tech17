package com.nitkkr.gawds.tech16.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nitkkr.gawds.tech16.Helper.ActionBarSimple;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.SignInStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;

public class Login extends AppCompatActivity
{
	boolean signingIn = false;
	boolean exit=false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ActionBarSimple barSimple = new ActionBarSimple(this);
		barSimple.setLabel(getString(R.string.FestName));
	}


	public void SignIn(View view)
	{

		signingIn = true;
		SignInStatus status = SignInStatus.NONE;

		//TODO:Gmail SignIn

		switch (status)
		{
			case FAILED:
				Toast.makeText(getBaseContext(), "Failed to LogIn", Toast.LENGTH_LONG).show();
				break;
			case SUCCESS:
				Toast.makeText(getBaseContext(), "SignIn Successful", Toast.LENGTH_SHORT).show();

				AppUserModel appUserModel=new AppUserModel();

				//TODO: Load User Data

				AppUserModel.MAIN_USER=appUserModel;
				AppUserModel.MAIN_USER.saveAppUser(Login.this);

				if(!ActivityHelper.isDebugMode(getApplicationContext()))
				{
					Crashlytics.setUserName(AppUserModel.MAIN_USER.getName());
					Crashlytics.setUserEmail(AppUserModel.MAIN_USER.getEmail());
				}
				if(getIntent().getBooleanExtra("Start_Home",true))
					startActivity(new Intent(Login.this, Home.class));
				else
				{
					Intent intent=new Intent();
					intent.putExtra("Logged_In",true);
					setResult(RESULT_OK,intent);
				}
				finish();
				break;
			case SIGNUP:
				Intent intent=new Intent(Login.this, SignUp.class);
				intent.putExtra("Start_Home",getIntent().getBooleanExtra("Start_Home",true));
				startActivity(intent);
				break;
			default:
				break;
		}
	}

	public void SignUp(View view)
	{
		Intent intent=new Intent(Login.this, SignUp.class);
		intent.putExtra("Start_Home",getIntent().getBooleanExtra("Start_Home",true));
		startActivity(intent);
	}

	public void Skip(View view)
	{
		SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE).edit();
		editor.putBoolean("Skip",true);
		editor.apply();

		AppUserModel.MAIN_USER.logoutUser(getBaseContext());
		if(getIntent().getBooleanExtra("Start_Home",true) || isTaskRoot())
			startActivity(new Intent(Login.this, Home.class));
		finish();
	}

	@Override
	public void onBackPressed()
	{
		if (signingIn)
		{
			Toast.makeText(getBaseContext(), "Please Wait", Toast.LENGTH_SHORT).show();
		}
		else
		{
			if(exit)
				super.onBackPressed();

			if(isTaskRoot())
			{
				if(!exit)
				{
					exit = true;
					Toast.makeText(Login.this, "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
					new Handler().postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
							exit = false;
						}
					}, getResources().getInteger(R.integer.WarningDuration));
				}
			}
			else super.onBackPressed();
		}
	}
}
