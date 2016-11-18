package com.nitkkr.gawds.tech16.Model;

/**
 * Created by Home Laptop on 16-Nov-16.
 */

public enum eMessageType
{
	SIMPLE_MESSAGE("Text"),
	EVENT_RESULT("Result"),
	EVENT("Event"),
	TEAM_INVITE("Invite");

	private String value;

	eMessageType(String t)
	{
		value=t;
	}
	String getValue(){return value;}
}
