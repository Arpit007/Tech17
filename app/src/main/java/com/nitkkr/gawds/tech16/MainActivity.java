package com.nitkkr.gawds.tech16;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nitkkr.gawds.tech16.API.Encryption;
import com.nitkkr.gawds.tech16.Model.UserModel;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static android.R.attr.key;

public class MainActivity extends AppCompatActivity
{
	private Handler handler=new Handler();
	private Runnable runnable = new Runnable()
	{
		public void run()
		{
			UserModel.USER_MODEL.loadUser(getApplicationContext());

			if(UserModel.USER_MODEL.isUserLoaded())
				startActivity(new Intent(MainActivity.this,Home.class));
			else
				startActivity(new Intent(MainActivity.this,Login.class));
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
	public void onBackPressed(){}
}
