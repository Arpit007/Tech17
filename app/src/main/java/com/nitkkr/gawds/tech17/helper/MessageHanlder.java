package com.nitkkr.gawds.tech17.helper;

import android.content.Context;

import com.nitkkr.gawds.tech17.model.MessageModel;
import com.nitkkr.gawds.tech17.model.MessageType;
import com.nitkkr.gawds.tech17.src.MessageEvent;
import com.nitkkr.gawds.tech17.src.MessageInvite;

/**
 * Created by Home Laptop on 18-Nov-16.
 */

public class MessageHanlder
{
	public void DispatchMessage(String message, Context context)
	{
		MessageModel formattedMessage = getFormattedMessage(message);

		switch (formattedMessage.getType())
		{
			case EVENT:
				new MessageEvent().performAction(formattedMessage, context);
				break;
			case TEAM_INVITE:
				new MessageInvite().performAction(formattedMessage, context);
				break;
		}
	}

	public MessageType getMessageType(String message)
	{
		return MessageType.EVENT;
	}

	public MessageModel getFormattedMessage(String message)
	{
		switch (getMessageType(message))
		{
			case EVENT:
				return new MessageEvent().formatMessage(message);
			case TEAM_INVITE:
				return new MessageInvite().formatMessage(message);
		}
		return null;
	}
}
