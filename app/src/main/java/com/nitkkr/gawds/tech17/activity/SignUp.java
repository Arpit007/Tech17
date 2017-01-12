package com.nitkkr.gawds.tech17.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SignUpEvent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.model.AppUserModel;
import com.nitkkr.gawds.tech17.src.CircularTextView;
import com.nitkkr.gawds.tech17.src.CompatCircleImageView;
import com.nitkkr.gawds.tech17.src.Typewriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
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

	private boolean exit;
	private final int AVATAR = 234;
	private final int RESULT = 654;
	private String TAG = "DEBUG";
	private static final int RC_SIGN_IN = 678;
	private GoogleApiClient mGoogleApiClient;
	private ProgressDialog mProgressDialog;
	private GoogleSignInOptions gso;
	private boolean googleSigningTask=false;

	AppUserModel userModel = (AppUserModel) AppUserModel.MAIN_USER.clone();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		ActivityHelper.setCreateAnimation(this);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		userModel.setUseGoogleImage(false);
		userModel.setImageId(0);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			ActivityHelper.setStatusBarColor(this);
		}

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

		ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_modified, R.id.branch_selected, getResources().getStringArray(R.array.Branches));
		AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.signup_Branch);
		spinner.setAdapter(adapter);
		spinner.setSelection(0);

		ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_modified, R.id.branch_selected, getResources().getStringArray(R.array.Gender));
		spinner = (AppCompatSpinner) findViewById(R.id.signup_gender);
		spinner.setAdapter(adapter2);
		spinner.setSelection(0);

		ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_modified, R.id.branch_selected, getResources().getStringArray(R.array.Year));
		spinner = (AppCompatSpinner) findViewById(R.id.signup_year);
		spinner.setAdapter(adapter3);
		spinner.setSelection(0);

		Typewriter signupLabel = (Typewriter) findViewById(R.id.signup_label);
		signupLabel.animateText("   Sign up");
		signupLabel.setCharacterDelay(60);

		( (EditText) findViewById(R.id.signup_Name) ).addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				if (!userModel.isUseGoogleImage() && userModel.getImageId() == -1)
				{
					setImage();
				}
			}

			@Override
			public void afterTextChanged(Editable editable)
			{
			}
		});

		if (userModel.isUserLoggedIn(this))
		{
			( (EditText) findViewById(R.id.signup_Name) ).setText(userModel.getName());
			Button button = ( (Button) findViewById(R.id.signup_Email) );
			button.setText(userModel.getEmail());
			button.setEnabled(false);
		}

		setImage();

		findViewById(R.id.signup_user_Image_Button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				PopupMenu popup = new PopupMenu(SignUp.this, view);
				MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.image_menu, popup.getMenu());

				if (userModel.getEmail().equals(""))
				{
					popup.getMenu().getItem(0).setVisible(false);
				}
				else
				{
					popup.getMenu().getItem(0).setVisible(true);
				}

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem item)
					{
						switch (item.getItemId())
						{
							case R.id.google_Image:
								userModel.setUseGoogleImage(true);
								userModel.setImageId(-1);
								setImage();
								break;

							case R.id.avatar:
								Intent intent = new Intent(SignUp.this, AvatarPicker.class);
								startActivityForResult(intent, AVATAR);
								break;

							case R.id.alphabet:
								userModel.setUseGoogleImage(false);
								userModel.setImageId(-1);
								setImage();
								break;
						}
						return false;
					}
				});
				popup.show();
			}
		});

		findViewById(R.id.signup_Email).setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						signIn();
					}
				}
		);

		gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.requestIdToken(Login.client_server_id)
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(SignUp.this)
				.enableAutoManage(SignUp.this, SignUp.this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		runAnimationUp.start();
		runAnimationDown.start();
	}

	private void signIn()
	{
		googleSigningTask=true;
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==RESULT && resultCode==RESULT_OK)
		{
			if(data.getBooleanExtra("Logged_In",true))
			{
				Intent intent = new Intent();
				intent.putExtra("Logged_In", true);
				setResult(RESULT_OK, intent);
				finish();
				ActivityHelper.setExitAnimation(this);
			}
		}

		if (requestCode == RC_SIGN_IN)
		{
			googleSigningTask=false;
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
		if (resultCode == RESULT_OK)
		{
			if (requestCode == AVATAR)
			{
				int ID = data.getIntExtra("ID", -1);

				userModel.setImageId(ID);
				if (ID != -1)
				{
					userModel.setUseGoogleImage(false);
				}

				setImage();
			}
		}
	}

	private void handleSignInResult(GoogleSignInResult result)
	{
		Log.d(TAG, "handleSignInResult:" + result.isSuccess());
		if (result.isSuccess())
		{
			GoogleSignInAccount acct = result.getSignInAccount();

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

				( (EditText) findViewById(R.id.signup_Name) ).setText(userModel.getName());
				( (Button) findViewById(R.id.signup_Email) ).setText(userModel.getEmail());
				findViewById(R.id.signup_Email).setEnabled(false);
			}
			sendToken();
		}
		else
		{
			signUpResult(ResponseStatus.FAILED);
		}
	}

	public void sendToken()
	{
		showProgressDialog("Verifying");

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
							userModel.setToken(data.getString("token"));

							if (code == 200)
							{
								Log.v(TAG, response.getJSONObject("status").getString("message"));

								if (!isNew)
								{
									Toast.makeText(SignUp.this, "Already signed up!!\n please login", Toast.LENGTH_LONG).show();
									startActivity(new Intent(SignUp.this, Login.class));
									ActivityHelper.setExitAnimation(SignUp.this);
									finish();
								}
								else
								{
									hideProgressDialog();
								}
							}
							else
							{
								signUpResult(ResponseStatus.FAILED);
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							signUpResult(ResponseStatus.FAILED);
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						signUpResult(ResponseStatus.FAILED);
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

	private void setImage()
	{
		if (userModel.getImageResource() != null && userModel.isUseGoogleImage())
		{
			CompatCircleImageView view = (CompatCircleImageView) findViewById(R.id.signup_user_Image);
			view.setVisibility(View.VISIBLE);

			Glide.with(SignUp.this).load(userModel.getImageResource()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f).centerCrop().into(view);

			findViewById(R.id.signup_user_Image_Letter).setVisibility(View.INVISIBLE);
			findViewById(R.id.temp_user_Image_Letter).setVisibility(View.INVISIBLE);
		}
		else if (userModel.getImageId() != -1)
		{
			CompatCircleImageView view = (CompatCircleImageView) findViewById(R.id.signup_user_Image);
			view.setVisibility(View.VISIBLE);

			TypedArray array = getResources().obtainTypedArray(R.array.Avatar);
			view.setImageResource(array.getResourceId(userModel.getImageId(), 0));
			array.recycle();

			CircularTextView circularTextView = (CircularTextView) findViewById(R.id.signup_user_Image_Letter);
			circularTextView.setVisibility(View.INVISIBLE);
			circularTextView = (CircularTextView) findViewById(R.id.temp_user_Image_Letter);
			circularTextView.setVisibility(View.VISIBLE);
			circularTextView.setFillColor(ContextCompat.getColor(this, R.color.User_Image_Fill_Color));
		}
		else
		{
			CircularTextView view = (CircularTextView) findViewById(R.id.signup_user_Image_Letter);

			String Text = ( (EditText) findViewById(R.id.signup_Name) ).getText().toString().trim();

			if (Text.isEmpty())
			{
				view.setText("#");
			}
			else
			{
				view.setText(String.valueOf(Text.toUpperCase().charAt(0)));
			}

			view.setVisibility(View.VISIBLE);

			TypedArray array = getResources().obtainTypedArray(R.array.Flat_Colors);

			int colorPos;

			if (Text.isEmpty())
			{
				colorPos = Math.abs(( '#' - 'a' )) % array.length();
			}
			else
			{
				colorPos = Math.abs(( Text.toLowerCase().charAt(0) - 'a' )) % array.length();
			}

			view.setFillColor(array.getColor(colorPos, 0));
			view.setBorderWidth(2);
			view.setBorderColor(ContextCompat.getColor(this, R.color.text_color_primary));

			array.recycle();

			findViewById(R.id.signup_user_Image).setVisibility(View.INVISIBLE);
			findViewById(R.id.temp_user_Image_Letter).setVisibility(View.INVISIBLE);
		}
	}

	boolean Check()
	{
		EditText signup_name = (EditText) findViewById(R.id.signup_Name);
		EditText signup_number = ( (EditText) findViewById(R.id.signup_Number) );
		EditText signup_Roll = ( (EditText) findViewById(R.id.signup_Roll) );
		AppCompatSpinner signup_year = ( (AppCompatSpinner) findViewById(R.id.signup_year) );

		String Name = ( signup_name ).getText().toString().trim();
		if (Name.equals(""))
		{
			signup_name.setError("Required");
			return false;
		}

		if (Name.length() < 3)
		{
			signup_name.setError("Invalid");
			return false;
		}

		if (( (RadioButton) findViewById(R.id.signup_OtherRadio) ).isChecked())
		{
			String Clg = ( (EditText) ( findViewById(R.id.signup_CollegeName) ) ).getText().toString();
			if (Clg.equals(""))
			{
				( (EditText) ( findViewById(R.id.signup_CollegeName) ) ).setError("Required");
				return false;
			}
			if (Clg.length() < 3)
			{
				( (EditText) ( findViewById(R.id.signup_CollegeName) ) ).setError("Invalid");
				return false;
			}
		}
		if (signup_number.getText().toString().trim().equals(""))
		{
			signup_number.setError("required");
			return false;
		}
		if (( (TextView) findViewById(R.id.signup_Number) ).getText().length() < 10)
		{
			signup_number.setError("Invalid");
			return false;
		}

		String roll = signup_Roll.getText().toString().trim();

		if (roll.equals(""))
		{
			signup_Roll.setError("Required");
			return false;
		}

		if (roll.length() < 4)
		{
			signup_Roll.setError("Invalid");
			return false;
		}
		if (signup_year.getSelectedItem().toString().trim().equals(""))
		{
			signup_year.setPrompt("required");
			return false;
		}

		if (userModel.getEmail().equals(""))
		{
			Snackbar.make(findViewById(android.R.id.content), "Please Select Email", Snackbar.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	public void saveAndSendUserDetails()
	{
		showProgressDialog("Signing Up...");

		if (( (RadioButton) findViewById(R.id.signup_NitRadio) ).isChecked())
		{
			userModel.setCollege("NIT Kurukshetra");
		}
		else
		{
			userModel.setCollege(( (EditText) findViewById(R.id.signup_CollegeName) ).getText().toString());
		}

		userModel.setRoll(( (EditText) findViewById(R.id.signup_Roll) ).getText().toString());
		userModel.setMobile(( (EditText) findViewById(R.id.signup_Number) ).getText().toString());
		userModel.setGender(( (Spinner) findViewById(R.id.signup_gender) ).getSelectedItem().toString());
		userModel.setBranch(( (Spinner) findViewById(R.id.signup_Branch) ).getSelectedItem().toString());
		userModel.setYear(( (AppCompatSpinner) findViewById(R.id.signup_year) ).getSelectedItem().toString());
		sendUserDetails();
	}

	public void sendUserDetails()
	{
		StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.server_url) +
				getResources().getString(R.string.get_user_details_url),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response;
						String message;
						int code;
						try
						{
							response = new JSONObject(res);

							code = response.getJSONObject("status").getInt("code");
							message = response.getJSONObject("status").getString("message");

							if (code == 200)
							{
								Log.v(TAG, message);
								signUpResult(ResponseStatus.SUCCESS);
							}
							else
							{
								signUpResult(ResponseStatus.FAILED);
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							signUpResult(ResponseStatus.FAILED);
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						signUpResult(ResponseStatus.FAILED);
					}
				})
		{
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String> params = new HashMap<>();
				params.put("token", userModel.getToken());
				params.put("name", userModel.getName());
				params.put("rollNo", userModel.getRoll());
				params.put("phoneNo", userModel.getMobile());
				params.put("branch", userModel.getBranch());
				params.put("year", userModel.getYear());
				params.put("college", userModel.getCollege());
				params.put("gender", userModel.getGender());
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}

	public void signUpResult(ResponseStatus status)
	{
		hideProgressDialog();

		switch (status)
		{
			case FAILED:
				AppUserModel.MAIN_USER.setSignedup(false, getBaseContext());
				Toast.makeText(this, "Failed to Sign Up, Please Try Again", Toast.LENGTH_LONG).show();
				break;

			case SUCCESS:
				AppUserModel.MAIN_USER = userModel;
				AppUserModel.MAIN_USER.setSignedup(true, getBaseContext());
				AppUserModel.MAIN_USER.setLoggedIn(true, getBaseContext());
				AppUserModel.MAIN_USER.saveAppUser(this);

				if(!ActivityHelper.isDebugMode(this))
					Answers.getInstance().logSignUp(new SignUpEvent()
							.putMethod("Google: " + AppUserModel.MAIN_USER.getName())
							.putSuccess(true));

				getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE).edit().putBoolean("Skip", false).commit();

				Intent intent = new Intent(SignUp.this, Interests.class);
				intent.putExtra("Start_Home",getIntent().getBooleanExtra("Start_Home", true));
				startActivityForResult(intent,RESULT);
				break;
			default:
				Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	public void SignUpButton(View view)
	{
		if (Check() && !userModel.getEmail().equals(""))
		{
			saveAndSendUserDetails();
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
		if ((mProgressDialog != null && mProgressDialog.isShowing())|| googleSigningTask)
		{
			return;
		}

		if (AppUserModel.MAIN_USER.isUserLoggedIn(this) && !AppUserModel.MAIN_USER.isUserSignedUp(this) && Login.mGoogleApiClient!=null)
		{
			try
			{
				Login.mGoogleApiClient.connect();
				Login.mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks()
				{
					@Override
					public void onConnected(Bundle bundle)
					{

						if (Login.mGoogleApiClient.isConnected())
						{
							Auth.GoogleSignInApi.signOut(Login.mGoogleApiClient).setResultCallback(new ResultCallback<Status>()
							{
								@Override
								public void onResult(@NonNull Status status)
								{
									if (status.isSuccess())
									{
										AppUserModel.MAIN_USER.setSignedup(false, SignUp.this);
									}
								}
							});
						}
					}

					@Override
					public void onConnectionSuspended(int i)
					{
						Log.d("DEBUG", "Google API Client Connection Suspended");
					}
				});
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if (isTaskRoot())
		{
			startActivity(new Intent(SignUp.this, Login.class));
		}
		else
		{
			super.onBackPressed();
			finish();
		}
		ActivityHelper.setExitAnimation(SignUp.this);
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{
		Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
		Log.d("DEBUG", "onConnectionFailed:" + connectionResult);
	}

	private void showProgressDialog(String msg)
	{
		if (mProgressDialog == null)
		{
			mProgressDialog = new ProgressDialog(this);
		}
		mProgressDialog.setMessage(msg);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);

		mProgressDialog.show();
	}

	private void hideProgressDialog()
	{
		if (mProgressDialog != null && mProgressDialog.isShowing())
		{
			mProgressDialog.dismiss();
		}
	}
}
