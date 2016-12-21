package com.nitkkr.gawds.tech16.helper;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;


/**
 * Created by Home Laptop on 21-Dec-16.
 */

public class FetchResponseHelper
{
	VolleyError error=null;
	private int requestCount =0;
	private int responseCount=0;

	private static FetchResponseHelper helper=new FetchResponseHelper();

	public static FetchResponseHelper getInstance(){return helper;}


	public void incrementRequestCount(){requestCount++;}
	public void incrementResponseCount(VolleyError error)
	{
		if (this.error == null)
			this.error=error;
		responseCount++;
		if(isAllFetchComplete())
		{
			DisplayError(ActivityHelper.getApplicationContext());
			requestCount = responseCount = 0;
		}
	}

	public void reset()
	{
		requestCount = responseCount = 0;
	}

	public boolean isAllFetchComplete()
	{
		return responseCount>=requestCount;
	}

	public boolean isAnyError(){ return  error!=null;}

	public void DisplayError(Context context)
	{
		if (isAnyError())
		{
			if(error instanceof TimeoutError || error instanceof NetworkError)
				Toast.makeText(context,"Network Error",Toast.LENGTH_LONG).show();
			else Toast.makeText(context,"Error Fetching Feed",Toast.LENGTH_LONG).show();
		}
	}


}
