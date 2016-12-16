package com.nitkkr.gawds.tech16.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.nitkkr.gawds.tech16.Helper.SignInStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.Model.UserModel;
import com.nitkkr.gawds.tech16.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{

	boolean Processing, Verified = false;
	private  String TAG="DEBUG";
	private static final int RC_SIGN_IN = 007;
	private GoogleApiClient mGoogleApiClient;
	private ProgressDialog mProgressDialog;
	private GoogleSignInOptions gso;
	String personName,personPhotoUrl,email,token_user,token_recieved,College,Gender,Branch,RollNo,PhoneNumber,Year;

	SignInStatus success=SignInStatus.SUCCESS;
	SignInStatus failed=SignInStatus.FAILED;

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
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_modified,R.id.branch_selected,Branches);
		( (Spinner) findViewById(R.id.signup_Branch) ).setAdapter(adapter);

		String Gender[] = getResources().getStringArray(R.array.Gender);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_modified,R.id.branch_selected,Gender);
		( (Spinner) findViewById(R.id.signup_gender) ).setAdapter(adapter2);


		ActionBarSimple barSimple = new ActionBarSimple(this);
		barSimple.setLabel(getString(R.string.FestName));

		Typewriter signup_label=(Typewriter)findViewById(R.id.signup_label);
		signup_label.animateText("      Sign up");
		signup_label.setCharacterDelay(60);


		//if user logged in first then if found new

		 personName=AppUserModel.MAIN_USER.getName();
		 email=AppUserModel.MAIN_USER.getEmail();
		personPhotoUrl=AppUserModel.MAIN_USER.getImageResource();

		EditText name_editText; Button email_button;
		if(!personName.equals("")){
			name_editText=( EditText)findViewById(R.id.signup_Name);
			name_editText.setText(personName);
		}
		if(!email.equals("")){
			email_button=(Button) findViewById(R.id.signup_Email);
			email_button.setText(email);
		}

		 gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.requestIdToken("726783559264-o574f9bvum7qdnlusrdmh0rnshqfnr8h.apps.googleusercontent.com")
				.build();


		Button google_signIn_btn=(Button)findViewById(R.id.signup_Email);
		google_signIn_btn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mGoogleApiClient = Login.mGoogleApiClient;
						signIn();
					}
				}
		);

		token_recieved=AppUserModel.MAIN_USER.getToken();
		Log.v("debug",token_recieved);
	}
	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode==RESULT_OK)
		{
			// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
			if (requestCode == RC_SIGN_IN)
			{
				GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
				handleSignInResult(result);
			}
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
			signup_result(failed);
		}
	}

	public void Authenticate(View view)
	{
		if(Check()){
			//this is google sign in
			signIn();

		}else{

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
									Button email_button;

									if(!email.equals("")){
										email_button=(Button) findViewById(R.id.signup_Email);
										email_button.setText(email);
									}


								}else{
									//already logged in user
									Toast.makeText(SignUp.this,"Already signed up!!, please login",Toast.LENGTH_LONG).show();
								}
							}else{
								//failure
								signup_result(failed);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}


						Toast.makeText(SignUp.this,res,Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(SignUp.this,error.toString(),Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				}){
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("idToken",token_user);
				return params;
			}

		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}


	boolean Check()
	{
		EditText signup_name=(EditText) findViewById(R.id.signup_Name);
		if (( signup_name ).getText().toString().trim().equals(""))
		{
			signup_name.setError("required");
			return false;
		}

		if (( (RadioButton) findViewById(R.id.signup_OtherRadio) ).isChecked() && ( (EditText) ( findViewById(R.id.signup_CollegeName) ) ).getText().toString().trim().equals(""))
		{
			return false;
		}
		EditText signup_number=( (EditText) findViewById(R.id.signup_Number) );
		if ( signup_number.getText().toString().trim().equals("") )
		{
			signup_number.setError("required");
			return false;
		}
		if(( (TextView) findViewById(R.id.signup_Number) ).getText().length() < 10){
			signup_number.setError("Not valid");
			return false;
		}

		return true;
	}

	public void save_and_send_user_details(){

		showProgressDialog("Saving");

		if(( (RadioButton) findViewById(R.id.signup_NitRadio) ).isChecked())
			College="NIT Kurukshetra";
		else
			College=(((EditText)findViewById(R.id.signup_CollegeName)).getText().toString());

		RollNo=(( EditText)findViewById(R.id.signup_Roll)).getText().toString();
		PhoneNumber=(( EditText)findViewById(R.id.signup_Number)).getText().toString();
		Gender=((Spinner)findViewById(R.id.signup_gender)).getSelectedItem().toString();
		Branch=((Spinner)findViewById(R.id.signup_Branch)).getSelectedItem().toString();
		Year=(( EditText)findViewById(R.id.signup_year)).getText().toString();

		//saving user data
		AppUserModel.MAIN_USER.setName(personName);
		AppUserModel.MAIN_USER.setEmail(email);
		AppUserModel.MAIN_USER.setImageResource(personPhotoUrl);
		AppUserModel.MAIN_USER.setisCoordinator(false);
		AppUserModel.MAIN_USER.setToken(token_recieved);
		AppUserModel.MAIN_USER.setRoll(RollNo+"");
		AppUserModel.MAIN_USER.setCollege(College);
		AppUserModel.MAIN_USER.setMobile(PhoneNumber);
		AppUserModel.MAIN_USER.setBranch(Branch+"");
		AppUserModel.MAIN_USER.setGender(Gender+"");
		AppUserModel.MAIN_USER.setYear(Year+"");

		AppUserModel.MAIN_USER.saveAppUser(SignUp.this);

		//saved now send it
		hideProgressDialog();

		send_user_details();

	}

	public void send_user_details(){

		showProgressDialog("Setting fire to the rain");

		StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.server_url)+
				getResources().getString(R.string.get_user_details_url),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String res) {

						//check response if good send to handler
						JSONObject response= null,status=null,data=null;
						String message;
						int code;
						try {
							response = new JSONObject(res);
							status=response.getJSONObject("status");
							data=response.getJSONObject("data");

							code=status.getInt("code");
							message=status.getString("message");

							if(code==200){
								Log.v(TAG,message);

								//user details updated on server
								signup_result(success);

							}else{
										//failure
								signup_result(failed);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						Toast.makeText(SignUp.this,res,Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(SignUp.this,error.toString(),Toast.LENGTH_LONG).show();
						hideProgressDialog();
					}
				}){
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("token",token_recieved);
				if(personName!=null)
				params.put("Name",personName+"");
				if(RollNo!=null)
				params.put("RollNo",RollNo+"");
				if(PhoneNumber!=null)
				params.put("PhoneNumber",PhoneNumber+"");
				if(Branch!=null)
				params.put("Branch",Branch+"");
				if(Year!=null)
				params.put("Year",Year+"");
				if(College!=null)
				params.put("College",College+"");
				if(Gender!=null)
				params.put("Gender",Gender+"");
				return params;
			}

		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);


	}

	public void signup_result(SignInStatus status){

		UserModel user=new UserModel();
		user.setName((( EditText)findViewById(R.id.signup_Name)).getText().toString());
		if(( (RadioButton) findViewById(R.id.signup_NitRadio) ).isChecked())
			user.setCollege("NIT Kurukshetra");
		else user.setCollege(((EditText)findViewById(R.id.signup_CollegeName)).getText().toString());
		user.setRoll((( EditText)findViewById(R.id.signup_Roll)).getText().toString());
		user.setMobile((( EditText)findViewById(R.id.signup_Number)).getText().toString());
		user.setGender(((Spinner)findViewById(R.id.signup_gender)).getSelectedItem().toString());
		user.setBranch(((Spinner)findViewById(R.id.signup_Branch)).getSelectedItem().toString());
		user.setYear((( EditText)findViewById(R.id.signup_year)).getText().toString());
		user.setEmail((( TextView)findViewById(R.id.signup_Email)).getText().toString());

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
	public void SignUp(View view)
	{

		if (Check() && !( (String) ( (Button) findViewById(R.id.signup_Email) ).getText() ).trim().equals(""))
		{
			save_and_send_user_details();
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


	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		// An unresolvable error has occurred and Google APIs (including Sign-In) will not
		// be available.
		Log.d(TAG, "onConnectionFailed:" + connectionResult);

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


}
