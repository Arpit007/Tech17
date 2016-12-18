package com.nitkkr.gawds.tech16.Model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class EventKey implements Serializable
{
	//Change ID from int to String

	public int getID()
	{
		return ID;
	}

	public void setID(int ID)
	{
		this.ID = ID;
	}

	public String getName()
	{
		return Name;
	}

	public void setName(String name)
	{
		Name = name;
	}

	public boolean isNotify()
	{
		return Notify;
	}

	public void setNotify(boolean notify)
	{
		Notify = notify;
	}

	private String Name;
	private int ID;
	private boolean Notify;

	public EventKey(@NonNull  String name, int id, boolean notify)
	{
		Name = name;
		ID=id;
		Notify=notify;
	}
}
