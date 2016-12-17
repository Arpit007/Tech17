package com.nitkkr.gawds.tech16.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nitkkr.gawds.tech16.Model.NotificationModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class NotificationDB extends SQLiteOpenHelper
{
	private Context context;
	private String TABLENAME = "Notification";

	public enum NotificationNames
	{
		EventName("EVENT_NAME"),
		EventID("EVENT_ID"),
		Notify("NOTIFY"),
		Date("_DATE");

		private String Name;

		NotificationNames(String value)
		{
			Name = value;
		}

		public String Name()
		{
			return Name;
		}

		@Override
		public String toString()
		{
			return Name;
		}
	}

	public NotificationDB(Context context)
	{
		super(context, context.getString(R.string.DatabaseName), null, context.getResources().getInteger(R.integer.DatabaseVersion));
		this.context = context;

		onCreate(getWritableDatabase());
		this.close();
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		sqLiteDatabase.execSQL(context.getString(R.string.CreateNotificationTable));
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
	}

	public void executeQuery(String Query)
	{
		Log.d("Query:\t",Query);

		getWritableDatabase().execSQL(Query);
		this.close();
	}

	public ArrayList<NotificationModel> getNotifications(String Clause)
	{
		ArrayList<NotificationModel> keys = new ArrayList<>();
		String Query = "SELECT * FROM " + TABLENAME;
		if (Clause.equals(""))
		{
			Query += ";";
		}
		else
		{
			Query += " WHERE " + Clause + ";";
		}
		Log.d("Query:\t",Query);

		Cursor cursor = null;
		try
		{
			cursor = getReadableDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(NotificationNames.EventName.Name()),
							Columns.indexOf(NotificationNames.EventID.Name()),
							Columns.indexOf(NotificationNames.Date.Name()),
							Columns.indexOf(NotificationNames.Notify.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					NotificationModel notification = new NotificationModel();
					notification.setEventName(cursor.getString(ColumnIndex[0]));
					notification.setEventID(Integer.parseInt(cursor.getString(ColumnIndex[1])));
					notification.setDate(cursor.getLong(ColumnIndex[2]));
					notification.setNotify(cursor.getInt(ColumnIndex[3]) != 0);
					keys.add(notification);
				}
				while (cursor.moveToNext());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			this.close();
		}
		return keys;
	}

	public ArrayList<NotificationModel> getAllNotifications(String Clause)
	{
		return getNotifications("");
	}

	public void deleteNotification(NotificationModel model)
	{
		deleteNotification(model.getEventID());
	}

	public void deleteNotification(int ID)
	{
		String Query = "DELETE FROM " + TABLENAME + " WHERE " + NotificationNames.EventID.Name() + "=" + ID + ";";
		Log.d("Query:\t",Query);
		getWritableDatabase().rawQuery(Query, null);
		this.close();
	}

	public String getTableName()
	{
		return TABLENAME;
	}

	public void addOrUpdateNotification(NotificationModel notification)
	{
		SQLiteDatabase database = getWritableDatabase();

		ContentValues values=new ContentValues();
		values.put(NotificationNames.EventName.Name(),notification.getEventName());
		values.put(NotificationNames.EventID.Name(),notification.getEventID());
		values.put(NotificationNames.Date.Name(),notification.getDate());
		values.put(NotificationNames.Notify.Name(),((notification.isNotify())?1:0));

		if(database.update(TABLENAME,values,NotificationNames.EventID.Name() + " = "+notification.getEventID(),null)<1)
		{
			database.insert(TABLENAME,null,values);
		}

		this.close();
	}

	public void addOrUpdateNotification(ArrayList<NotificationModel> notifications)
	{
		SQLiteDatabase database=getWritableDatabase();

		for(NotificationModel notification : notifications)
		{
			ContentValues values=new ContentValues();
			values.put(NotificationNames.EventName.Name(),notification.getEventName());
			values.put(NotificationNames.EventID.Name(),notification.getEventID());
			values.put(NotificationNames.Date.Name(),notification.getDate());
			values.put(NotificationNames.Notify.Name(),((notification.isNotify())?1:0));

			if(database.update(TABLENAME,values,NotificationNames.EventID.Name() + " = "+notification.getEventID(),null)<1)
			{
				database.insert(TABLENAME,null,values);
			}
		}

		this.close();
	}
}
