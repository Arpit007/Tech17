package com.nitkkr.gawds.tech16.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nitkkr.gawds.tech16.Activity.Login;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class AppUserModel extends UserModel
{
	private boolean Coordinator;
	private ArrayList<String> Interests;
	public static final int LOGIN_REQUEST_CODE=99;
	private boolean loggedIn,signedup;
	public ArrayList<String> getInterests(){return Interests;}

	public static AppUserModel MAIN_USER=new AppUserModel();

	public boolean isCoordinator(){return Coordinator;}
	public void setisCoordinator(boolean coordinator){Coordinator=coordinator;}

	public String interestsToString() {
		//TODO: Add Tokens Accordingly
		StringBuilder stringBuilder=new StringBuilder("");
		if(Interests!=null){
		for(String interest:Interests)
		{
			if(stringBuilder.toString().equals(""))
				stringBuilder.append(interest);
			else
			{
				stringBuilder.append(",").append(interest);
			}
		}

		return stringBuilder.toString();}
		else{
			return "";
		}
	}
	private ArrayList<String> stringToInterests(String Interests) {
		String[] strings=Interests.split(",");
		ArrayList<String> list = new ArrayList<>(strings.length);
		for(String x : strings)
			list.add(x);
		return list;
	}
	public void setInterests(String interests){Interests=stringToInterests(interests);}

	public void setInterests_arraylist(ArrayList<String> s){
		this.Interests=s;
	}

	public boolean saveAppUser(Context context) {
		return saveAppUser(context, "User_Data");
	}
	private boolean saveAppUser(Context context, String File) {
		SharedPreferences.Editor editor=context.getSharedPreferences(File,Context.MODE_PRIVATE).edit();
		editor.putString("Name",getName());
		editor.putString("Token",getToken());
		editor.putString("Email",getEmail());
		editor.putString("Roll",getRoll());
		editor.putString("College",getCollege());
		editor.putString("Mobile",getMobile());
		editor.putString("Branch",getBranch());
		editor.putBoolean("Coordinator",isCoordinator());
		editor.putString("ImageId",getImageResource());
		editor.putString("Gender",getGender());
		editor.putString("Interests",interestsToString());
		editor.putBoolean("GoogleImage",isUseGoogleImage());
		editor.putInt("ImageDrawableID",getImageId());
		return editor.commit();
	}

	public void loadAppUser(Context context) {
		loadUser(context,"User_Data");
	}
	private void loadUser(Context context, String File) {
		SharedPreferences preferences=context.getSharedPreferences(File,Context.MODE_PRIVATE);
		setName(preferences.getString("Name",""));
		setEmail(preferences.getString("Email",""));
		setRoll(preferences.getString("Roll",""));
		setCollege(preferences.getString("College",""));
		setMobile(preferences.getString("Mobile",""));
		setBranch(preferences.getString("Branch",""));
		setImageResource(preferences.getString("ImageId",null));
		setisCoordinator(preferences.getBoolean("Coordinator",false));
		setToken(preferences.getString("Token",""));
		setGender(preferences.getString("Gender",""));
		Interests=stringToInterests(preferences.getString("Interests",""));
		setUseGoogleImage(preferences.getBoolean("GoogleImage",true));
		setImageId(preferences.getInt("ImageDrawableID",-1));
	}

	public void logoutUser(Context context) {
		SharedPreferences.Editor editor=context.getSharedPreferences("User_Data",Context.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
		loadAppUser(context);
		saveAppUser(context);
	}
	public boolean isUserLoaded(){

		if(getEmail()!=null)
		return !getEmail().equals("");
		else
			return false;
	}
	public void LoginUserHome(Activity activity, boolean Result)
	{
		if(Result)
			activity.startActivityForResult(new Intent(activity, Login.class),LOGIN_REQUEST_CODE);
		else activity.startActivity(new Intent(activity,Login.class));
	}

	public boolean isUserLoggedIn(Context context){
		SharedPreferences sp=context.getSharedPreferences("authenticate",Context.MODE_PRIVATE);
		loggedIn=sp.getBoolean("loggedIn",false);

		if(loggedIn)
			return true;
		else
			return false;
	}

	public void setLoggedIn(boolean val,Context context){
		SharedPreferences.Editor editor=context.getSharedPreferences("authenticate",Context.MODE_PRIVATE).edit();
		editor.putBoolean("loggedIn",val);
		editor.commit();
	}

	public boolean isUserSignedUp(Context context){
		SharedPreferences sp=context.getSharedPreferences("authenticate",Context.MODE_PRIVATE);
		signedup=sp.getBoolean("signedup",false);
		if(signedup)
			return true;
		else
			return false;
	}
	public void setSignedup(boolean val,Context context){
		SharedPreferences.Editor editor=context.getSharedPreferences("authenticate",Context.MODE_PRIVATE).edit();
		editor.putBoolean("signedup",val);
		editor.commit();
	}

	public void LoginUserNoHome(Activity activity, boolean Result)
	{
		Intent intent=new Intent(activity,Login.class);
		intent.putExtra("Start_Home",false);
		if(Result)
			activity.startActivityForResult(intent,LOGIN_REQUEST_CODE);
		else activity.startActivity(intent);
	}
}
