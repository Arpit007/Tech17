package com.nitkkr.gawds.tech16.model1;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class BaseEventModel extends EventKey implements Serializable
{
	private long EventDate;
	private long EventEndDate;
	private String Venue;
	private String Description;
	private String Image_URL;
	private String PdfLink;

	public long getEventDate(){return EventDate;}
	public long getEventEndDate(){return EventEndDate;}
	public String getVenue(){return Venue;}
	public String getDescription(){return Description;}
	public String getImage_URL(){return Image_URL;}
	public String getPdfLink(){return PdfLink;}
	public Date getDateObject(){return new Date(EventDate);}
	public Date getEndDateObject(){return new Date(EventEndDate);}

	public void setEventDate(long date){EventDate=date;}
	public void setEventEndDate(long date){EventEndDate=date;}
	public void setVenue(String venue){Venue=venue;}
	public void setPdfLink(String link){PdfLink = link;}
	public void setDescription(String description){Description=description;}
	public void setImage_URL(String image_URL){Image_URL=image_URL;}
}
