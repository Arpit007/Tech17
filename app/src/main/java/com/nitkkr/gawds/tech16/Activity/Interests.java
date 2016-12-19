package com.nitkkr.gawds.tech16.Activity;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.nitkkr.gawds.tech16.Adapter.InterestAdapter;
import com.nitkkr.gawds.tech16.Helper.ActionBarDoneButton;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.SignInStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Interests extends AppCompatActivity
{
	private InterestAdapter adapter;
	private ProgressDialog mProgressDialog;
	String token,interests_post_data;

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

					AppUserModel appUserModel=AppUserModel.MAIN_USER;

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
					interests_post_data="[";
					//TODO: Send Info
					ArrayList<String> s=AppUserModel.MAIN_USER.getInterests();
					for(int i=0;i<s.size()-1;i++){
						interests_post_data+='"'+s.get(i)+'"'+",";
					}
					interests_post_data+='"'+s.get(s.size()-1)+'"'+"]";
					send_interests();

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

	public void send_interests(){
		showProgressDialog("Optimising your feed...");
		StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.server_url)+
				getResources().getString(R.string.get_user_interests_url),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String res) {

						//check response if good send to handler
						JSONObject response= null,status=null;
						JSONArray data;
						String message,RollNo,PhoneNumber,Branch,Year,College,Gender;
						ArrayList<String> interests=new ArrayList<String>();
						int code;
						try {
							response = new JSONObject(res);
							status=response.getJSONObject("status");

							code=status.getInt("code");

							if(code==200){
								interests_status(SignInStatus.SUCCESS);
							}else{
								//failure
								interests_status(SignInStatus.FAILED);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						Toast.makeText(Interests.this,res,Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(Interests.this,error.toString(),Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				}){
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("token",token);
				params.put("interests",interests_post_data);
				return params;
			}

		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);

	}

	public void interests_status(SignInStatus status){
		switch (status)
		{
			case SUCCESS:
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
		super.onBackPressed();
	}
}
