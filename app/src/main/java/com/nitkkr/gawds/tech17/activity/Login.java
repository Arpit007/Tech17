package com.nitkkr.gawds.tech17.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.api.FetchData;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.model.AppUserModel;
import com.nitkkr.gawds.tech17.model.InterestModel;
import com.nitkkr.gawds.tech17.src.Typewriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    Thread runAnimationUp = new Thread()
	{
		@Override
		public void run()
		{
			findViewById(R.id.view3).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up));
		}
	};
	Thread runAnimationDown = new Thread()
	{
		@Override
		public void run()
		{
			findViewById(R.id.view2).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down));
		}
	};

	boolean exit = false;
	private static final int RC_SIGN_IN = 678;
	static public GoogleApiClient mGoogleApiClient;
	static public GoogleSignInOptions gso;
	private ProgressDialog mProgressDialog;
	public static final String client_server_id = "726783559264-o574f9bvum7qdnlusrdmh0rnshqfnr8h.apps.googleusercontent.com";

	AppUserModel userModel = new AppUserModel();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ActivityHelper.setCreateAnimation(this);

		Database.getInstance().ResetTables();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{

			ActivityHelper.setStatusBarColor(this);
		}

		Typewriter login_type = (Typewriter) findViewById(R.id.signup_label);
		login_type.animateText("    Login");
		login_type.setCharacterDelay(80);

		findViewById(R.id.login_Gmail).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
                if (!ActivityHelper.isInternetConnected())
                {
                    Toast.makeText(Login.this, "No Network Connection", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    googleLogin();
                }
			}
		});

		gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.requestIdToken(client_server_id)
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		runAnimationUp.start();
		runAnimationDown.start();
	}

	private void googleLogin()
	{
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN)
		{
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
	}

	private void handleSignInResult(GoogleSignInResult result)
	{
		Log.d("DEBUG", "handleSignInResult:" + result.isSuccess());
		if (result.isSuccess())
		{
			GoogleSignInAccount acct = result.getSignInAccount();
			Log.v("DEBUG",acct.getIdToken()+"");
			if (acct != null)
			{
				userModel.setName(acct.getDisplayName());
				try
				{
					userModel.setImageResource(acct.getPhotoUrl().toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				userModel.setEmail(acct.getEmail());
				userModel.setToken(acct.getIdToken());
			}
			sendToken();
		}
		else
		{
			LogInResult(ResponseStatus.FAILED);
		}
	}

	private void stopAutoManage()
	{
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.stopAutoManage(Login.this);
        }
	}

	public void sendToken()
	{
		showProgressDialog("Verifying");
		Log.v("login", "Sending Token");

		StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.server_url) +
				getResources().getString(R.string.login_post_url),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response, data;
						int code;
						boolean isNew;
						try
						{
							response = new JSONObject(res);

							data = response.getJSONObject("data");
							code = response.getJSONObject("status").getInt("code");

							isNew = data.getBoolean("IsNew");

							if (code == 200)
							{
								Log.v("login", response.toString());

								userModel.setToken(data.getString("token"));

								if (isNew)
								{
									ResponseStatus sign_up = ResponseStatus.SIGNUP;
									LogInResult(sign_up);
								}
								else
								{
									fetchUserDetails();
								}
							}
							else
							{
								LogInResult(ResponseStatus.FAILED);
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							LogInResult(ResponseStatus.FAILED);
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						LogInResult(ResponseStatus.FAILED);
					}
				})
		{
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String> params = new HashMap<>();
				params.put("idToken", userModel.getToken());
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		stopAutoManage();

	}

	public void fetchUserDetails()
	{
		showProgressDialog("Fetching User Details...");
		StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.server_url) +
				getResources().getString(R.string.get_user_details_url) + "?token=" + userModel.getToken(),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response, data;
						int code;
						try
						{
							response = new JSONObject(res);
							data = response.getJSONObject("data");

							code = response.getJSONObject("status").getInt("code");

							if (code == 200)
							{
								userModel.setRoll(String.valueOf(data.getLong("RollNo")));
								userModel.setMobile(data.getString("PhoneNumber"));
								userModel.setCollege(data.getString("College"));
								userModel.setBranch(data.getString("Branch"));
								userModel.setGender(data.getString("Gender"));
								userModel.setYear(data.getString("Year"));

								fetchUserInterests();
							}
							else
							{
								LogInResult(ResponseStatus.FAILED);
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							LogInResult(ResponseStatus.FAILED);
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						LogInResult(ResponseStatus.FAILED);
					}
				});

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}

	public void LogInResult(ResponseStatus status)
	{
		hideProgressDialog();

		switch (status)
		{
			case FAILED:
				userModel = new AppUserModel();
				Toast.makeText(getBaseContext(), "Failed to LogIn", Toast.LENGTH_SHORT).show();
				break;

			case SUCCESS:
				AppUserModel.MAIN_USER = userModel;
				AppUserModel.MAIN_USER.setLoggedIn(true, getBaseContext());
				AppUserModel.MAIN_USER.setSignedup(true, getBaseContext());
				AppUserModel.MAIN_USER.setUseGoogleImage(false);
                if (AppUserModel.MAIN_USER.getGender().toLowerCase().equals("male"))
                {
                    AppUserModel.MAIN_USER.setImageId(0);
                }
                else
                {
                    AppUserModel.MAIN_USER.setImageId(1);
                }
				AppUserModel.MAIN_USER.saveAppUser(this);

				Toast.makeText(getBaseContext(), "SignIn Successful", Toast.LENGTH_SHORT).show();

				getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE).edit().putBoolean("Skip", false).commit();

				FetchData.getInstance().fetchUserWishlist(getApplicationContext());
				FetchData.getInstance().fetchUserInterests(getApplicationContext());
				FetchData.getInstance().getNotifications(getApplicationContext());

				if (!ActivityHelper.isDebugMode(getApplicationContext()))
				{
					Crashlytics.setUserName(AppUserModel.MAIN_USER.getName());
					Crashlytics.setUserEmail(AppUserModel.MAIN_USER.getEmail());
					Answers.getInstance().logLogin(new LoginEvent()
							.putMethod("Google: " + AppUserModel.MAIN_USER.getName())
							.putSuccess(true));
				}

                if (getIntent().getBooleanExtra("Start_Home", true))
                {
                    startActivity(new Intent(Login.this, Home.class));
                }
                else
                {
                    Intent intent = new Intent();
                    intent.putExtra("Logged_In", true);
                    setResult(RESULT_OK, intent);
                }

				finish();
				ActivityHelper.setExitAnimation(this);
				break;

			case SIGNUP:
				AppUserModel.MAIN_USER = userModel;

				userModel = new AppUserModel();

				AppUserModel.MAIN_USER.setLoggedIn(true, getBaseContext());
				AppUserModel.MAIN_USER.setSignedup(false, getBaseContext());
				AppUserModel.MAIN_USER.saveAppUser(this);

				Intent intent = new Intent(Login.this, SignUp.class);
				intent.putExtra("Start_Home", getIntent().getBooleanExtra("Start_Home", true));
				startActivity(intent);
				break;

			default:
				break;
		}
	}

	private void signOut()
	{
		Log.v("Debug", "Logged out");

		if (mGoogleApiClient.isConnected())
		{
			Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>()
			{
				@Override
				public void onResult(Status status)
				{
					Log.v("Debug", "Logged out");
				}
			});
		}
	}

	private void showProgressDialog(String msg)
	{
		if (mProgressDialog == null)
		{
			mProgressDialog = new ProgressDialog(this);
		}
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(msg);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);

		mProgressDialog.show();
	}

	private void hideProgressDialog()
	{
		if (mProgressDialog != null)
		{
			mProgressDialog.dismiss();
		}
	}

	public void SignUp(View view)
	{
		if (!ActivityHelper.isInternetConnected())
		{
			Snackbar.make(this.findViewById(android.R.id.content), "No Network Connection", Snackbar.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(Login.this, SignUp.class);
		intent.putExtra("Start_Home", getIntent().getBooleanExtra("Start_Home", true));
		startActivity(intent);
	}

	public void Skip(View view)
	{
		AppUserModel.MAIN_USER.logoutUser(this);
		SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE).edit();
		editor.putBoolean("Skip", true);
		editor.apply();

        if (getIntent().getBooleanExtra("Start_Home", true) || isTaskRoot())
        {
            startActivity(new Intent(Login.this, Home.class));
        }
		finish();
		ActivityHelper.setExitAnimation(this);
	}

	@Override
	public void onBackPressed()
	{
        if (mProgressDialog != null && mProgressDialog.isShowing())
        {
            return;
        }

		if (exit)
		{
			if (AppUserModel.MAIN_USER.isUserLoggedIn(this) && !AppUserModel.MAIN_USER.isUserSignedUp(this))
			{
				AppUserModel.MAIN_USER.logoutUser(this);
			}
			super.onBackPressed();
		}

		if (isTaskRoot())
		{
			if (!exit)
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
		else
		{
			if (AppUserModel.MAIN_USER.isUserLoggedIn(this) && !AppUserModel.MAIN_USER.isUserSignedUp(this))
			{
				AppUserModel.MAIN_USER.logoutUser(this);
			}
			super.onBackPressed();
		}
		ActivityHelper.setExitAnimation(Login.this);
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{
		Toast.makeText(Login.this, "Network Error", Toast.LENGTH_SHORT).show();
		Log.d("DEBUG", "onConnectionFailed:" + connectionResult);
	}

	public void fetchUserInterests()
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.server_url) +
				getResources().getString(R.string.get_interests_list) + "?token=" + userModel.getToken(),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{

						JSONObject response;
						JSONArray data;
						int code;
						try
						{
							response = new JSONObject(res);
							data = response.getJSONArray("data");
							code = response.getJSONObject("status").getInt("code");

							if (code == 200)
							{
								Database.getInstance().getInterestDB().resetTable();

								ArrayList<InterestModel> list = new ArrayList<>();

								for (int i = 0; i < data.length(); i++)
								{
									InterestModel interestModel = new InterestModel();
									interestModel.setID(data.getInt(i));
									interestModel.setInterest(Database.getInstance().getInterestDB().getInterest(interestModel));
									interestModel.setSelected(true);
									list.add(interestModel);
								}

								Database.getInstance().getInterestDB().addOrUpdateInterest(list);
								LogInResult(ResponseStatus.SUCCESS);
							}
							else
							{
								Log.d("Fetch:\t", "Failed Fetching User Interests");
								LogInResult(ResponseStatus.FAILED);
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							LogInResult(ResponseStatus.FAILED);
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						LogInResult(ResponseStatus.FAILED);
					}
				});

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}
}