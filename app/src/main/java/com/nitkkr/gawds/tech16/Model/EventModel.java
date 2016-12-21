package com.nitkkr.gawds.tech16.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class EventModel extends BaseEventModel implements Serializable
{
	public enum EventStatus {
		None,
		Upcoming,
		RoundOver,
		Live,
		Over,
		Delayed,
		Pause
	}


	public interface EventStatusListener {
		void EventStatusChanged(EventStatus status);
	}

	private String Rules;
	private int MinUsers = 1;
	private int MaxUsers = 1;
	private int CurrentRound = 0;
	private int Society;
	private int Category;
	private boolean Registered = false;
	private EventStatus status= EventStatus.None;
	private ArrayList<RoundResultModel> Result;
	private ArrayList<iUserModel> Participants;
	private EventStatusListener listener;


	public int getCategory(){return Category;}
	public int getSociety(){return Society;}
	public String getRules(){return Rules;}
	public int getMinUsers(){return MinUsers;}
	public int getMaxUsers(){return MaxUsers;}
	public int getCurrentRound(){return CurrentRound;}
	public EventStatus getEventStatus(){return  status;}
	public ArrayList<RoundResultModel> getResult(){return Result;}
	public ArrayList<iUserModel> getParticipants(){return Participants;}
	public EventStatusListener getListener(){return listener;}
	public void callStatusListener() {
		try
		{
			if (listener != null)
				listener.EventStatusChanged(status);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void callStatusListener(EventStatus status) {
		this.status=status;
		if(listener!=null)
			listener.EventStatusChanged(status);
	}


	public void setListener(EventStatusListener listener){this.listener=listener;}
	public void setCategory(int category){Category = category;}
	public void setSociety(int society){Society = society;}
	public void setRules(String rules){Rules=rules;}
	public void setMaxUsers(int maxUsers){MaxUsers=maxUsers;}
	public void setCurrentRound(int currentRound){CurrentRound = currentRound;}
	public void setStatus(EventStatus status){this.status=status;}
	public void setResult(ArrayList<RoundResultModel> result){Result=result;}
	public void setParticipants(ArrayList<iUserModel> participants){Participants=participants;}
	public void setRegistered(boolean registered){Registered=registered;}

	public boolean isRegistered(){return Registered;}
	public boolean isParticipantCountOK(){return (Participants.size()>=MinUsers && Participants.size()<=MaxUsers);}
	public boolean isSingleEvent()
	{
		return (MaxUsers==1);
	}
	public boolean isGroupEvent()
	{
		return !isSingleEvent();
	}

	public static long parseDate(String date)
	{
		//TODO: Implement
		return 0;
	}

	//TODO: Implement or Depreciate
	public void setRoundLive() {
		if (CurrentRound == 0)
		{
			CurrentRound = 1;
		}
		if (status == EventModel.EventStatus.Over)
		{
			CurrentRound++;
		}
		status = EventModel.EventStatus.Live;
	}
	public void setRoundOver()	{
		status = EventStatus.Over;
	}
	public void setNextRound() {
		if (status == EventStatus.Over)
		{
			CurrentRound++;
			status = EventStatus.Upcoming;
		}
	}
}
