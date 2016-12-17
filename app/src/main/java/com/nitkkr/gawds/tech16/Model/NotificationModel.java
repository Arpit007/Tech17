package com.nitkkr.gawds.tech16.Model;

import java.util.Date;

/**
 * Created by Home Laptop on 18-Dec-16.
 */

public class NotificationModel extends EventKey
{
	private long date;

	public void setDate(long date){this.date = date;}
	public long getDate(){return date;}
	public Date getDateObject()
	{
		return new Date(date);
	}
}
