package com.nitkkr.gawds.techspardha17.Model;

import java.io.Serializable;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class ExhibitionModel extends BaseEventModel implements Serializable
{
	private String Author;

	public String getAuthor(){
		return Author;
	}
	public void setAuthor(String author){
		Author=author;
	}
}
