package com.nitkkr.gawds.tech16.Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Home Laptop on 05-Nov-16.
 */

public class EventModel implements EventRoundModel.RoundModelListener
{

	public interface EventStateListener
	{
		void EventStateChanged(EventStatus status);
	}

	public enum EventStatus
	{
		None,
		Upcoming,
		RoundOver,
		Live,
		Over,
		Delayed
	}

	private boolean Registered=false;
	private int MinUsers;
	private int MaxUsers;
	private String EventName;
	private Date EventDate;
	private String Venue;
	private String Description;
	private String Rules;
	private ArrayList<RoundResultModel> Results=new ArrayList<>();
	private EventStatus eventStatus=EventStatus.None;
	private ArrayList<CoordinatorModel> Coordinators=new ArrayList<>();
	private ArrayList<UserModel> Participants=new ArrayList<>();
	private EventRoundModel roundModel=new EventRoundModel();
	private EventStateListener listener;

	public EventModel(EventStateListener stateListener)
	{
		roundModel.setRoundModelListener(this);
		listener=stateListener;
	}

	public String getEventName(){return EventName;}
	public Date getEventDate(){return EventDate;}
	public String getVenue(){return Venue;}
	public String getDescription(){return Description;}
	public String getRules(){return Rules;}
	public final ArrayList<RoundResultModel> getResults(){return Results;}
	public RoundResultModel getRoundResult(int Round) {
		for(RoundResultModel resultModel:Results)
			if(resultModel.getRoundNumber()==Round)
				return resultModel;
		return null;
	}
	public EventStatus getEventStatus(){return  eventStatus;}
	public final ArrayList<CoordinatorModel> getCoordinators(){return Coordinators;}
	public final EventRoundModel getRounds(){return roundModel;}
	public int getMinUsers(){return MinUsers;}
	public int getMaxUsers(){return MaxUsers;}
	public final ArrayList<UserModel> getParticipants(){return Participants;}

	public boolean isRegistered(){return Registered;}
	public boolean isParticipantCountOK(){return (Participants.size()>=MinUsers && Participants.size()<=MaxUsers);}
	public boolean isSingleEvent(){return (MinUsers==MaxUsers && MinUsers==1);}
	public boolean isGroupEvent(){return !isSingleEvent();}
	public boolean isVariableGroupEvent(){return MinUsers!=MaxUsers;}

	public void setRegistered(boolean registered){Registered=registered;}
	public void setMaxUsers(int maxUsers){MaxUsers=maxUsers;}
	public void setMinUsers(int minUsers){MinUsers=minUsers;}
	public void setEventName(String name){EventName=name;}
	public void setEventDate(Date date){EventDate=date;}
	public void setVenue(String venue){Venue=venue;}
	public void setDescription(String description){Description=description;}
	public void setRules(String rules){Rules=rules;}
	public void setResults(ArrayList<RoundResultModel> results)
	{
		Results=results;
	}

	@Override
	public void RoundStatusChanged(EventRoundModel.RoundStatus status)
	{
		switch (status)
		{
			case Live:
				eventStatus=EventStatus.Live;
				break;
			case Over:
				if(roundModel.isFinalRound())
					eventStatus=EventStatus.Over;
				else eventStatus=EventStatus.RoundOver;
				break;
			case Delayed:
				eventStatus=EventStatus.Delayed;
				break;
			case Upcoming:
				eventStatus=EventStatus.Upcoming;
				break;
			default:
				eventStatus=EventStatus.None;
				break;
		}
		if(listener!=null)
			listener.EventStateChanged(eventStatus);
	}
}
