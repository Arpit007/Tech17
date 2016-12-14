package com.nitkkr.gawds.techspardha17.Model;

import java.io.Serializable;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class UserModel implements iUserModel, Serializable
{
	private String Name;
	private String Email;
	private String Roll;
	private String College;
	private String Mobile;
	private String Branch;
	private int imageResource =-1;

	public String getName() {return Name;}
	public String getEmail(){return Email;}
	public String getRoll(){return Roll;}
	public String getCollege(){return College;}
	public String getMobile(){return Mobile;}
	public String getBranch(){return Branch;}
	public int getImageResource(){return imageResource;}

	public void setName(String name){Name = name;}
	public void setEmail(String email){Email=email;}
	public void setRoll(String roll){Roll = roll;}
	public void setCollege(String college){College = college;}
	public void setMobile(String mobile){Mobile=mobile;}
	public void setBranch(String branch){Branch=branch;}
	public void setImageResource(int imageResource){this.imageResource=imageResource;}
}
