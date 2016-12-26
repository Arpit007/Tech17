package com.nitkkr.gawds.tech16.model;

import android.content.Context;

import org.json.JSONObject;


/**
 * Created by Home Laptop on 16-Nov-16.
 */

public interface iMessageAction
{
	void performAction(MessageModel model, Context context);

	MessageModel formatMessage(String Data);

	JSONObject getObject(MessageModel model);
}
