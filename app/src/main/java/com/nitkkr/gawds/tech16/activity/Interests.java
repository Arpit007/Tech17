package com.nitkkr.gawds.tech16.activity;

import android.app.ProgressDialog;
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
import com.nitkkr.gawds.tech16.adapter.InterestAdapter;
import com.nitkkr.gawds.tech16.api.iResponseCallback;
import com.nitkkr.gawds.tech16.helper.ActionBarDoneButton;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.fetchData;
import com.nitkkr.gawds.tech16.helper.ResponseStatus;
import com.nitkkr.gawds.tech16.model.AppUserModel;
import com.nitkkr.gawds.tech16.R;

public class Interests extends AppCompatActivity
{
	private AppUserModel appUserModel=(AppUserModel)AppUserModel.MAIN_USER.clone();
	private InterestAdapter adapter;
	private ProgressDialog mProgressDialog;
	String token;

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
					appUserModel.setInterests(adapter.getFinalList());

					//Used for Edit User
					if(getIntent().getBooleanExtra("Return_Interest",false))
					{
						Intent data=new Intent();
						Bundle bundle=new Bundle();
						bundle.putSerializable("Interests",adapter.getFinalList());
						data.putExtras(bundle);
						setResult(RESULT_OK,data);
						finish();
					}
					else sendInterests();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Select minimum 1 Interest", Toast.LENGTH_LONG).show();
				}
			}
		});

		barDone.setLabel("Interests");
		token=AppUserModel.MAIN_USER.getToken();
	}

	public void sendInterests()
	{
		showProgressDialog("Uploading Information, Please Wait...");
		fetchData.getInstance().sendInterests(getApplicationContext(), adapter.getFinalList(), appUserModel, new iResponseCallback()
		{
			@Override
			public void onResponse(ResponseStatus status)
			{
				hideProgressDialog();
				serverResponse(status);
			}

			@Override
			public void onResponse(ResponseStatus status, Object object)
			{
				this.onResponse(status);
			}
		});
	}

	public void serverResponse(ResponseStatus status){
		switch (status)
		{
			case SUCCESS:
				appUserModel.saveAppUser(Interests.this);
				AppUserModel.MAIN_USER=appUserModel;

				SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE).edit();
				editor.putBoolean("Skip",false);
				editor.apply();

				if(!ActivityHelper.isDebugMode(getApplicationContext()))
				{
					Crashlytics.setUserName(AppUserModel.MAIN_USER.getName());
					Crashlytics.setUserEmail(AppUserModel.MAIN_USER.getEmail());
				}
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
				Toast.makeText(Interests.this,"Network error",Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}
	}

	private void showProgressDialog(String msg) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(msg);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(false);
		}

		mProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.hide();
		}
	}

	@Override
	public void onBackPressed()
	{
		if (mProgressDialog!=null && mProgressDialog.isShowing())
			return;
		super.onBackPressed();
	}
}
