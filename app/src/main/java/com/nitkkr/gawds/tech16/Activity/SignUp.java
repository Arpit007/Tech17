package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.Helper.ActionBarSimple;
import com.nitkkr.gawds.tech16.Helper.SignInStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.Model.UserModel;
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

		ActionBarSimple barSimple = new ActionBarSimple(this);
		barSimple.setLabel(getString(R.string.FestName));
	}

	public void Authenticate(View view)
	{
		Processing = true;

		SignInStatus status=SignInStatus.NONE;

		//TODO: Authenticate and Get Email address

		switch (status)
		{
			case FAILED:
				Toast.makeText(SignUp.this,"Failed to Authenticate, Try Again",Toast.LENGTH_LONG).show();
				break;
			case SUCCESS:
				Verified = true;
				findViewById(R.id.signup_Email).setVisibility(View.GONE);
				TextView textView = (TextView) findViewById(R.id.signup_EmailFinal);
				textView.setText("-------------------------Email Here---------------------------");
				textView.setVisibility(View.VISIBLE);
				break;
			case OTHER:
				Toast.makeText(SignUp.this,"---------Message Here-----------",Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}

		Processing = false;
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
			UserModel user=new UserModel();
			user.setName((( EditText)findViewById(R.id.signup_Name)).getText().toString());
			if(( (RadioButton) findViewById(R.id.signup_NitRadio) ).isChecked())
				user.setCollege("NIT Kurukshetra");
			else user.setCollege(((EditText)findViewById(R.id.signup_CollegeName)).getText().toString());
			user.setRoll((( EditText)findViewById(R.id.signup_Roll)).getText().toString());
			user.setMobile((( EditText)findViewById(R.id.signup_Number)).getText().toString());
			user.setBranch(((Spinner)findViewById(R.id.signup_Branch)).getSelectedItem().toString());
			user.setEmail((( TextView)findViewById(R.id.signup_EmailFinal)).getText().toString());


			SignInStatus status=SignInStatus.NONE;

			//TODO: Send Info

			switch (status)
			{
				case FAILED:
					Toast.makeText(this,"Failed to Sign Up, Please Try Again",Toast.LENGTH_LONG).show();
					break;
				case SUCCESS:
					Intent intent=new Intent(SignUp.this, Interests.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("User",user);
					bundle.putBoolean("Start_Home",getIntent().getBooleanExtra("Start_Home",true));
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				default:
					break;
			}
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
