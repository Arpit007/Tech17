package com.nitkkr.gawds.tech16.model;

/**
 * Created by Home Laptop on 23-Dec-16.
 */
public enum EventStatus
{
	None("None"),
	Upcoming("Upcoming"),
	Started("Started"),
	Live("Live"),
	Delay("Delayed"),
	Finished("Finished");



	private String value;
	static public EventStatus Parse(String data)
	{
		switch (data.toLowerCase())
		{
			case "upcoming" :return Upcoming;
			case "started"  :return Started;
			case "live":return Live;
			case "delayed": return Delay;
			case "finished": return Finished;
			default:return None;
		}
	}

	EventStatus(String value)
	{
		this.value=value;
	}
	String getValue(){return value;}
}
