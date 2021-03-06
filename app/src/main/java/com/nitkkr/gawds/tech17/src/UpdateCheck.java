package com.nitkkr.gawds.tech17.src;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import static com.nitkkr.gawds.tech17.helper.ActivityHelper.getApplicationContext;
import static com.nitkkr.gawds.tech17.helper.ActivityHelper.isInternetConnected;

/**
 * Created by Home Laptop on 18-Nov-16.
 */

public class UpdateCheck
{
	private boolean UpdateAvailable = false;

	private static UpdateCheck CHECK_UPDATE = new UpdateCheck();

	public static UpdateCheck getInstance()
	{
		return CHECK_UPDATE;
	}

	private UpdateCheck()
	{
	}

	public boolean isUpdateAvailable()
	{
		SharedPreferences preferences = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.Misc_Prefs), Context.MODE_PRIVATE);
		UpdateAvailable = preferences.getBoolean("Update", UpdateAvailable);
		return UpdateAvailable;
	}

	public boolean displayUpdate(final Context context)
	{
		if (!ActivityHelper.isInternetConnected())
		{
			return false;
		}

		final SharedPreferences preferences = context.getSharedPreferences("Misc_Prefs", Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = context.getSharedPreferences("Misc_Prefs", Context.MODE_PRIVATE).edit();


		if (isUpdateAvailable())
		{
			final Date date = new Date(preferences.getLong("Update_Date", new Date().getTime()));

			if (date.after(new Date()))
			{
				return false;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setCancelable(false);
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialogInterface, int i)
				{
					if(!ActivityHelper.isDebugMode(context))
					{
						Answers.getInstance().logCustom(new CustomEvent("Updated App"));
					}
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					calendar.add(Calendar.HOUR, context.getResources().getInteger(R.integer.AfterUpdateHours));

					UpdateAvailable = false;
					editor.putBoolean("Update", false);

					editor.putLong("Update_Date", calendar.getTime().getTime());
					editor.apply();

					dialogInterface.dismiss();

					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
					intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
					context.startActivity(intent);

				}
			});
			builder.setNegativeButton("Later", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialogInterface, int i)
				{
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					calendar.add(Calendar.HOUR, context.getResources().getInteger(R.integer.LaterUpdateHours));

					UpdateAvailable = true;
					editor.putBoolean("Update", true);
					editor.putLong("Update_Date", calendar.getTime().getTime());
					editor.apply();

					dialogInterface.dismiss();
				}
			});
			builder.setTitle("Update");
			builder.setMessage(R.string.Update);
			final AlertDialog alertDialog = builder.create();
			alertDialog.setOnShowListener(
					new DialogInterface.OnShowListener()
					{
						@Override
						public void onShow(DialogInterface arg0)
						{

							alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context, R.color.button_color));
							alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.button_color));
							alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.button_color));
						}
					});
			alertDialog.getWindow().getAttributes().windowAnimations = R.style.CloseDialogTheme;
			alertDialog.show();
		}
		return true;
	}

	public void checkForUpdate(final Context context)
	{
		if(!isInternetConnected())
			return;
		
		String url = "http://carreto.pt/tools/android-store-version/?package=";
		try
		{
			url += context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			url += "null";
		}

		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
		{
			@Override
			public void onResponse(JSONObject response)
			{
				try
				{
					if (response != null && response.has("status") && response.getBoolean("status") && response.has("version"))
					{
						String version = response.getString("version");
						UpdateAvailable = ( context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName.compareTo(version) < 0 );
					}
					else
					{
						UpdateAvailable = false;
					}
				}
				catch (Exception e)
				{
					UpdateAvailable = false;
					e.printStackTrace();
				}
				finally
				{
					SharedPreferences.Editor editor = context.getSharedPreferences("Misc_Prefs", Context.MODE_PRIVATE).edit();
					editor.putBoolean("Update", UpdateAvailable);
					editor.apply();
				}
			}
		}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				try
				{
					UpdateAvailable = false;
					SharedPreferences.Editor editor = context.getSharedPreferences("Misc_Prefs", Context.MODE_PRIVATE).edit();
					editor.putBoolean("Update", UpdateAvailable);
					editor.apply();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(request);
	}

	public final JsonObjectRequest checkForUpdateRequest(final Context context)
	{
		String url = "http://carreto.pt/tools/android-store-version/?package=";
		try
		{
			url += context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			url += "null";
		}

		return new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
		{
			@Override
			public void onResponse(JSONObject response)
			{
				try
				{
					if (response != null && response.has("status") && response.getBoolean("status") && response.has("version"))
					{
						String version = response.getString("version");
						UpdateAvailable = ( context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName.compareTo(version) < 0 );
					}
					else
					{
						UpdateAvailable = false;
					}
				}
				catch (Exception e)
				{
					UpdateAvailable = false;
					e.printStackTrace();
				}
				finally
				{
					SharedPreferences.Editor editor = context.getSharedPreferences("Misc_Prefs", Context.MODE_PRIVATE).edit();
					editor.putBoolean("Update", UpdateAvailable);
					editor.commit();
				}
			}
		}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				try
				{
					UpdateAvailable = false;
					SharedPreferences.Editor editor = context.getSharedPreferences("Misc_Prefs", Context.MODE_PRIVATE).edit();
					editor.putBoolean("Update", UpdateAvailable);
					editor.commit();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
