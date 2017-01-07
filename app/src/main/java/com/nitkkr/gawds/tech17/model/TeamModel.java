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

	public enum TeamControl implements Serializable
	{
		None("None"),
		Participant("Team Member"),
		Leader("Team Leader"),
		Pending("Pending"),
		Declined("Declined");

		private String value;
		TeamControl(String value)
		{
			this.value=value;
		}
		public String getValue(){return value;}
	}
}
