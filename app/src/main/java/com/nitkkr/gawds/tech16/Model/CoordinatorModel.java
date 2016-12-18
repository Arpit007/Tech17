package com.nitkkr.gawds.tech16.Model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class CoordinatorModel implements Serializable
{
	private int EventID;
	private String Name;
	private String Email;
	private String Mobile;
	private String Designation;

	public String getName() {return Name;}
	public String getEmail(){return Email;}
	public String getMobile(){return Mobile;}
	public int getEventID(){return EventID;}
	public String getDesignation(){return Designation;}

	public void setName(String name){Name = name;}
	public void setEmail(String email){Email=email;}
	public void setMobile(String mobile){Mobile=mobile;}
	public void setEventID(int id){EventID = id;}
	public void setDesignation(String designation){Designation=designation;}

	public void CallCoordinator(Activity activity){
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Mobile));
		activity.startActivity(intent);
	}

}
