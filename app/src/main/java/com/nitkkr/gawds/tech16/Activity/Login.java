package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.Helper.ActionBarSimple;
import com.nitkkr.gawds.tech16.Helper.SignInStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;

public class Login extends AppCompatActivity
{
	boolean signingIn = false;

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
				//TODO:Save User Data
				startActivity(new Intent(Login.this, Home.class));
				finish();
				break;
			case SIGNUP:
				startActivity(new Intent(Login.this, SignUp.class));
				break;
			default:
				break;
		}


	}

	public void SignUp(View view)
	{
		startActivity(new Intent(Login.this, SignUp.class));
	}

	public void Skip(View view)
	{
		AppUserModel.MAIN_USER.logoutUser(getBaseContext());
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
			super.onBackPressed();
		}
	}
}
