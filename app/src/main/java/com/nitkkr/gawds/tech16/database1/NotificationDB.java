package com.nitkkr.gawds.tech16.database1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nitkkr.gawds.tech16.helper1.ActivityHelper;
import com.nitkkr.gawds.tech16.model1.NotificationModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class NotificationDB extends SQLiteOpenHelper implements iBaseDB
{
	@Override
	public void deleteTable()
	{
		String Query="DROP TABLE " + DbConstants.Constants.getNotificationTableName() + ";";
		dbRequest.getDatabase().rawQuery(Query,null);
	}

	@Override
	public void resetTable()
	{
		String Query="DELETE FROM " + DbConstants.Constants.getNotificationTableName() + ";";
		Log.d("Query: ",Query);
		dbRequest.getDatabase().rawQuery(Query,null);
	}

	private iDbRequest dbRequest;

	public NotificationDB(Context context, iDbRequest dbRequest)
	{
		super(context, DbConstants.Constants.getDatabaseName(), null, DbConstants.Constants.getDatabaseVersion());
		this.dbRequest = dbRequest;

		if(DbConstants.Constants==null)
			DbConstants.Constants=new DbConstants(context);

		onCreate(dbRequest.getDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		sqLiteDatabase.execSQL(ActivityHelper.getApplicationContext().getString(R.string.Query_Create_NotificationTable));
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbConstants.Constants.getNotificationTableName());
	}

	public ArrayList<NotificationModel> getNotifications(String Clause)
	{
		ArrayList<NotificationModel> keys = new ArrayList<>();
		String Query = "SELECT * FROM " + DbConstants.Constants.getNotificationTableName();
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
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.NotificationNames.EventName.Name()),
							Columns.indexOf(DbConstants.NotificationNames.EventID.Name()),
							Columns.indexOf(DbConstants.NotificationNames.Date.Name()),
							Columns.indexOf(DbConstants.NotificationNames.Notify.Name()),
							Columns.indexOf(DbConstants.NotificationNames.Generated.Name())
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
					notification.setGenerated(cursor.getInt(ColumnIndex[4]) != 0);
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
		}
		return keys;
	}

	public ArrayList<NotificationModel> getAllNotifications()
	{
		return getNotifications("");
	}

	public void deleteNotification(NotificationModel model)
	{
		deleteNotification(model.getEventID());
	}

	public void deleteNotification(int ID)
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getNotificationTableName() + " WHERE " + DbConstants.NotificationNames.EventID.Name() + "=" + ID + ";";
		Log.d("Query:\t",Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	public String getTableName()
	{
		return DbConstants.Constants.getNotificationTableName();
	}

	public void addOrUpdateNotification(NotificationModel notification)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		ContentValues values=new ContentValues();
		values.put(DbConstants.NotificationNames.EventName.Name(),notification.getEventName());
		values.put(DbConstants.NotificationNames.EventID.Name(),notification.getEventID());
		values.put(DbConstants.NotificationNames.Date.Name(),notification.getDate());
		values.put(DbConstants.NotificationNames.Notify.Name(),((notification.isNotify())?1:0));
		values.put(DbConstants.NotificationNames.Generated.Name(),((notification.isGenerated())?1:0));

		if(database.update(DbConstants.Constants.getNotificationTableName(),values, DbConstants.NotificationNames.EventID.Name() + " = "+notification.getEventID(),null)<1)
		{
			database.insert(DbConstants.Constants.getNotificationTableName(),null,values);
		}
	}

	public void UpdateTable()
	{
		dbRequest.getDatabase().rawQuery(ActivityHelper.getApplicationContext().getString(R.string.Query_Update_NotificationList), null);
	}

	@Override
	public synchronized void close()
	{
		//super.close();
	}

	public void addOrUpdateNotification(ArrayList<NotificationModel> notifications)
	{
		SQLiteDatabase database=dbRequest.getDatabase();

		String TABLENAME=DbConstants.Constants.getInterestTableName();
		String Event_Name= DbConstants.NotificationNames.EventName.Name();
		String Event_ID= DbConstants.NotificationNames.EventID.Name();
		String Event_Date= DbConstants.NotificationNames.Date.Name();
		String Event_Notify= DbConstants.NotificationNames.Notify.Name();
		String Event_Generated= DbConstants.NotificationNames.Generated.Name();

		for(NotificationModel notification : notifications)
		{
			ContentValues values=new ContentValues();
			values.put(Event_Name,notification.getEventName());
			values.put(Event_ID,notification.getEventID());
			values.put(Event_Date,notification.getDate());
			values.put(Event_Notify,((notification.isNotify())?1:0));
			values.put(Event_Generated,((notification.isGenerated())?1:0));

			if(database.update(TABLENAME,values,Event_ID + " = " + notification.getEventID(),null) < 1)
			{
				database.insert(TABLENAME,null,values);
			}
		}
	}

	public long getRowCount()
	{
		return DatabaseUtils.queryNumEntries(dbRequest.getDatabase(), DbConstants.Constants.getNotificationTableName());
	}
}
