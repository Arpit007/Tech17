package com.nitkkr.gawds.tech16.Model;

/**
 * Created by Home Laptop on 19-Nov-16.
 */

public class MessageModel
{
	private String Text;
	private int ID;
	private MessageType type;
	//Other Required Fields

	public String getText()
	{
		return Text;
	}
	public int getID()
	{
		return ID;
	}
	public MessageType getType()
	{
		return type;
	}

	public void setText(String text)
	{
		Text = text;
	}
	public void setID(int ID)
	{
		this.ID = ID;
	}
	public void setType(MessageType type)
	{
		this.type = type;
	}

}
