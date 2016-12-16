package com.nitkkr.gawds.tech16.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.nitkkr.gawds.tech16.Helper.ActionBarSimple;
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

public class Login extends AppCompatActivity  implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener
{
	boolean signingIn = false;
	boolean exit=false;
	private static final int RC_SIGN_IN = 007;
	private  String TAG="DEBUG";
	private GoogleApiClient mGoogleApiClient;
	private ProgressDialog mProgressDialog;
	private SignInButton btnSignIn;
	private boolean issignout;
	String personName,personPhotoUrl,email,token_user,token_recieved;
	SignInStatus success=SignInStatus.SUCCESS;
	SignInStatus failed=SignInStatus.FAILED;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ActionBarSimple barSimple = new ActionBarSimple(this);
		barSimple.setLabel(getString(R.string.FestName));

		Typewriter login_type=(Typewriter)findViewById(R.id.label);
		login_type.animateText("      Login");
		login_type.setCharacterDelay(80);
		btnSignIn = (SignInButton) findViewById(R.id.login_Gmail);
		btnSignIn.setOnClickListener(this);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.requestIdToken("726783559264-o574f9bvum7qdnlusrdmh0rnshqfnr8h.apps.googleusercontent.com")
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
		// Customizing G+ button
		btnSignIn.setSize(SignInButton.SIZE_STANDARD);
		btnSignIn.setScopes(gso.getScopeArray());

