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

	private String EventName;
	private Date EventDate;
	private String Venue;
	private String Description;
	private String Rules;
	private ArrayList<RoundResultModel> Results=new ArrayList<>();
	private EventStatus eventStatus=EventStatus.None;
	private ArrayList<CoordinatorModel> Coordinators=new ArrayList<>();
	private EventRoundModel roundModel;
	private EventStateListener listener;

	public EventModel(EventRoundModel eventDetail, EventStateListener stateListener)
	{
		roundModel=eventDetail;
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
