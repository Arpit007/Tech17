package com.nitkkr.gawds.tech16.Helper;

import com.nitkkr.gawds.tech16.Model.MessageModel;
import com.nitkkr.gawds.tech16.Model.MessageType;
import com.nitkkr.gawds.tech16.Src.MessageEvent;
import com.nitkkr.gawds.tech16.Src.MessageInvite;
import com.nitkkr.gawds.tech16.Src.MessageResult;
import com.nitkkr.gawds.tech16.Src.MessageSimple;

/**
 * Created by Home Laptop on 18-Nov-16.
 */

public class MessageHanlder
{
	public void DispatchMessage(String message)
	{
		MessageModel formattedMessage=getFormattedMessage(message);
		//==============================Set Model Fields here=======================

		switch(formattedMessage.getType())
		{
			case EVENT:
				new MessageEvent().performAction(formattedMessage);
				break;
			case TEAM_INVITE:
				new MessageInvite().performAction(formattedMessage);
				break;
			case EVENT_RESULT:
				new MessageResult().performAction(formattedMessage);
				break;
			case SIMPLE_MESSAGE:
				new MessageSimple().performAction(formattedMessage);
				break;
		}
	}

	public MessageType getMessageType(String message)
	{
		//TODO: Implement
		return MessageType.EVENT;
	}

	public MessageModel getFormattedMessage(String message)
	{
		switch(getMessageType(message))
		{
			case EVENT:
				return new MessageEvent().formatMessage(message);
			case TEAM_INVITE:
				return new MessageInvite().formatMessage(message);
			case EVENT_RESULT:
				return new MessageResult().formatMessage(message);
			case SIMPLE_MESSAGE:
				return new MessageSimple().formatMessage(message);
		}
		return null;
	}
}
