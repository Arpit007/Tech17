package com.nitkkr.gawds.tech16.Model;

import java.io.Serializable;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class ExhibitionModel extends BaseEventModel implements Serializable
{
	public ExhibitionModel(){}
	public ExhibitionModel(String name){setEventName(name);}
	private String Author;

	public String getAuthor(){
		return Author;
	}
	public void setAuthor(String author){
		Author=author;
	}
}
