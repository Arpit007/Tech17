package com.nitkkr.gawds.tech17.src;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Home Laptop on 08-Jan-17.
 */

public class Feedback
{
	private boolean isFeedbackTime(Context context)
	{
		Calendar calendar = Calendar.getInstance();
		//22-Jan-2017
		calendar.set(Calendar.DATE,22);
		calendar.set(Calendar.MONTH,1);
		calendar.set(Calendar.YEAR,2017);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE,0);
		calendar.setTimeZone(TimeZone.getDefault());

		long prevDate=calendar.getTimeInMillis();
		long currDate=new Date().getTime();

		SharedPreferences preferences = context.getSharedPreferences("Feedback", Context.MODE_PRIVATE);
		boolean Generated=preferences.getBoolean("Feedback",false);
		long feedBackTime=preferences.getLong("Time",prevDate);

		preferences = context.getSharedPreferences(context.getString(R.string.Misc_Prefs), Context.MODE_PRIVATE);
		long AppCount=preferences.getLong("AppStartCount", 0);

		boolean result= (currDate>feedBackTime && AppCount > 6 && !Generated);
		return result;
	}

	public void showFeedback(final Context context, boolean onDelay)
	{
		final SharedPreferences preferences = context.getSharedPreferences("Feedback", Context.MODE_PRIVATE);

		if( ActivityHelper.isInternetConnected() && isFeedbackTime(context))
		{
			final AlertDialog.Builder builder=new AlertDialog.Builder(context);
			builder.setCancelable(true);
			builder.setMessage("Liked the App?\nPlease fill in the feedback.");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialogInterface, int i)
				{
					if(!ActivityHelper.isDebugMode(context))
						Answers.getInstance().logCustom(new CustomEvent("Feedback"));

					preferences.edit().putBoolean("Feedback",true).commit();

					String url = "http://techspardha.org/feedback";
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					context.startActivity(intent);

					dialogInterface.dismiss();
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				@Override
				public void onCancel(DialogInterface dialogInterface)
				{
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.MINUTE,45);

					SharedPreferences preferences = context.getSharedPreferences("Feedback", Context.MODE_PRIVATE);
					preferences.edit().putLong("Time",calendar.getTimeInMillis()).commit();
				}
			});
			builder.setTitle("Feedback");
			final AlertDialog dialog=builder.create();
			dialog.getWindow().getAttributes().windowAnimations = R.style.CloseDialogTheme;

			if(!onDelay)
				dialog.show();
			else
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						dialog.show();
					}
				},200);
		}
	}
}
