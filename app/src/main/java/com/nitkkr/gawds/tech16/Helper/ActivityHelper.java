package com.nitkkr.gawds.tech16.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import com.nitkkr.gawds.tech16.API.Query;
import com.nitkkr.gawds.tech16.Activity.Home;
import com.nitkkr.gawds.tech16.Activity.ListPage;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class ActivityHelper
{
	private static Context context=null;

	public static Context getApplicationContext(){return context;}

	public static void setApplictionContext(Context context){ActivityHelper.context = context;}

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
		Bundle bundle=new Bundle();
		bundle.putSerializable("Query",query);
		bundle.putString("Label",Label);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}

	public static boolean isDebugMode(Context context)
	{
		int appFlags=context.getApplicationInfo().flags;
		return (appFlags & ApplicationInfo.FLAG_DEBUGGABLE)!=0;
	}
}
