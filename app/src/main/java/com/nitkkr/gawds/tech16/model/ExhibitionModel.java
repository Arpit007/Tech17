package com.nitkkr.gawds.tech16.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class ExhibitionModel extends BaseEventModel implements Serializable
{
	private String Author;
	private boolean GTalk=false;

	public String getAuthor(){
		return Author;
	}

	public boolean isGTalk(){return GTalk;}

	public void setAuthor(String author){
		Author=author;
	}
	public void setGTalk(boolean gTalk){GTalk=gTalk;}
	public static long parseDate(String date)
	{
		//TODO: Implement
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
		Date parsed_date=new Date();
		try {
			parsed_date = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parsed_date.getTime();
	}
}
