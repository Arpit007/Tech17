package com.nitkkr.gawds.tech16.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.ResponseStatus;
import com.nitkkr.gawds.tech16.model.AppUserModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.src.CircularTextView;
import com.nitkkr.gawds.tech16.src.Typewriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
	Animation slideUp,slideDown;
	View upper, lower;
	Thread runAnimationUp = new Thread(){
		@Override
		public void run() {
			upper.startAnimation(slideUp);
		}
	};
	Thread runAnimationDown = new Thread(){
		@Override
		public void run() {
			lower.startAnimation(slideDown);
		}
	};

	private PopupMenu popup;
	private final int AVATAR=234;
	boolean Processing, Verified = false;
	private  String TAG="DEBUG";
	private static final int RC_SIGN_IN = 007;
	private GoogleApiClient mGoogleApiClient;
	private ProgressDialog mProgressDialog;
	private GoogleSignInOptions gso;
	String personName,personPhotoUrl,email,token_user,token_recieved,College,Gender,Branch,RollNo,PhoneNumber,Year;

	ResponseStatus success= ResponseStatus.SUCCESS;
	ResponseStatus failed= ResponseStatus.FAILED;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		if(!AppUserModel.MAIN_USER.isUserLoggedIn(this))
		{
			AppUserModel.MAIN_USER.setUseGoogleImage(false);
			AppUserModel.MAIN_USER.setImageId(0);
		}

		setImage();

		ActivityHelper.setStatusBarColor(this);

		( (RadioButton) findViewById(R.id.signup_NitRadio) ).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
			{
				if (checked)
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
		AppCompatSpinner spinner=(AppCompatSpinner) findViewById(R.id.signup_Branch);
		spinner.setAdapter(adapter);
		spinner.setSelection(0);

		String Gender[] = getResources().getStringArray(R.array.Gender);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_modified,R.id.branch_selected,Gender);
		spinner=(AppCompatSpinner) findViewById(R.id.signup_gender);
		spinner.setAdapter(adapter2);
		spinner.setSelection(0);


		String Year[] = getResources().getStringArray(R.array.Year);
		ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_modified,R.id.branch_selected,Year);
		spinner=(AppCompatSpinner) findViewById(R.id.signup_year);
		spinner.setAdapter(adapter3);
		spinner.setSelection(0);

		Typewriter signupLabel=(Typewriter)findViewById(R.id.signup_label);
		signupLabel.animateText("   Sign up");
		signupLabel.setCharacterDelay(60);

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

		(( EditText)findViewById(R.id.signup_Name)).addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				if(popup!=null)
				{
					if (charSequence.equals(""))
						popup.getMenu().getItem(2).setVisible(false);
					else popup.getMenu().getItem(2).setVisible(true);

					if(!AppUserModel.MAIN_USER.isUseGoogleImage() && AppUserModel.MAIN_USER.getImageId()==-1)
						setImage();
				}
			}

			@Override
			public void afterTextChanged(Editable editable)
			{
			}
		});

		findViewById(R.id.signup_user_Image_Button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				popup = new PopupMenu(SignUp.this, view);
				MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.image_menu, popup.getMenu());

				if(email.equals(""))
					popup.getMenu().getItem(0).setVisible(false);
				else popup.getMenu().getItem(0).setVisible(true);

				if (personName.equals(""))
					popup.getMenu().getItem(2).setVisible(false);
				else popup.getMenu().getItem(2).setVisible(true);

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem item)
					{
						switch (item.getItemId())
						{
							case R.id.google_Image:
								AppUserModel.MAIN_USER.setUseGoogleImage(true);
								setImage();
								break;

							case R.id.avatar:
								Intent intent=new Intent(SignUp.this,AvatarPicker.class);
								startActivityForResult(intent,AVATAR);
								break;

							case R.id.alphabet:
								AppUserModel.MAIN_USER.setUseGoogleImage(false);
								AppUserModel.MAIN_USER.setImageId(-1);
								setImage();
								break;
						}
						return false;
					}
				});
				popup.show();
			}
		});


		Button google_signIn_btn=(Button)findViewById(R.id.signup_Email);
		google_signIn_btn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(Check()){
							mGoogleApiClient = Login.mGoogleApiClient;
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
				}
		);

		token_recieved=AppUserModel.MAIN_USER.getToken();
		lower = findViewById(R.id.view2);
		upper = findViewById(R.id.view3);
		slideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
		slideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
		runAnimationUp.start();
		runAnimationDown.start();


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

		if(resultCode==RESULT_OK)
		{
			int ID = data.getIntExtra("ID", -1);
			AppUserModel.MAIN_USER.setImageId(ID);

			AppUserModel.MAIN_USER.setUseGoogleImage(( ID == -1 ));

			setImage();
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


			if(popup!=null)
			{
				if (email.equals(""))
					popup.getMenu().getItem(0).setVisible(false);
				else popup.getMenu().getItem(0).setVisible(true);

				if (personName.equals(""))
					popup.getMenu().getItem(2).setVisible(false);
				else popup.getMenu().getItem(2).setVisible(true);
			}
			sendToken();
		} else {
			// Signed out, show unauthenticated UI.
			signup_result(failed);
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

							isNew=data.getBoolean("IsNew");
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
									AppUserModel.MAIN_USER.setLoggedIn(false,getBaseContext());
									AppUserModel.MAIN_USER.setSignedup(true,getBaseContext());
									Toast.makeText(SignUp.this,"Already signed up!!, please login",Toast.LENGTH_LONG).show();
									startActivity(new Intent(SignUp.this,Login.class));
								}
							}else{
								//failure
								signup_result(failed);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}


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


	private void setImage()
	{
		if(AppUserModel.MAIN_USER.getImageResource()!=null && AppUserModel.MAIN_USER.isUseGoogleImage())
		{
			CircleImageView view=(CircleImageView)findViewById(R.id.signup_user_Image);
			view.setVisibility(View.VISIBLE);

			Glide.with(SignUp.this).load(AppUserModel.MAIN_USER.getImageResource()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f).centerCrop().into(view);

			findViewById(R.id.signup_user_Image_Letter).setVisibility(View.INVISIBLE);
		}
		else if(AppUserModel.MAIN_USER.getImageId()!=-1)
		{
			CircleImageView view=(CircleImageView)findViewById(R.id.signup_user_Image);
			view.setVisibility(View.VISIBLE);

			TypedArray array=getResources().obtainTypedArray(R.array.Avatar);
			view.setImageResource(array.getResourceId(AppUserModel.MAIN_USER.getImageId(),0));
			array.recycle();

			CircularTextView circularTextView=(CircularTextView)findViewById(R.id.signup_user_Image_Letter);
			circularTextView.setVisibility(View.VISIBLE);
			circularTextView.setText("");
			circularTextView.setFillColor(ContextCompat.getColor(this,R.color.User_Image_Fill_Color));
		}
		else
		{
			CircularTextView view=(CircularTextView)findViewById(R.id.signup_user_Image_Letter);

			view.setText(AppUserModel.MAIN_USER.getName().toUpperCase().charAt(0));
			view.setVisibility(View.VISIBLE);

			TypedArray array=getResources().obtainTypedArray(R.array.Flat_Colors);

			int colorPos=(AppUserModel.MAIN_USER.getName().toLowerCase().charAt(0)-'a')%array.length();

			view.setFillColor(array.getColor(colorPos,0));
			view.setBorderColor(ContextCompat.getColor(this,R.color.text_color_primary));

			array.recycle();

			findViewById(R.id.signup_user_Image).setVisibility(View.INVISIBLE);
		}
	}

	boolean Check()
	{
		EditText signup_name=(EditText) findViewById(R.id.signup_Name);
		EditText signup_number=( (EditText) findViewById(R.id.signup_Number) );
		EditText signup_Roll=( (EditText) findViewById(R.id.signup_Roll) );
		AppCompatSpinner signup_year=( (AppCompatSpinner) findViewById(R.id.signup_year) );

		if (( signup_name ).getText().toString().trim().equals(""))
		{
			signup_name.setError("required");
			return false;
		}

		if (( (RadioButton) findViewById(R.id.signup_OtherRadio) ).isChecked() && ( (EditText) ( findViewById(R.id.signup_CollegeName) ) ).getText().toString().trim().equals(""))
		{
			return false;
		}
		if ( signup_number.getText().toString().trim().equals("") )
		{
			signup_number.setError("required");
			return false;
		}
		if(( (TextView) findViewById(R.id.signup_Number) ).getText().length() < 10){
			signup_number.setError("Not valid");
			return false;
		}
		if ( signup_Roll.getText().toString().trim().equals("") )
		{
			signup_Roll.setError("required");
			return false;
		}

		if ( signup_year.getSelectedItem().toString().trim().equals("") )
		{
			signup_year.setPrompt("required");
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
		Year=(( AppCompatSpinner)findViewById(R.id.signup_year)).getSelectedItem().toString();

		//saving user data
		AppUserModel.MAIN_USER.setName(personName);
		AppUserModel.MAIN_USER.setEmail(email);
		AppUserModel.MAIN_USER.setImageResource(personPhotoUrl);
		AppUserModel.MAIN_USER.setToken(token_recieved);
		AppUserModel.MAIN_USER.setRoll(RollNo);
		AppUserModel.MAIN_USER.setCollege(College);
		AppUserModel.MAIN_USER.setMobile(PhoneNumber);
		AppUserModel.MAIN_USER.setBranch(Branch);
		AppUserModel.MAIN_USER.setGender(Gender);
		AppUserModel.MAIN_USER.setYear(Year);

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
				params.put("name",personName);
				params.put("rollNo",RollNo);
				params.put("phoneNo",PhoneNumber);
				params.put("branch",Branch);
				params.put("year",Year);
				params.put("college",College);
				params.put("gender",Gender);
				return params;
			}

		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);


	}

	public void signup_result(ResponseStatus status){


		switch (status)
			{
				case FAILED:
					AppUserModel.MAIN_USER.setSignedup(false,getBaseContext());
					Toast.makeText(this,"Failed to Sign Up, Please Try Again",Toast.LENGTH_LONG).show();
					break;
				case SUCCESS:

					AppUserModel.MAIN_USER.setSignedup(true,getBaseContext());
					AppUserModel.MAIN_USER.setLoggedIn(true,getBaseContext());

					Intent intent=new Intent(SignUp.this, Interests.class);
					Bundle bundle=new Bundle();
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
			if (mProgressDialog!=null && mProgressDialog.isShowing())
				return;

			AppUserModel.MAIN_USER.loadAppUser(getApplicationContext());

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
			mProgressDialog.setCancelable(false);

		}

		mProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.hide();
		}
	}



}
