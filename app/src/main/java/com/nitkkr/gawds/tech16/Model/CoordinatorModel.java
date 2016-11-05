package com.nitkkr.gawds.tech16.Model;

/**
 * Created by Home Laptop on 05-Nov-16.
 */

public class CoordinatorModel
{
	private String Name;
	private String Designation;
	private String Number;
	private String Image_URL;

	public String getName(){return Name;}
	public String getDesignation(){return Designation;}
	public  String getNumber(){return Number;}
	public String getImage_URL(){return Image_URL;}

	public void setName(String name){Name=name;}
	public void setDesignation(String designation){Designation=designation;}
	public void setNumber(String number){Number=number;}
	public void setImage_URL(String url){Image_URL=url;}
}
