package com.nitkkr.gawds.tech16;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.API.Encryption;
import com.nitkkr.gawds.tech16.Model.UserModel;

public class Login extends AppCompatActivity
{
	private enum SignInState
	{
		NONE,
		FAILED,
		SUCCESS,
		SIGNUP
	}

	boolean signingIn=false;
	ProgressDialog progressDialog;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}


	public void SignIn(View view){

		signingIn=true;
		SignInState state=SignInState.NONE;

		//TODO:Gmail SignIn

		switch (state)
		{
			case FAILED:
				Toast.makeText(getBaseContext(),"Failed to LogIn",Toast.LENGTH_LONG).show();
				break;
			case SUCCESS:
				Toast.makeText(getBaseContext(),"SignIn Successful",Toast.LENGTH_SHORT).show();
				//TODO:Save User Data
				startActivity(new Intent(Login.this,Home.class));
				finish();
				break;
			case SIGNUP:
				startActivity(new Intent(Login.this,SignUp.class));
				break;
			default:break;
		}


	}
	public void SignUp(View view){
		startActivity(new Intent(Login.this,SignUp.class));
	}
	public void Skip(View view){
		UserModel.USER_MODEL.logoutUser(getBaseContext());
		startActivity(new Intent(Login.this,Home.class));
	}

	@Override
	public void onBackPressed()
	{
		if(signingIn)
			Toast.makeText(getBaseContext(),"Please Wait",Toast.LENGTH_SHORT).show();
		else
			super.onBackPressed();
	}
}
