package com.nitkkr.gawds.tech16.model;

import java.io.Serializable;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class EventKey implements Serializable
{
	private String Name="";
	private int EventID;
	private boolean Notify;
	private int Society=0;

	public int getSociety(){return Society;}
	public int getEventID()
	{
		return EventID;
	}
	public String getEventName()
	{
		return Name;
	}

	public void setSociety(int society){this.Society = society;}
	public void setEventID(int ID)
	{
		this.EventID = ID;
	}
	public void setEventName(String name)
	{
		Name = name;
	}
	public void setNotify(boolean notify)
	{
		Notify = notify;
	}

	public boolean isNotify()
	{
		return Notify;
	}
}
