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

public class AppUserModel extends CoordinatorModel
{
	private boolean Coordinator;
	private ArrayList<String> Interests;

	public static final int LOGIN_REQUEST_CODE=99;

	public ArrayList<String> getInterests(){return Interests;}

	public static AppUserModel MAIN_USER=new AppUserModel();

	public boolean isCoordinator(){return Coordinator;}
	public void setisCoordinator(boolean coordinator){Coordinator=coordinator;}

	private String interestsToString() {
		StringBuilder stringBuilder=new StringBuilder("");
		for(String interest:Interests)
		{
			if(stringBuilder.toString().equals(""))
				stringBuilder.append(interest);
			else
			{
				stringBuilder.append(",").append(interest);
			}
		}
		return stringBuilder.toString();
	}
	private ArrayList<String> stringToInterests(String Interests) {
		String[] strings=Interests.split(",");
		ArrayList<String> list = new ArrayList<>(strings.length);
		for(String x : strings)
			list.add(x);
		return list;
	}
	public void setInterests(String interests){Interests=stringToInterests(interests);}

	public boolean saveAppUser(Context context) {
		return saveAppUser(context, "User_Data");
	}
	public boolean saveTempUser(Context context) {
		return saveAppUser(context, "Temp_User");
	}
	private boolean saveAppUser(Context context, String File) {
		SharedPreferences.Editor editor=context.getSharedPreferences(File,Context.MODE_PRIVATE).edit();
		editor.putString("Name",getName());
		editor.putString("Email",getEmail());
		editor.putString("Roll",getRoll());
		editor.putString("College",getCollege());
		editor.putString("Mobile",getMobile());
		editor.putString("Branch",getBranch());
		editor.putBoolean("Coordinator",isCoordinator());
		editor.putString("Interests",interestsToString());
		if(isCoordinator())
			editor.putString("Designation",getDesignation());
		return editor.commit();
	}

	public void loadAppUser(Context context) {
		loadUser(context,"User_Data");
	}
	public void loadTempUser(Context context) {
		loadUser(context,"Temp_Data");
	}
	private void loadUser(Context context, String File) {
		SharedPreferences preferences=context.getSharedPreferences(File,Context.MODE_PRIVATE);
		setName(preferences.getString("Name",""));
		setEmail(preferences.getString("Email",""));
		setRoll(preferences.getString("Roll",""));
		setCollege(preferences.getString("College",""));
		setMobile(preferences.getString("Mobile",""));
		setBranch(preferences.getString("Branch",""));
		setisCoordinator(preferences.getBoolean("Coordinator",false));
		Interests=stringToInterests(preferences.getString("Interests",""));
		if(isCoordinator())
			setDesignation(preferences.getString("Designation",""));
	}

	public boolean logoutUser(Context context) {
		SharedPreferences.Editor editor=context.getSharedPreferences("User_Data",Context.MODE_PRIVATE).edit();
		editor.clear();
		if(editor.commit())
		{
			loadAppUser(context);
			saveAppUser(context);
			return true;
		}
		return false;
	}
	public boolean isUserLoaded(){
		return !getEmail().equals("");
	}

	public void LoginUser(Activity activity, boolean Result)
	{
		if(Result)
			activity.startActivityForResult(new Intent(activity, Login.class),LOGIN_REQUEST_CODE);
		else activity.startActivity(new Intent(activity,Login.class));
	}
}
