package com.nitkkr.gawds.tech16.Model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class AppUserModel extends CoordinatorModel
{
	private boolean Coordinator;
	private ArrayList<String> Interests;

	private ArrayList<String> getInterests(){return Interests;}

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

	public boolean saveUser(Context context) {
		SharedPreferences.Editor editor=context.getSharedPreferences("User_Data",Context.MODE_PRIVATE).edit();
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
	public void loadUser(Context context) {
		SharedPreferences preferences=context.getSharedPreferences("User_Data",Context.MODE_PRIVATE);
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
			loadUser(context);
			return true;
		}
		return false;
	}
	public boolean isUserLoaded(){return !getEmail().equals("");}
}
