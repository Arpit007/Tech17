package com.nitkkr.gawds.tech16.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nitkkr.gawds.tech16.Activity.Login;
import com.nitkkr.gawds.tech16.Database.Database;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class AppUserModel extends UserModel implements Cloneable
{

	ArrayList<InterestModel> Interests;

	public static final int LOGIN_REQUEST_CODE=99;
	private boolean loggedIn,signedup;

	public ArrayList<InterestModel> getInterests(){return Interests;}
	public ArrayList<String> getSelectedInterests()
	{
		ArrayList<String> Keys=new ArrayList<>();
		for(InterestModel interestModel: Interests)
		{
			if(interestModel.isSelected())
				Keys.add(interestModel.getID()+"");
		}
		return Keys;
	}

	public static AppUserModel MAIN_USER=new AppUserModel();

	public String interestsToString()
	{
		//TODO: Set Token
		StringBuilder stringBuilder=new StringBuilder("");
		String Token=",";

		ArrayList<String> Interests=Database.database.getInterestDB().getSelectedInterestStrings();

		for(String interest:Interests)
		{
			if(stringBuilder.toString().equals(""))
				stringBuilder.append(interest);
			else
			{
				stringBuilder.append(Token).append(interest);
			}
		}

		return stringBuilder.toString();
	}

	private ArrayList<String> stringToInterests(String Interests)
	{
		//TODO: Set Token
		String Token=",";
		String[] strings=Interests.split(Token);
		ArrayList<String> list = (ArrayList<String>)Arrays.asList(strings);

		return list;
	}

	public void setInterests(ArrayList<InterestModel> interests){Interests = interests;}

	public void setInterests(String interests)
	{
		setInterestsFromArray(stringToInterests(interests));
	}

	public void setInterestsFromArray(ArrayList<String> list)
	{
		for(InterestModel model :Interests)
			model.setSelected(false);

		int ID;

		for(String id: list)
		{
			ID=Integer.parseInt(id);
			for(InterestModel model :Interests)
			{
				if (model.getID()==ID)
				{
					model.setSelected(true);
					break;
				}
			}
		}
	}

	public boolean saveAppUser(Context context) {
		SharedPreferences.Editor editor=context.getSharedPreferences("User_Data",Context.MODE_PRIVATE).edit();
		editor.putString("Name",getName());
		editor.putString("Token",getToken());
		editor.putString("Email",getEmail());
		editor.putString("Roll",getRoll());
		editor.putString("Year",getYear());
		editor.putString("College",getCollege());
		editor.putString("Mobile",getMobile());
		editor.putString("Branch",getBranch());
		editor.putString("ImageId",getImageResource());
		editor.putString("Gender",getGender());
		editor.putBoolean("GoogleImage",isUseGoogleImage());
		editor.putInt("ImageDrawableID",getImageId());

		Database.database.getInterestDB().addOrUpdateInterest(Interests);

		return editor.commit();
	}

	public void loadAppUser(Context context)
	{
		SharedPreferences preferences=context.getSharedPreferences("User_Data",Context.MODE_PRIVATE);
		setName(preferences.getString("Name",""));
		setEmail(preferences.getString("Email",""));
		setRoll(preferences.getString("Roll",""));
		setYear(preferences.getString("Year","1"));
		setCollege(preferences.getString("College",""));
		setMobile(preferences.getString("Mobile",""));
		setBranch(preferences.getString("Branch",""));
		setImageResource(preferences.getString("ImageId",null));
		setToken(preferences.getString("Token",""));
		setGender(preferences.getString("Gender",""));
		setUseGoogleImage(preferences.getBoolean("GoogleImage",true));
		setImageId(preferences.getInt("ImageDrawableID",-1));

		Interests=Database.database.getInterestDB().getSelectedInterests();
	}

	public void logoutUser(Context context) {
		SharedPreferences.Editor editor=context.getSharedPreferences("User_Data",Context.MODE_PRIVATE).edit();
		editor.clear();
		editor.apply();

		Database.database.ResetTables();

		setLoggedIn(false,context);
		setSignedup(false,context);

		loadAppUser(context);
		saveAppUser(context);
	}
	public boolean isUserLoaded()
	{
		return (getEmail()!=null && !getEmail().equals(""));
	}

	public void setLoggedIn(boolean val,Context context){
		SharedPreferences.Editor editor=context.getSharedPreferences("authenticate",Context.MODE_PRIVATE).edit();
		editor.putBoolean("loggedIn",val);
		editor.apply();
	}
	public void setSignedup(boolean val,Context context){
		SharedPreferences.Editor editor=context.getSharedPreferences("authenticate",Context.MODE_PRIVATE).edit();
		editor.putBoolean("signedup",val);
		editor.apply();
	}

	public boolean isUserLoggedIn(Context context){
		SharedPreferences sp=context.getSharedPreferences("authenticate",Context.MODE_PRIVATE);
		loggedIn=sp.getBoolean("loggedIn",false);

		return loggedIn;
	}
	public boolean isUserSignedUp(Context context){
		SharedPreferences sp=context.getSharedPreferences("authenticate",Context.MODE_PRIVATE);
		signedup=sp.getBoolean("signedup",false);
		return signedup;
	}

	public void LoginUserNoHome(Activity activity, boolean Result)
	{
		Intent intent=new Intent(activity,Login.class);
		intent.putExtra("Start_Home",false);
		if(Result)
			activity.startActivityForResult(intent,LOGIN_REQUEST_CODE);
		else activity.startActivity(intent);
	}

	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			return this;
		}
	}
}
