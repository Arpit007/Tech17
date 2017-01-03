package com.nitkkr.gawds.tech17.model;

import java.util.Date;

/**
 * Created by Home Laptop on 18-Dec-16.
 */

public class NotificationModel
{
	private int NotificationID=0;
	private int EventID=0;
	private String Title="";
	private String Message="";
	private boolean Seen=false;
	private boolean Updated=false;

	public int getNotificationID(){return NotificationID;}
	public int getEventID(){return EventID;}
	public String getTitle(){return Title;}
	public String getMessage(){return  Message;}
	public boolean isSeen(){return Seen;}
	public boolean isUpdated(){return Updated;}

	public void setNotificationID(int id){NotificationID = id;}
	public void setEventID(int eventID){EventID = eventID;}
	public void setTitle(String title){Title = title;}
	public void setMessage(String message){Message = message;}
	public void setSeen(boolean seen){Seen = seen;}
	public void setUpdated(boolean updated){Updated=updated;}
}
