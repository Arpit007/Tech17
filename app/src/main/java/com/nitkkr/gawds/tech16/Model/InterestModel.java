package com.nitkkr.gawds.tech16.Model;

import java.io.Serializable;

/**
 * Created by Home Laptop on 18-Dec-16.
 */

public class InterestModel implements Serializable
{
	private int ID;
	private String Interest;
	private boolean Selected;

	public int getID(){return ID;}
	public String getInterest()
	{
		return Interest;
	}

	public void setID(int id){ID=id;}
	public void setInterest(String interest)
	{
		Interest = interest;
	}

	public boolean isSelected()
	{
		return Selected;
	}

	public void setSelected(boolean selected)
	{
		Selected = selected;
	}
}
