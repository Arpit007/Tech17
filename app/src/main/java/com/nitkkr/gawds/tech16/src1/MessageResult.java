package com.nitkkr.gawds.tech16.src1;


import android.content.Context;

import com.nitkkr.gawds.tech16.model1.MessageModel;
import com.nitkkr.gawds.tech16.model1.iMessageAction;

import org.json.JSONObject;

/**
 * Created by Home Laptop on 18-Nov-16.
 */

public class MessageResult implements iMessageAction
{
	@Override
	public void performAction(MessageModel model, Context context)
	{
		//====================Implement============================
	}

	@Override
	public MessageModel formatMessage(String Data)
	{
		//====================Implement============================
		return null;
	}

	@Override
	public JSONObject getObject(MessageModel model)
	{
		return null;
	}
}