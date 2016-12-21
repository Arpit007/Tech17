package com.nitkkr.gawds.tech16.model1;

/**
 * Created by Home Laptop on 16-Nov-16.
 */

public enum MessageType
{
	SIMPLE_MESSAGE("Text"),
	EVENT_RESULT("Result"),
	EVENT("Event"),
	TEAM_INVITE("Invite");

	private String value;

	MessageType(String t)
	{
		value=t;
	}
	String getValue(){return value;}
}
