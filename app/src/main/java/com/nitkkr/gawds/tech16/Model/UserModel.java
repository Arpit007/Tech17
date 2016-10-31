package com.nitkkr.gawds.tech16.Model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class UserModel
{
	private String Name;
	private String UserName;
	private String Email;
	private String Roll;
	private String College;
	private String Mobile;
	private String Branch;
	private ArrayList<String> Interest;

	public final static UserModel USER_MODEL=new UserModel();

	private String interestsToString()
	{
		StringBuilder stringBuilder=new StringBuilder("");
		for(String interest:Interest)
		{
			if(stringBuilder.toString().equals(""))
				stringBuilder.append(interest);
			else
			{
				stringBuilder.append(",");
				stringBuilder.append(interest);
			}
		}
		return stringBuilder.toString();
	}
	private ArrayList<String> stringToInterests(String Interests)
	{
		String[] strings=Interests.split(",");
		ArrayList<String> list = new ArrayList<>(strings.length);
		for(int x=0;x<strings.length;x++)
			list.set(x,strings[x]);
		return list;
	}


	public String getName() {return Name;}
	public String getUserName(){return UserName;}
	public String getEmail(){return Email;}
	public String getRoll(){return Roll;}
	public String getCollege(){return College;}
	public String getMobile(){return Mobile;}
	public String getBranch(){return Branch;}
	public ArrayList<String> getInterest(){return Interest;}
	public boolean isUserLoaded(){return !UserName.equals("");}

	@SuppressWarnings("unchecked")
	public void setUser(UserModel User)
	{
		Name = User.Name;
		UserName=User.UserName;
		Email = User.Email;
		Roll = User.Roll;
		College = User.College;
		Mobile = User.Mobile;
		Branch = User.Branch;
		Interest=(ArrayList<String>)User.Interest.clone();
	}

	public boolean saveUser(Context context)
	{
		SharedPreferences.Editor editor=context.getSharedPreferences("User_Data",Context.MODE_PRIVATE).edit();
		editor.putString("Name",Name);
		editor.putString("UserName",UserName);
		editor.putString("Email",Email);
		editor.putString("Roll",Roll);
		editor.putString("College",College);
		editor.putString("Mobile",Mobile);
		editor.putString("Branch",Branch);
		editor.putString("Interests",interestsToString());
		return editor.commit();
	}
	public void loadUser(Context context)
	{
		SharedPreferences preferences=context.getSharedPreferences("User_Data",Context.MODE_PRIVATE);
		Name=preferences.getString("Name","");
		UserName = preferences.getString("UserName","");
		Email=preferences.getString("Email","");
		Roll = preferences.getString("Roll","");
		College = preferences.getString("College","");
		Mobile=preferences.getString("Mobile","");
		Branch=preferences.getString("Branch","");
		Interest=stringToInterests(preferences.getString("Interests",""));
	}
}