		issignout=getIntent().getBooleanExtra("isLogout",false);


	}
	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
	}
	private void handleSignInResult(GoogleSignInResult result) {

		Log.d(TAG, "handleSignInResult:" + result.isSuccess());
		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.
			GoogleSignInAccount acct = result.getSignInAccount();

			Log.v(TAG, "display name: " + acct.getDisplayName());

			 personName = acct.getDisplayName();
			 personPhotoUrl = acct.getPhotoUrl().toString();
			 email = acct.getEmail();
			token_user=acct.getIdToken().toString();
			Log.e(TAG, "Name: " + personName + ", email: " + email
					+ ", Image: " + personPhotoUrl+" token :"+token_user);

			sendToken();
		} else {
			// Signed out, show unauthenticated UI.
			SignIn(failed);
		}
	}


	public void sendToken(){
		showProgressDialog("Verifying");

		StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.server_url)+
				getResources().getString(R.string.login_post_url),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String res) {

						//check response if good send to handler
						JSONObject response= null,status=null,data=null;
						String message;
						int code;
						boolean isNew;
						try {
							response = new JSONObject(res);
							status=response.getJSONObject("status");
							data=response.getJSONObject("data");

							code=status.getInt("code");
							message=status.getString("message");

							isNew=data.getBoolean("isNew");
							token_recieved=data.getString("token");

							//save this token for further use

							if(code==200){
								Log.v(TAG,message);
								//success
								if(isNew){
									SignInStatus sign_up=SignInStatus.SIGNUP;
									SignIn(sign_up);
								}else{
									//get things first
									fetch_user_details();
								}
							}else{
								//failure
								SignIn(failed);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}


						Toast.makeText(Login.this,res,Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				}){
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
//				params.put("gmailToken",token_user);
				params.put("gmailToken","d5bb3540281a8993c1963aaacd85ab57109f13288b0142b7f936c9ad8476d37f0483e1c2ac0166");

				return params;
			}

		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}

	public void fetch_user_details(){

		showProgressDialog("Locating you in our beacon..");
		StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.server_url)+
				getResources().getString(R.string.get_user_details_url),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String res) {

						//check response if good send to handler
						JSONObject response= null,status=null,data=null;
						String message,RollNo,PhoneNumber,Branch,Year,College,Gender;
						int code;
						try {
							response = new JSONObject(res);
							status=response.getJSONObject("status");
							data=response.getJSONObject("data");

							code=status.getInt("code");
							message=status.getString("message");

							if(code==200){
								Log.v(TAG,message);

								RollNo= String.valueOf(data.getInt("RollNo"));
								PhoneNumber=data.getString("PhoneNumber");
								College=data.getString("College");
								Branch=data.getString("Branch");
								Gender=data.getString("Gender");
								Year=data.getString("Year");
								//saving user data
								AppUserModel.MAIN_USER.setName(personName);
								AppUserModel.MAIN_USER.setEmail(email);
								AppUserModel.MAIN_USER.setImageResource(personPhotoUrl);
								AppUserModel.MAIN_USER.setisCoordinator(false);
								AppUserModel.MAIN_USER.setToken(token_recieved);
								AppUserModel.MAIN_USER.setRoll(RollNo);
								AppUserModel.MAIN_USER.setCollege(College);
								AppUserModel.MAIN_USER.setMobile(PhoneNumber);
								AppUserModel.MAIN_USER.setBranch(Branch);
								AppUserModel.MAIN_USER.setGender(Gender);
								AppUserModel.MAIN_USER.setYear(Year);

								AppUserModel.MAIN_USER.saveAppUser(Login.this);

								//now fetch interests
								fetch_interests();

							}else{
								//failure
								SignIn(failed);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}


						Toast.makeText(Login.this,res,Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				}){
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("token",token_recieved);
				return params;
			}

		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);

	}

	public void fetch_interests(){
		showProgressDialog("Optimising your feed...");
		StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.server_url)+
				getResources().getString(R.string.get_interests_url),
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
							data=response.getJSONArray("data");

							code=status.getInt("code");
							message=status.getString("message");

							if(code==200){
								Log.v(TAG,message);
								for(int i=0;i<data.length();i++){
									interests.add(data.getString(i));
								}
								AppUserModel.MAIN_USER.setInterests_arraylist(interests);

								//everything is fetched and saved now
								//so intent to home
								SignIn(success);
							}else{
								//failure
								SignIn(failed);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}


						Toast.makeText(Login.this,res,Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				}){
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("token",token_recieved);
				return params;
			}

		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);

	}
	public void SignIn(SignInStatus status)
	{

		signingIn = true;

		switch (status)
		{
			case FAILED:
				Toast.makeText(getBaseContext(), "Failed to LogIn", Toast.LENGTH_LONG).show();
				break;
			case SUCCESS:
				Toast.makeText(getBaseContext(), "SignIn Successful", Toast.LENGTH_SHORT).show();


				if(!ActivityHelper.isDebugMode(getApplicationContext()))
				{
					Crashlytics.setUserName(AppUserModel.MAIN_USER.getName());
					Crashlytics.setUserEmail(AppUserModel.MAIN_USER.getEmail());
				}
				if(getIntent().getBooleanExtra("Start_Home",true))
					startActivity(new Intent(Login.this, Home.class));
				else
				{
					Intent intent=new Intent();
					intent.putExtra("Logged_In",true);
					setResult(RESULT_OK,intent);
				}
				finish();
				break;
			case SIGNUP:
				Intent intent=new Intent(Login.this, SignUp.class);
				intent.putExtra("Start_Home",getIntent().getBooleanExtra("Start_Home",true));
				startActivity(intent);
				break;
			default:
				break;
		}
	}
	private void signOut() {
		if(mGoogleApiClient.isConnected()) {
			Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
					new ResultCallback<Status>() {
						@Override
						public void onResult(Status status) {

						}
					});
		}
	}
	@Override
	public void onStart() {
		super.onStart();

		if(!issignout) {
			OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
			if (opr.isDone()) {
				// If the user's cached credentials are valid, the OptionalPendingResult will be "done"
				// and the GoogleSignInResult will be available instantly.
				Log.d(TAG, "Got cached sign-in");
				GoogleSignInResult result = opr.get();
				handleSignInResult(result);

			} else {
				// If the user has not previously signed in on this device or the sign-in has expired,
				// this asynchronous branch will attempt to sign in the user silently.  Cross-device
				// single sign-on will occur in this branch.
				showProgressDialog("Loading");
				opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
					@Override
					public void onResult(GoogleSignInResult googleSignInResult) {
						hideProgressDialog();
						handleSignInResult(googleSignInResult);
					}
				});
			}
		}else{
			signOut();
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

	public void SignUp(View view)
	{
		Intent intent=new Intent(Login.this, SignUp.class);
		intent.putExtra("Start_Home",getIntent().getBooleanExtra("Start_Home",true));
		startActivity(intent);
	}

	public void Skip(View view)
	{
		SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE).edit();
		editor.putBoolean("Skip",true);
		editor.apply();

		AppUserModel.MAIN_USER.logoutUser(getBaseContext());
		if(getIntent().getBooleanExtra("Start_Home",true) || isTaskRoot())
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
			if(exit)
				super.onBackPressed();

			if(isTaskRoot())
			{
				if(!exit)
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
			else super.onBackPressed();
		}
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		// An unresolvable error has occurred and Google APIs (including Sign-In) will not
		// be available.
		Log.d(TAG, "onConnectionFailed:" + connectionResult);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
			case R.id.login_Gmail:
				signIn();
				break;
		}
	}
}
