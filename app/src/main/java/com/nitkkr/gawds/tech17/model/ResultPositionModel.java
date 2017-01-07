package com.nitkkr.gawds.tech17.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class ResultPositionModel implements Serializable
{
	private ArrayList<UserKey> Winners;

	public ArrayList<UserKey> getWinners()
	{
		return Winners;
	}

	public void setWinners(ArrayList<UserKey> winners)
	{
		Winners = winners;
	}
}
