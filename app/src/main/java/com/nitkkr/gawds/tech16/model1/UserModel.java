package com.nitkkr.gawds.tech16.model1;

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
	private String imageResourceURL=null;
	private String token;
	private String Gender;
	private String year;
	private int ImageId=-1;
	private boolean useGoogleImage=true;

	public String getToken() {
		return token;
	}
	public String getName() {return Name;}
	public String getEmail(){return Email;}
	public String getRoll(){return Roll;}
	public String getCollege(){return College;}
	public String getMobile(){return Mobile;}
	public String getBranch(){return Branch;}
	public String getImageResource(){return imageResourceURL;}
	public int getImageId(){return ImageId;}
	public String getYear() {return year;}
	public String getGender() {
		return Gender;
	}
	public boolean isUseGoogleImage(){return useGoogleImage;}

	public void setName(String name){Name = name;}
	public void setEmail(String email){Email=email;}
	public void setRoll(String roll){Roll = roll;}
	public void setCollege(String college){College = college;}
	public void setMobile(String mobile){Mobile=mobile;}
	public void setBranch(String branch){Branch=branch;}
	public void setImageResource(String imageResource){this.imageResourceURL=imageResource;}
	public void setImageId(int imageId){ImageId=imageId;}
	public void  setUseGoogleImage(boolean useGoogleImage){this.useGoogleImage=useGoogleImage;}
	public void setToken(String token) {this.token = token;}
	public void setGender(String gender) {Gender = gender;}
	public void setYear(String year) {this.year = year;}
}
