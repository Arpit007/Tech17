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
import com.nitkkr.gawds.tech16.Helper.ActionBarSimple;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.SignInStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;

public class Login extends AppCompatActivity  implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener
{
	boolean signingIn = false;
	boolean exit=false;
	private static final int RC_SIGN_IN = 007;
	private  String TAG="DEBUG";
	private GoogleApiClient mGoogleApiClient;
	private ProgressDialog mProgressDialog;
	private SignInButton btnSignIn;

	String personName,personPhotoUrl,email;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ActionBarSimple barSimple = new ActionBarSimple(this);
		barSimple.setLabel(getString(R.string.FestName));

		btnSignIn = (SignInButton) findViewById(R.id.login_Gmail);
		btnSignIn.setOnClickListener(this);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
		// Customizing G+ button
		btnSignIn.setSize(SignInButton.SIZE_STANDARD);
		btnSignIn.setScopes(gso.getScopeArray());


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
		SignInStatus success=SignInStatus.SUCCESS;
		SignInStatus failed=SignInStatus.FAILED;

		Log.d(TAG, "handleSignInResult:" + result.isSuccess());
		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.
			GoogleSignInAccount acct = result.getSignInAccount();

			Log.v(TAG, "display name: " + acct.getDisplayName());

			 personName = acct.getDisplayName();
			 personPhotoUrl = acct.getPhotoUrl().toString();
			 email = acct.getEmail();

			Log.e(TAG, "Name: " + personName + ", email: " + email
					+ ", Image: " + personPhotoUrl);


			SignIn(success);
		} else {
			// Signed out, show unauthenticated UI.
			SignIn(failed);
		}
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

				AppUserModel appUserModel=new AppUserModel();

				AppUserModel.MAIN_USER=appUserModel;

				//saving user data
				AppUserModel.MAIN_USER.setName(personName);
				AppUserModel.MAIN_USER.setEmail(email);
				AppUserModel.MAIN_USER.setRoll("");
				AppUserModel.MAIN_USER.setCollege("");
				AppUserModel.MAIN_USER.setMobile("");
				AppUserModel.MAIN_USER.setBranch("");
				AppUserModel.MAIN_USER.setImageResource(personPhotoUrl);
				AppUserModel.MAIN_USER.setInterests("");
				AppUserModel.MAIN_USER.setisCoordinator(false);


				AppUserModel.MAIN_USER.saveAppUser(Login.this);

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
	@Override
	public void onStart() {
		super.onStart();

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
			showProgressDialog();
			opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
				@Override
				public void onResult(GoogleSignInResult googleSignInResult) {
					hideProgressDialog();
					handleSignInResult(googleSignInResult);
				}
			});
		}
	}
	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Loading..");
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
