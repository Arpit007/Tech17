package com.nitkkr.gawds.tech17.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class EventModel extends BaseEventModel implements Serializable
{


	public interface EventStatusListener
	{
		void EventStatusChanged(EventStatus status);
	}

	private String Rules = "";
	private int MinUsers = 1;
	private int MaxUsers = 1;
	private int CurrentRound = 0;
	private int Society;
	private int Category;

	private boolean Informal = false;
	private boolean Registered = false;
	private EventStatus status = EventStatus.None;
	private String Result ="";
	private ArrayList<UserKey> Participants;
	private EventStatusListener listener;


	public int getCategory()
	{
		return Category;
	}

	public int getSociety()
	{
		return Society;
	}

	public String getRules()
	{
		return Rules;
	}

	public int getMinUsers()
	{
		return MinUsers;
	}

	public int getMaxUsers()
	{
		return MaxUsers;
	}

	public int getCurrentRound()
	{
		return CurrentRound;
	}

	public EventStatus getEventStatus()
	{
		return status;
	}

	public String getResult()
	{
		return Result;
	}

	public ArrayList<UserKey> getParticipants()
	{
		return Participants;
	}

	public EventStatusListener getListener()
	{
		return listener;
	}

	public void callStatusListener()
	{
		try
		{
			if (listener != null)
			{
				listener.EventStatusChanged(status);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void callStatusListener(EventStatus status)
	{
		this.status = status;
		if (listener != null)
		{
			listener.EventStatusChanged(status);
		}
	}


	public void setListener(EventStatusListener listener)
	{
		this.listener = listener;
	}

	public void setCategory(int category)
	{
		Category = category;
	}

	public void setSociety(int society)
	{
		Society = society;
	}

	public void setRules(String rules)
	{
		Rules = rules;
	}

	public void setMaxUsers(int maxUsers)
	{
		MaxUsers = maxUsers;
	}

	public void setCurrentRound(int currentRound)
	{
		CurrentRound = currentRound;
	}

	public void setStatus(EventStatus status)
	{
		this.status = status;
	}

	public void setResult(String result)
	{
		Result = result;
	}

	public void setParticipants(ArrayList<UserKey> participants)
	{
		Participants = participants;
	}

	public void setRegistered(boolean registered)
	{
		Registered = registered;
	}

	public void setInformal(boolean informal)
	{
		this.Informal = informal;
	}

	public boolean isRegistered()
	{
		return Registered;
	}

	public boolean isInformal()
	{
		return Informal;
	}

	public boolean isParticipantCountOK()
	{
		return ( Participants.size() >= MinUsers && Participants.size() <= MaxUsers );
	}

	public boolean isSingleEvent()
	{
		return ( MaxUsers == 1 );
	}

	public boolean isGroupEvent()
	{
		return !isSingleEvent();
	}

	public static long parseDate(String date)
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
		Date parsed_date = new Date();
		try
		{
			parsed_date = format.parse(date);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return parsed_date.getTime();

	}
}
