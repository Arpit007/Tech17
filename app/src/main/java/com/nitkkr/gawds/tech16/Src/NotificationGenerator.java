package com.nitkkr.gawds.tech16.Src;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.nitkkr.gawds.tech16.Activity.Event;
import com.nitkkr.gawds.tech16.Activity.Exhibition;
import com.nitkkr.gawds.tech16.Activity.Home;
import com.nitkkr.gawds.tech16.Database.Database;
import com.nitkkr.gawds.tech16.Model.EventKey;
import com.nitkkr.gawds.tech16.Model.iMessageAction;
import com.nitkkr.gawds.tech16.R;

import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by Home Laptop on 21-Dec-16.
 */

public class NotificationGenerator
{
	Context context;
	private int LastId;

	public NotificationGenerator(Context context)
	{
		this.context = context;
		SharedPreferences preferences=context.getSharedPreferences("Generator", Context.MODE_PRIVATE);
		LastId=preferences.getInt("LastId",1000);
	}

	private NotificationCompat.Builder basicBuild(int IconID, String Label, String Ticker, String Message)
	{
		return new NotificationCompat.Builder(context)
				.setSmallIcon(IconID)
				.setTicker(Ticker)
				.setAutoCancel(true);
	}

	private void complexBuild(int IconID, String Label, String Ticker, String Message, iMessageAction action, RemoteViews view, Intent intent)
	{
		NotificationCompat.Builder builder=basicBuild(IconID,Label,Ticker,Message);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		{
			builder = builder.setContent(view);
		}
		else
		{
			builder = builder.setContentTitle(Label)
					.setContentText(Message)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
					.setWhen(new Date().getTime());
		}

		PendingIntent pIntent = PendingIntent.getActivity(context, LastId, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pIntent);

		Notification notification = builder.build();

		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE)).notify(LastId, notification);

		saveCache();
	}

	public void eventNotification(String Label, String Ticker, String Message, iMessageAction action, EventKey key)
	{
		int IconID=0;

		RemoteViews view=new RemoteViews(context.getPackageName(),R.layout.layout_notification_event);

		Bundle bundle=new Bundle();
		bundle.putSerializable("Event",key);
		bundle.putInt("NotificationID",LastId);
		Intent intent;

		if(Database.database==null)
			Database.database=new Database(context);

		if(key.getEventID()!= Database.database.getExhibitionDB().getExhibition(key.getEventID()).getEventID())
			intent=new Intent(context, Event.class);
		else intent=new Intent(context, Exhibition.class);

		intent.putExtras(bundle);

		complexBuild(IconID,Label,Ticker,Message,action,view,intent);
	}

	public void eventResultNotification(String Label, String Ticker, String Message,  iMessageAction action, EventKey key)
	{
		int IconID=0;

		RemoteViews view=new RemoteViews(context.getPackageName(),R.layout.layout_notification_event);

		//TODO:Implement
		Bundle bundle=new Bundle();
		bundle.putSerializable("Event",key);
		bundle.putInt("NotificationID",LastId);
		bundle.putInt("PageID",3);
		Intent intent=new Intent(context, Event.class);

		intent.putExtras(bundle);

		complexBuild(IconID,Label,Ticker,Message,action,view,intent);
	}

	public void inviteNotification(String Label, String Ticker, String Message, iMessageAction action)
	{
		int IconID=0;

		RemoteViews view=new RemoteViews(context.getPackageName(),R.layout.layout_notification_event);

		//TODO:Implement
		Bundle bundle=new Bundle();
		Intent intent=new Intent(context, Event.class);

		intent.putExtras(bundle);

		complexBuild(IconID,Label,Ticker,Message,action,view,intent);
	}

	public void simpleNotification(String Label, String Ticker, String Message, iMessageAction action)
	{
		int IconID=0;

		RemoteViews view=new RemoteViews(context.getPackageName(),R.layout.layout_notification_event);

		//TODO:Implement
		Intent intent=new Intent(context,Home.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

		complexBuild(IconID,Label,Ticker,Message,action,view,intent);
	}

	private void saveCache()
	{
		SharedPreferences.Editor editor=context.getSharedPreferences("Generator",Context.MODE_PRIVATE).edit();
		editor.putInt("LastId",LastId);
		editor.apply();
		LastId++;
	}
}
