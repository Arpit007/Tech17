package com.nitkkr.gawds.tech17.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class CoordinatorModel implements Serializable
{
	private int EventID;
	private String Name = "";
	private String Email = "info@techspardha.org";
	private String Mobile = "";
	private String Designation = "";

	public String getName()
	{
		return Name;
	}

	public String getEmail()
	{
		return Email;
	}

	public String getMobile()
	{
		return Mobile;
	}

	public int getEventID()
	{
		return EventID;
	}

	public String getDesignation()
	{
		return Designation;
	}

	public void setName(String name)
	{
		Name = name;
	}

	public void setEmail(String email)
	{
		Email = email;
	}

	public void setMobile(String mobile)
	{
		Mobile = mobile;
	}

	public void setEventID(int id)
	{
		EventID = id;
	}

	public void setDesignation(String designation)
	{
		Designation = designation;
	}

	public void CallCoordinator(Context context)
	{
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Mobile));
		context.startActivity(intent);
	}

	public void EmailCoordinator(Context context)
	{
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		emailIntent.setType("vnd.android.cursor.item/email");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ getEmail() });

		context.startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
	}

}
