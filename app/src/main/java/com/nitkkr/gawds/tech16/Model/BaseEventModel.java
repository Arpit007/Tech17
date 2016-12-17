package com.nitkkr.gawds.tech16.Model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class BaseEventModel extends EventKey implements Serializable
{
	private Date EventDate;
	private String Venue;
	private String Description;
	private String Image_URL;

	public Date getEventDate(){return EventDate;}
	public String getVenue(){return Venue;}
	public String getDescription(){return Description;}
	public String getImage_URL(){return Image_URL;}

	public void setEventDate(Date date){EventDate=date;}
	public void setVenue(String venue){Venue=venue;}
	public void setDescription(String description){Description=description;}
	public void setImage_URL(String image_URL){Image_URL=image_URL;}
}
