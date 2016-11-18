package com.nitkkr.gawds.tech16.Src;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.nitkkr.gawds.tech16.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Home Laptop on 18-Nov-16.
 */

public class CheckUpdate
{
	private boolean UpdateAvailable=false;

	public static final CheckUpdate CHECK_UPDATE=new CheckUpdate();

	private CheckUpdate(){}

	public boolean isUpdateAvailable()
	{
		return  UpdateAvailable;
	}

	public void displayUpdate(final Context context)
	{
		final SharedPreferences preferences=context.getSharedPreferences("Update",Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor=context.getSharedPreferences("Update",Context.MODE_PRIVATE).edit();

		try
		{
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			int version=preferences.getInt("Version",0);
			if(version < packageInfo.versionCode)
			{
				editor.putInt("Version",packageInfo.versionCode);
				editor.putLong("Date",new Date().getTime());
				editor.apply();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (UpdateAvailable)
		{
			final Date date=new Date(preferences.getLong("Date",new Date().getTime()));

			if(date.after(new Date()))
				return;

			AlertDialog.Builder builder=new AlertDialog.Builder(context);
			builder.setCancelable(false);
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialogInterface, int i)
				{
					Calendar calendar=Calendar.getInstance();
					calendar.setTime(date);
					calendar.add(Calendar.HOUR,context.getResources().getInteger(R.integer.AfterUpdateHours));

					editor.putLong("Date",calendar.getTime().getTime());
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
					calendar.setTime(date);
					calendar.add(Calendar.HOUR,context.getResources().getInteger(R.integer.LaterUpdateHours));
					editor.putLong("Date",calendar.getTime().getTime());
					editor.apply();
					dialogInterface.dismiss();
				}
			});
			builder.setTitle("Update");
			builder.setMessage(R.string.Update);
			builder.create().show();
		}
	}

	public boolean checkForUpdate()
	{
		//TODO: Check for Updates
		UpdateAvailable=true;
		return UpdateAvailable;
	}
}
