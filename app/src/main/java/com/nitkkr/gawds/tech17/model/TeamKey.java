package com.nitkkr.gawds.tech17.model;

import java.io.Serializable;

/**
 * Created by Home Laptop on 05-Jan-17.
 */

public class TeamKey implements Serializable
{
	private int EventID;
	private String TeamName;
	private TeamControl control = TeamControl.None;

	public int getEventID(){return EventID;}
	public String getTeamName(){return TeamName;}
	public TeamControl getControl(){return control;}

	public void setEventID(int eventID){EventID = eventID;}
	public void setTeamName(String eventName){TeamName = eventName;}
	public void setControl(TeamControl control){this.control = control;}

	public enum TeamControl
	{
		None("None"),
		Participant("Team Member"),
		Leader("Team Leader"),
		Pending("Pending");

		private String value;
		TeamControl(String value)
		{
			this.value=value;
		}
		public String getValue(){return value;}
	}

}
