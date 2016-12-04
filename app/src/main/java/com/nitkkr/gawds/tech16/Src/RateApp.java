package com.nitkkr.gawds.tech16.Src;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.nitkkr.gawds.tech16.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Home Laptop on 28-Nov-16.
 */

public class RateApp
{
	public static RateApp rateApp=new RateApp();

	//No new Class Instance
	private RateApp(){}

	public void incrementAppStartCount(Context context)
	{
		final SharedPreferences preferences=context.getSharedPreferences(context.getString(R.string.Misc_Prefs), Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor=context.getSharedPreferences(context.getString(R.string.Misc_Prefs),Context.MODE_PRIVATE).edit();

		long count=preferences.getLong("AppStartCount",0);
		count++;
		editor.putLong("AppStartCount",count);
		editor.apply();
	}

	public boolean isReadyForRating(Context context)
	{
		final SharedPreferences preferences=context.getSharedPreferences(context.getString(R.string.Misc_Prefs), Context.MODE_PRIVATE);
		long _date=preferences.getLong("Rate_Date",new Date().getTime());

		if(_date==0)
			return false;

		final Date date=new Date(_date);

		long count=preferences.getLong("AppStartCount",0);
		if(count>context.getResources().getInteger(R.integer.AppStartCount) && !date.after(new Date()))
			return true;
		return false;
	}

	public void displayRating(final Context context)
	{
		final SharedPreferences.Editor editor=context.getSharedPreferences(context.getString(R.string.Misc_Prefs),Context.MODE_PRIVATE).edit();

		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{

				editor.putLong("Rate_Date",0);
				editor.apply();

				Intent intent=new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id="+context.getPackageName()));
				context.startActivity(intent);
			}
		});
		builder.setNegativeButton("Later", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE,context.getResources().getInteger(R.integer.LaterRateDays));
				editor.putLong("Rate_Date",calendar.getTime().getTime());
				editor.apply();
				dialogInterface.dismiss();
			}
		});
		builder.setTitle("Rate Us");
		builder.setMessage(R.string.Rate);
		builder.create().show();
	}

}
