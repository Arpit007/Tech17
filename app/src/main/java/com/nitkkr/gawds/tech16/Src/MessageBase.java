package com.nitkkr.gawds.tech16.Src;

import com.nitkkr.gawds.tech16.Model.iMessageAction;

/**
 * Created by Home Laptop on 18-Nov-16.
 */

public class MessageBase implements iMessageAction
{
	private String Message;

	@Override
	public void performAction()
	{
	}

	@Override
	public void setMessage(String message)
	{
		Message=message;
	}
}
