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
		void EventStatusChanged(com.nitkkr.gawds.tech16.Model.EventModel.EventStatus status);
	}

	public EventModel(){}
	public EventModel(String name){setEventName(name);}

	private String Rules;
	private int MinUsers;
	private int MaxUsers;
	private int TotalRounds = 0;
	private int CurrentRound = 0;
	private int Society;
	private int Category;
	private boolean Registered = false;
	private com.nitkkr.gawds.tech16.Model.EventModel.EventStatus status= com.nitkkr.gawds.tech16.Model.EventModel.EventStatus.None;
	private ArrayList<RoundResultModel> Result;
	private ArrayList<CoordinatorModel> Coordinators;
	private ArrayList<iUserModel> Participants;
	private EventStatusListener listener;


	public int getCategory(){return Category;}
	public int getSociety(){return Society;}
	public String getRules(){return Rules;}
	public int getMinUsers(){return MinUsers;}
	public int getMaxUsers(){return MaxUsers;}
	public int getTotalRounds(){return TotalRounds;}
	public int getCurrentRound(){return CurrentRound;}
	public com.nitkkr.gawds.tech16.Model.EventModel.EventStatus getEventStatus(){return  status;}
	public ArrayList<RoundResultModel> getResult(){return Result;}
	public ArrayList<CoordinatorModel> getCoordinators(){return Coordinators;}
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
	public void callStatusListener(com.nitkkr.gawds.tech16.Model.EventModel.EventStatus status) {
		this.status=status;
		if(listener!=null)
			listener.EventStatusChanged(status);
	}


	public void setListener(EventStatusListener listener){this.listener=listener;}
	public void setCategory(int category){Category = category;}
	public void setSociety(int society){Society = society;}
	public void setRules(String rules){Rules=rules;}
	public void setMaxUsers(int maxUsers){MaxUsers=maxUsers;}
	public void setMinUsers(int minUsers){MinUsers=minUsers;}
	public void setTotalRounds(int totalRounds){TotalRounds = totalRounds;}
	public void setCurrentRound(int currentRound){CurrentRound = currentRound;}
	public void setStatus(com.nitkkr.gawds.tech16.Model.EventModel.EventStatus status){this.status=status;}
	public void setResult(ArrayList<RoundResultModel> result){Result=result;}
	public void setCoordinators(ArrayList<CoordinatorModel> coordinators){Coordinators=coordinators;}
	public void setParticipants(ArrayList<iUserModel> participants){Participants=participants;}
	public void setStatusListener(EventStatusListener listener){this.listener=listener;}
	public void setRegistered(boolean registered){Registered=registered;}


	public boolean isFinalRound()
	{
		return CurrentRound == TotalRounds;
	}
	public boolean isRegistered(){return Registered;}
	public boolean isParticipantCountOK(){return (Participants.size()>=MinUsers && Participants.size()<=MaxUsers);}
	public boolean isSingleEvent(){return (MinUsers==MaxUsers && MinUsers==1);}
	public boolean isGroupEvent(){return !isSingleEvent();}
	public boolean isVariableGroupEvent(){return MinUsers!=MaxUsers;}


	public void setRoundLive() {
		if (CurrentRound == 0)
		{
			CurrentRound = 1;
		}
		if (status == com.nitkkr.gawds.tech16.Model.EventModel.EventStatus.Over)
		{
			CurrentRound++;
		}
		status = com.nitkkr.gawds.tech16.Model.EventModel.EventStatus.Live;
	}
	public void setRoundOver()	{
		status = com.nitkkr.gawds.tech16.Model.EventModel.EventStatus.Over;
	}
	public void setNextRound() {
		if (status == com.nitkkr.gawds.tech16.Model.EventModel.EventStatus.Over && !isFinalRound())
		{
			CurrentRound++;
			status = com.nitkkr.gawds.tech16.Model.EventModel.EventStatus.Upcoming;
		}
	}
}
