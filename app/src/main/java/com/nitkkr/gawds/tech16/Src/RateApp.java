package com.nitkkr.gawds.tech16.Src;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Home Laptop on 28-Nov-16.
 */

public class RateApp
{
	private static RateApp rateApp=new RateApp();

	public static RateApp getInstance(){return rateApp;}

	private RateApp(){}

	public void incrementAppStartCount(Context context)
	{
		final SharedPreferences preferences=context.getSharedPreferences(context.getString(R.string.Misc_Prefs), Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor=context.getSharedPreferences(context.getString(R.string.Misc_Prefs),Context.MODE_PRIVATE).edit();

		long count=preferences.getLong("AppStartCount",0);
		long maxCount=preferences.getLong("MaxCount",context.getResources().getInteger(R.integer.AppStartCount));

		count++;

		editor.putLong("AppStartCount",count);
		editor.putLong("MaxCount",maxCount);

		editor.apply();
	}

	public boolean isReadyForRating(Context context)
	{
		final SharedPreferences preferences=context.getSharedPreferences(context.getString(R.string.Misc_Prefs), Context.MODE_PRIVATE);
		long MaxCount=preferences.getLong("MaxCount",context.getResources().getInteger(R.integer.AppStartCount));

		if(MaxCount==-1)
			return false;

		long count=preferences.getLong("AppStartCount",0);

		return count >= MaxCount;
	}

	public void displayRating(final Context context)
	{

		if (!ActivityHelper.isInternetConnected())
			return;

		final SharedPreferences.Editor editor=context.getSharedPreferences(context.getString(R.string.Misc_Prefs),Context.MODE_PRIVATE).edit();
		final SharedPreferences preferences=context.getSharedPreferences(context.getString(R.string.Misc_Prefs), Context.MODE_PRIVATE);

		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setCancelable(false);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{

				editor.putLong("MaxCount",-1);
				editor.apply();

				Intent intent=new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id="+context.getPackageName()));
				context.startActivity(intent);

				dialogInterface.dismiss();
			}
		});

		builder.setNegativeButton("Later", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				long MaxCount=preferences.getLong("MaxCount",context.getResources().getInteger(R.integer.AppStartCount));
				MaxCount+=context.getResources().getInteger(R.integer.AppStartCount);

				editor.putLong("MaxCount",MaxCount);
				editor.apply();
				dialogInterface.dismiss();
			}
		});
		builder.setTitle("Rate Us");
		builder.setMessage(R.string.Rate);
		builder.create().show();
	}

}
