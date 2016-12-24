package com.nitkkr.gawds.tech16.model;

import java.io.Serializable;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class ExhibitionModel extends BaseEventModel implements Serializable
{
	private String Author="";
	private boolean GTalk=false;

	public String getAuthor(){
		return Author;
	}

	public boolean isGTalk(){return GTalk;}

	public void setAuthor(String author){
		Author=author;
	}
	public void setGTalk(boolean gTalk){GTalk=gTalk;}
}
