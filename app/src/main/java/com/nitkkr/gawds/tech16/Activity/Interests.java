package com.nitkkr.gawds.tech16.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nitkkr.gawds.tech16.Adapter.InterestAdapter;
import com.nitkkr.gawds.tech16.Helper.ActionBarDoneButton;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.SignInStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;

public class Interests extends AppCompatActivity
{
	private InterestAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interests);

		ListView listView = (ListView) findViewById(R.id.interest_list);
		adapter = new InterestAdapter(getBaseContext());

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				adapter.onItemClick(view, i);
			}
		});

		listView.setAdapter(adapter);

		ActionBarDoneButton barDone = new ActionBarDoneButton(this, new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (adapter.isDone())
				{
					SignInStatus status=SignInStatus.NONE;
					AppUserModel appUserModel=(AppUserModel)getIntent().getExtras().getSerializable("User");
					try
					{
						appUserModel.setInterests(adapter.getInterestsString());
					}
					catch (Exception e)
					{
						e.printStackTrace();
						appUserModel=new AppUserModel();
					}

					//Used for Edit User
					if(getIntent().getBooleanExtra("Return_Interest",false))
					{
						Intent data=new Intent();
						data.putExtra("Interests",appUserModel.interestsToString());
						setResult(RESULT_OK,data);
						finish();
						return;
					}

					//TODO: Send Info
					switch (status)
					{
						case SUCCESS:
							AppUserModel.MAIN_USER=appUserModel;
							AppUserModel.MAIN_USER.saveAppUser(Interests.this);

							SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE).edit();
							editor.putBoolean("Skip",false);
							editor.apply();

							if(!ActivityHelper.isDebugMode(getApplicationContext()))
							{
								Crashlytics.setUserName(AppUserModel.MAIN_USER.getName());
								Crashlytics.setUserEmail(AppUserModel.MAIN_USER.getEmail());
							}
							//Check if it works as not part of bundle=================
							if(getIntent().getExtras().getBoolean("Start_Home",true))
								startActivity(new Intent(Interests.this, Home.class));
							else
							{
								Intent intent=new Intent();
								intent.putExtra("Logged_In",true);
								setResult(RESULT_OK,intent);
							}
							finish();
							break;
						case FAILED:
							Toast.makeText(Interests.this,"Failed, Please Try Again",Toast.LENGTH_LONG).show();
							break;
						case OTHER:
							Toast.makeText(Interests.this,"----------------message-------------------",Toast.LENGTH_LONG).show();
							break;
						default:
							break;
					}
				}
				else
				{
					Toast.makeText(getBaseContext(), "Select minimum 1 Interest", Toast.LENGTH_LONG).show();
				}
			}
		});
		barDone.setLabel("Interests");

	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
	}
}
