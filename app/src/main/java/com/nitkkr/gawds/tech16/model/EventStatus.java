package com.nitkkr.gawds.tech16.model;

/**
 * Created by Home Laptop on 23-Dec-16.
 */
public enum EventStatus
{
	None("None"),
	Running("Running"),
	NotStarted("Not Started"),
	Started("Started"),
	Waiting("Waiting"),
	Delayed("Delayed"),
	Finished("Finished");



	private String value;
	static public EventStatus Parse(String data)
	{
		switch (data.toLowerCase())
		{
			case "running" :return Running;
			case "not started"  :return NotStarted;
			case "started":return Started;
			case "waiting": return Waiting;
			case "delayed": return Delayed;
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
