package com.nitkkr.gawds.tech16.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.api.Query;
import com.nitkkr.gawds.tech16.activity.Home;
import com.nitkkr.gawds.tech16.activity.ListPage;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class ActivityHelper
{
	private static Context context=null;

	public static void setStatusBarColor(Activity activity)
	{
		setStatusBarColor(R.color.status_bar_color,activity);
	}

	public static void setStatusBarColor(int statusBarColorID, Activity activity)
	{
		Window window = activity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		//window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setStatusBarColor(ContextCompat.getColor(activity, statusBarColorID));

		Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ts_logo2);
		ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription("Techspardha", bm, ContextCompat.getColor(activity, R.color.pin_screen_color));
		activity.setTaskDescription(taskDescription);
	}

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

	public static boolean isInternetConnected()
	{
		ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
}
