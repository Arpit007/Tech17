package com.nitkkr.gawds.tech17.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Home Laptop on 05-Jan-17.
 */

public class TeamModel implements Serializable
{
	private int TeamID;
	private int EventID;
	private String TeamName = "";
	private TeamControl control = TeamControl.None;
	private ArrayList<UserKey> Members = new ArrayList<>();

	public int getTeamID(){return TeamID;}
	public ArrayList<UserKey> getMembers() {return Members;}
	public int getEventID(){return EventID;}
	public String getTeamName(){return TeamName;}
	public TeamControl getControl(){return control;}

	public void setTeamID(int teamID){TeamID=teamID;}
	public void setMembers(ArrayList<UserKey> members) {Members = members;}
	public void setEventID(int eventID){EventID = eventID;}
	public void setTeamName(String eventName){TeamName = eventName;}
	public void setControl(TeamControl control){this.control = control;}

	public String getInviteString()
	{

		StringBuilder stringBuilder = new StringBuilder("[");

		String ID;

		for(int x=1;x<Members.size();x++)
		{
			ID = Members.get(x).getUserID();

			if (ID.equals(""))
				continue;

			stringBuilder.append(",").append(ID);
		}

		stringBuilder.append("]");
		stringBuilder.deleteCharAt(1);

		return stringBuilder.toString();
	}

	public enum TeamControl implements Serializable
	{
		None("None"),
		Participant("Team Member"),
		Leader("Team Leader"),
		Pending("Pending"),
		Declined("Declined");

		private String value;
		public static TeamControl Parse(String value)
		{
			if(value.equals("Accepted"))
				return Participant;
			if(value.equals("Declined"))
				return Declined;
			if(value.equals("Pending"))
				return Pending;
			if(value.equals("Team Leader"))
				return Leader;
			return None;
		}
		TeamControl(String value)
		{
			this.value=value;
		}
		public String getValue(){return value;}
	}
}
