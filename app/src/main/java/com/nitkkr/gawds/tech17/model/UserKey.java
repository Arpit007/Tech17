package com.nitkkr.gawds.tech17.model;

import java.io.Serializable;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class UserKey implements Serializable
{
	private TeamModel.TeamControl teamControl;
	private String Name = "";
	private String UserID = "";
	private String Roll = "";

	public TeamModel.TeamControl getTeamControl(){return teamControl;}
	public String getName()
	{
		return Name;
	}
	public String getUserID(){return UserID;}
	public String getRoll()
	{
		return Roll;
	}

	public void setTeamControl(TeamModel.TeamControl teamControl){this.teamControl=teamControl;}
	public void setName(String name)
	{
		Name = name;
	}
	public void setUserID(String userID){UserID = userID;}
	public void setRoll(String roll)
	{
		Roll = roll;
	}
}
