package com.nitkkr.gawds.tech17.model;

import java.io.Serializable;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class ExhibitionModel extends BaseEventModel implements Serializable
{
	private String Author = "";
	private int GTalk = 0;

	public String getAuthor()
	{
		return Author;
	}

	public int isGTalk()
	{
		return GTalk;
	}

	public void setAuthor(String author)
	{
		Author = author;
	}

	public void setGTalk(int gTalk)
	{
		GTalk = gTalk;
	}
}
