package com.nitkkr.gawds.tech16.Model;

import org.json.JSONArray;

/**
 * Created by Home Laptop on 16-Nov-16.
 */

public interface iMessageAction
{
	void performAction(MessageModel model);
	MessageModel formatMessage(String Data);
}
