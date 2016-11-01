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

import com.nitkkr.gawds.tech16.Model.UserModel;

public class Login extends AppCompatActivity
{
	ProgressDialog progressDialog;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	private boolean Recover(String Email)
	{
		if(dialog!=null)
			progressDialog=new ProgressDialog(dialog.getContext());

		progressDialog.show();
		//TODO: Implement, No registered Email, Failure
		progressDialog.dismiss();
		return true;
	}

	public void SignIn(View view){
		String UserName=((TextView)findViewById(R.id.login_UserName)).getText().toString();
		String Password=(( EditText)findViewById(R.id.login_Password)).getText().toString();
		progressDialog=new ProgressDialog(getBaseContext());
		progressDialog.show();
		//TODO:Implement Sign In, Wrong Password, Load Home
		progressDialog.dismiss();
	}
	public void SignUp(View view){
		startActivity(new Intent(Login.this,SignUp.class));
	}
	public void Skip(View view){
		UserModel.USER_MODEL.logoutUser(getBaseContext());
		startActivity(new Intent(Login.this,Home.class));
	}
	public void ForgotPassword(View view) {
		dialog=new Dialog(getBaseContext());
		dialog.setContentView(R.layout.dialog_forgot_password);

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		dialog.getWindow().setLayout((int)(0.9 * width),  WindowManager.LayoutParams.WRAP_CONTENT);

		dialog.findViewById(R.id.Recover).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(Recover((( TextView)dialog.findViewById(R.id.login_ForgotEmail)).getText().toString()))
				{
					Toast.makeText(getBaseContext(),getString(R.string.recover_string),Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		if(progressDialog!=null && progressDialog.isShowing())
			return;

		if(dialog!=null && dialog.isShowing())
			dialog.dismiss();
		else
			super.onBackPressed();
	}
}
