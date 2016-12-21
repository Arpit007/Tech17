package com.nitkkr.gawds.tech16.model1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class ResultPositionModel implements Serializable
{
	private ArrayList<iUserModel> Winners;

	public ArrayList<iUserModel> getWinners(){return Winners;}
	public void setWinners(ArrayList<iUserModel> winners){Winners=winners;}
}
