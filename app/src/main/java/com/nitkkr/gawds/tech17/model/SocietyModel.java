package com.nitkkr.gawds.tech17.model;

/**
 * Created by Home Laptop on 19-Dec-16.
 */

public class SocietyModel
{
	private int ID;
	private String Name = "";
	private String Description = "";


	public String getDescription()
	{
		return Description;
	}

	public int getID()
	{
		return ID;
	}

	public String getName()
	{
		return Name;
	}

	public void setID(int id)
	{
		ID = id;
	}

	public void setName(String name)
	{
		Name = name;
	}

	public void setDescription(String description)
	{
		Description = description;
	}
}
