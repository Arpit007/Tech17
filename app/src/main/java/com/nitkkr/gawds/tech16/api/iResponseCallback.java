package com.nitkkr.gawds.tech16.api;

import com.nitkkr.gawds.tech16.helper.ResponseStatus;

/**
 * Created by Home Laptop on 21-Dec-16.
 */

public interface iResponseCallback
{
	void onResponse(ResponseStatus status);

	void onResponse(ResponseStatus status, Object object);
}
