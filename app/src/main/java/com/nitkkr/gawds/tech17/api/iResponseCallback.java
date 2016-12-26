package com.nitkkr.gawds.tech17.api;

import com.nitkkr.gawds.tech17.helper.ResponseStatus;

/**
 * Created by Home Laptop on 21-Dec-16.
 */

public interface iResponseCallback
{
	void onResponse(ResponseStatus status);

	void onResponse(ResponseStatus status, Object object);
}
