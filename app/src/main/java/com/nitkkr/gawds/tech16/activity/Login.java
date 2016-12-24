package com.nitkkr.gawds.tech16.activity;

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
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.nitkkr.gawds.tech16.api.FetchData;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.ResponseStatus;
import com.nitkkr.gawds.tech16.model.AppUserModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.src.Typewriter;

import android.view.animation.AnimationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity  implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener
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
    boolean signingIn = false;
    boolean exit=false;
    private static final int RC_SIGN_IN = 007;
    private  String TAG="DEBUG";
    static public GoogleApiClient mGoogleApiClient;
    static public GoogleSignInOptions gso;
    private ProgressDialog mProgressDialog;
    private Button btnSignIn;
    String personName,personPhotoUrl,email,token_user,token_recieved;
    ResponseStatus success= ResponseStatus.SUCCESS;
    ResponseStatus failed= ResponseStatus.FAILED;
    private String client_server_id="726783559264-o574f9bvum7qdnlusrdmh0rnshqfnr8h.apps.googleusercontent.com";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityHelper.setCreateAnimation(this);
        ActivityHelper.setStatusBarColor(this);

        Typewriter login_type=(Typewriter)findViewById(R.id.signup_label);
        login_type.animateText("    Login");
        login_type.setCharacterDelay(80);
        btnSignIn = (Button) findViewById(R.id.login_Gmail);
        btnSignIn.setOnClickListener(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(client_server_id)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        RelativeLayout rl=(RelativeLayout) findViewById(R.id.activity_login);


//        Bitmap bk=App.decodeSampledBitmapFromResource(getResources(),R.drawable.login_bk3,300,300);
//        Drawable bd=new BitmapDrawable(getResources(),bk);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            rl.setBackground(bd);
//        }else{
//            rl.setBackgroundDrawable(bd);
//        }

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

            //Log.v(TAG, "display name: " + acct.getDisplayName());

            if(acct!=null)
            {
                personName = acct.getDisplayName();
                personPhotoUrl = acct.getPhotoUrl().toString();
                email = acct.getEmail();
                token_user = acct.getIdToken();
            }
//            Log.e(TAG, "Name: " + personName + ", email: " + email
//                    + ", Image: " + personPhotoUrl+" token :"+token_user);

            sendToken();
        } else {
            // Signed out, show unauthenticated UI.
            SignIn(failed);
        }
    }


    public void sendToken(){
        showProgressDialog("Verifying");
        Log.v("login","Sending Token");
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
                            token_recieved=data.getString("token");

                            code=status.getInt("code");
                            message=status.getString("message");

                            isNew=data.getBoolean("IsNew");

                            //save this token for further use
                            if(code==200){
                                Log.v("login",response.toString());

                                //success
                                AppUserModel.MAIN_USER.setName(personName);
                                AppUserModel.MAIN_USER.setEmail(email);
                                AppUserModel.MAIN_USER.setImageResource(personPhotoUrl);
                                AppUserModel.MAIN_USER.setToken(token_recieved);
                                AppUserModel.MAIN_USER.saveAppUser(Login.this);


                                if(isNew){

                                    ResponseStatus sign_up= ResponseStatus.SIGNUP;
                                    SignIn(sign_up);

                                }else{
                                //get things first
                                    AppUserModel.MAIN_USER.setSignedup(true,getBaseContext());

                                    //SignIn(success);
                                    fetch_user_details();
                                }
                            }else{
                                //failure
                                SignIn(failed);
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
                        Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("idToken",token_user);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void fetch_user_details(){

        showProgressDialog("Locating you in our beacon..");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.server_url)+
                getResources().getString(R.string.get_user_details_url)+"?token="+token_recieved,
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
                                //Log.v(TAG,message);

                                RollNo= String.valueOf(data.getInt("RollNo"));
                                PhoneNumber=data.getString("PhoneNumber");
                                College=data.getString("College");
                                Branch=data.getString("Branch");
                                Gender=data.getString("Gender");
                                Year=data.getString("Year");
                                //saving user data
                                AppUserModel.MAIN_USER.setRoll(RollNo);
                                AppUserModel.MAIN_USER.setCollege(College);
                                AppUserModel.MAIN_USER.setMobile(PhoneNumber);
                                AppUserModel.MAIN_USER.setBranch(Branch);
                                AppUserModel.MAIN_USER.setGender(Gender);
                                AppUserModel.MAIN_USER.setYear(Year);

                                AppUserModel.MAIN_USER.saveAppUser(Login.this);
                                Log.v("login","Fetched");

                                //now fetch interests
                                fetch_interests();

                            }else{
                                //failure
                                SignIn(failed);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                      //  Toast.makeText(Login.this,res,Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void fetch_interests(){
        showProgressDialog("Optimising your feed...");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.server_url)+
                getResources().getString(R.string.get_user_interests_url)+"?token="+token_recieved,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        //check response if good send to handler
                        JSONObject response= null,status=null;
                        JSONArray data;
                        String message,RollNo,PhoneNumber,Branch,Year,College,Gender;
                        ArrayList<String> interests=new ArrayList<>();
                        int code;
                        try {
                            response = new JSONObject(res);
                            status=response.getJSONObject("status");
                            data=response.getJSONArray("data");

                            code=status.getInt("code");
                            message=status.getString("message");

                            if(code==200){
                                //Log.v(TAG,message);
                                for(int i=0;i<data.length();i++){
                                    interests.add(data.getString(i));
                                }
                                AppUserModel.MAIN_USER.setInterestsFromArray(interests);
                                AppUserModel.MAIN_USER.saveAppUser(getApplicationContext());
                                //everything is fetched and saved now
                                //so intent to home
                                Log.v("login","interests fetched");

                                SignIn(success);
                            }else{
                                //failure
                                SignIn(failed);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                      //  Toast.makeText(Login.this,res,Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public void SignIn(ResponseStatus status)
    {

        signingIn = true;

        switch (status)
        {
            case FAILED:
                AppUserModel.MAIN_USER.setLoggedIn(false,getBaseContext());
                Toast.makeText(getBaseContext(), "Failed to LogIn", Toast.LENGTH_LONG).show();
                break;
            case SUCCESS:

                AppUserModel.MAIN_USER.setLoggedIn(true,getBaseContext());
                Toast.makeText(getBaseContext(), "SignIn Successful", Toast.LENGTH_SHORT).show();

                FetchData.getInstance().fetchUserInterests(getApplicationContext());
                FetchData.getInstance().fetchUserWishlist(getApplicationContext());

                if(!ActivityHelper.isDebugMode(getApplicationContext()))
                {
                    Crashlytics.setUserName(AppUserModel.MAIN_USER.getName());
                    Crashlytics.setUserEmail(AppUserModel.MAIN_USER.getEmail());
                }
                Log.v("login","Successful login");

                if(getIntent().getBooleanExtra("Start_Home",true))
                    startActivity(new Intent(Login.this, Home.class));
                else
                {
                    Intent intent=new Intent();
                    intent.putExtra("Logged_In",true);
                    setResult(RESULT_OK,intent);
                }

                finish();
                ActivityHelper.setExitAnimation(this);
                break;
            case SIGNUP:
                AppUserModel.MAIN_USER.setSignedup(false,getBaseContext());
                Intent intent=new Intent(Login.this, SignUp.class);
                intent.putExtra("Start_Home",getIntent().getBooleanExtra("Start_Home",true));
                startActivity(intent);

                finish();
                ActivityHelper.setExitAnimation(this);
                break;
            default:
                break;
        }
    }
     private void signOut() {
        Log.v("Debug","Logged out");

        if(mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Log.v("Debug","Logged out");
                        }
                    });
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        if(AppUserModel.MAIN_USER.isUserLoggedIn(getBaseContext())){

            if(!AppUserModel.MAIN_USER.isUserSignedUp(getBaseContext())){
                SignIn(ResponseStatus.SIGNUP);
            }else{
                startActivity(new Intent(Login.this,Home.class));
            }

        }
        else{
            //show login screen
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
        ActivityHelper.setExitAnimation(this);
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
            if (mProgressDialog!=null && mProgressDialog.isShowing())
                return;

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