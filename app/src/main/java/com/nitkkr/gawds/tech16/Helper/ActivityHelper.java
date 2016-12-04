package com.nitkkr.gawds.tech16.Helper;

import android.app.Activity;
import android.content.Intent;

import com.nitkkr.gawds.tech16.API.Query;
import com.nitkkr.gawds.tech16.Activity.Home;
import com.nitkkr.gawds.tech16.Activity.ListPage;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class ActivityHelper
{
	public static boolean revertToHomeIfLast(Activity activity)
	{
		if(activity.isTaskRoot())
		{
			revertToHome(activity);
			return true;
		}
		return false;
	}

	public static void revertToHome(Activity activity)
	{
		Intent intent=new Intent(activity,Home.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		activity.startActivity(intent);
		activity.finish();
	}

	public static void startListActivity(Activity activity, String Label, Query query)
	{
		Intent intent=new Intent(activity, ListPage.class);
		intent.putExtra("Label",Label);
		intent.putExtra("Query", query);
		activity.startActivity(intent);
	}

}
