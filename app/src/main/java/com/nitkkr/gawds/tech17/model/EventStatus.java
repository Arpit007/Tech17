package com.nitkkr.gawds.tech17.model;

/**
 * Created by Home Laptop on 23-Dec-16.
 */
public enum EventStatus
{
	None("None"),
	NotStarted("Not Started"),
	Started("Started"),
	Finished("Finished");


	private String value;

	static public EventStatus Parse(String data)
	{
		switch (data.toLowerCase())
		{
			case "not started":
				return NotStarted;
			case "started":
				return Started;
			case "finished":
				return Finished;
			default:
				return None;
		}
	}

	EventStatus(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}
}
