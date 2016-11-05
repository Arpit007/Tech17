package com.nitkkr.gawds.tech16.Model;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 05-Nov-16.
 */

public class RegisteredEventModel
{
	private EventModel Event;
	private int MinUsers;
	private int MaxUsers;
	private ArrayList<UserModel> Participants=new ArrayList<>();

	public RegisteredEventModel(EventModel event, int minUsers, int maxUsers)
	{
		Event=event;
		MinUsers = minUsers;
		MaxUsers = maxUsers;
	}

	public EventModel getEvent(){return Event;}
	public int getMinUsers(){return MinUsers;}
	public int getMaxUsers(){return MaxUsers;}
	public final ArrayList<UserModel> getParticipants(){return Participants;}
	public boolean isParticipantCountOK(){return (Participants.size()>=MinUsers && Participants.size()<=MaxUsers);}
}
