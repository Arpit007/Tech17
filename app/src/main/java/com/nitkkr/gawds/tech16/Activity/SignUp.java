package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.Helper.ActionBarSimple;
import com.nitkkr.gawds.tech16.R;

public class SignUp extends AppCompatActivity
{

	boolean Processing, Verified = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		( (RadioButton) findViewById(R.id.signup_NitRadio) ).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b)
			{
				if (b)
				{
					findViewById(R.id.signup_OtherCollege).setVisibility(View.GONE);
				}
				else
				{
					findViewById(R.id.signup_OtherCollege).setVisibility(View.VISIBLE);
				}
			}
		});

		//TODO: Add Branches Data
		String Branches[] = getResources().getStringArray(R.array.Branches);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, Branches);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		( (Spinner) findViewById(R.id.signup_Branch) ).setAdapter(adapter);

	}

	public void Authenticate(View view)
	{
		Processing = true;

		//TODO: Authenticate and Get Email address

		Processing = false;
		Verified = true;
		findViewById(R.id.signup_Email).setVisibility(View.GONE);
		TextView textView = (TextView) findViewById(R.id.signup_EmailFinal);
		textView.setText("Email Here");
		textView.setVisibility(View.VISIBLE);

		ActionBarSimple barSimple = new ActionBarSimple(this);
		barSimple.setLabel(getString(R.string.FestName));
	}

	boolean Check()
	{
		if (( (TextView) findViewById(R.id.signup_Name) ).getText().toString().trim().equals(""))
		{
			return false;
		}

		if (( (RadioButton) findViewById(R.id.signup_OtherRadio) ).isChecked() && ( (TextView) ( findViewById(R.id.signup_CollegeName) ) ).getText().toString().trim().equals(""))
		{
			return false;
		}

		if (( (TextView) findViewById(R.id.signup_Number) ).getText().toString().trim().equals("") || ( (TextView) findViewById(R.id.signup_Number) ).getText().length() < 10)
		{
			return false;
		}

		if (( (String) ( (Spinner) findViewById(R.id.signup_Branch) ).getSelectedItem() ).trim().equals(""))
		{
			return false;
		}

		if (!Verified)
		{
			return false;
		}

		return true;
	}

	public void SignUp(View view)
	{
		if (Check())
		{
			//TODO: Save User Data, Interests
			startActivity(new Intent(SignUp.this, Interests.class));
			finish();
		}
		else
		{
			findViewById(R.id.signup_Warning).setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					findViewById(R.id.signup_Warning).setVisibility(View.INVISIBLE);
				}
			}, getResources().getInteger(R.integer.WarningDuration));
		}
	}

	@Override
	public void onBackPressed()
	{
		if (Processing)
		{
			Toast.makeText(getBaseContext(), "Please Wait", Toast.LENGTH_SHORT).show();
		}
		else
		{
			super.onBackPressed();
		}
	}
}
