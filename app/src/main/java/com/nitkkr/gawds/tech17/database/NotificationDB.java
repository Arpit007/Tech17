package com.nitkkr.gawds.tech17.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nitkkr.gawds.tech17.BuildConfig;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.model.NotificationModel;

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
		String Query = "DROP TABLE IF EXISTS " + DbConstants.Constants.getNotificationTableName() + ";";
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	@Override
	public void resetTable()
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getNotificationTableName() + ";";
		Log.d("Query: ", Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	private iDbRequest dbRequest;
	private int CodeVersion;

	public NotificationDB(Context context, iDbRequest dbRequest)
	{
		super(context, DbConstants.Constants.getDatabaseName(), null, DbConstants.Constants.getDatabaseVersion());
		this.dbRequest = dbRequest;

		if (DbConstants.Constants == null)
		{
			DbConstants.Constants = new DbConstants(context);
		}

		CodeVersion=context.getSharedPreferences("Update", Context.MODE_PRIVATE).getInt("Version",0);
		if(BuildConfig.VERSION_CODE>CodeVersion)
		{
			context.getSharedPreferences("Update", Context.MODE_PRIVATE).edit().putInt("Version",BuildConfig.VERSION_CODE).commit();
			deleteTable();
		}
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
			Query += " ORDER BY " + DbConstants.NotificationNames.NotificationID.Name() + " DESC";
		}
		else
		{
			Query += " WHERE " + Clause + " ORDER BY " + DbConstants.NotificationNames.NotificationID.Name() + " DESC";
		}
		Log.d("Query:\t", Query);

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.NotificationNames.NotificationID.Name()),
							Columns.indexOf(DbConstants.NotificationNames.EventID.Name()),
							Columns.indexOf(DbConstants.NotificationNames.Message.Name()),
							Columns.indexOf(DbConstants.NotificationNames.Seen.Name()),
							Columns.indexOf(DbConstants.NotificationNames.Title.Name()),
							Columns.indexOf(DbConstants.NotificationNames.Updated.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					NotificationModel notification = new NotificationModel();
					notification.setNotificationID(cursor.getInt(ColumnIndex[0]));
					notification.setEventID(cursor.getInt(ColumnIndex[1]));
					notification.setMessage(cursor.getString(ColumnIndex[2]));
					notification.setSeen(cursor.getInt(ColumnIndex[3]) == 0);
					notification.setTitle(cursor.getString(ColumnIndex[4]));
					notification.setUpdated(cursor.getInt(ColumnIndex[5])!=0);
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
		deleteNotification(model.getNotificationID());
	}

	public void deleteNotification(int ID)
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getNotificationTableName() + " WHERE " + DbConstants.NotificationNames.NotificationID.Name() + "=" + ID + ";";
		Log.d("Query:\t", Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	public String getTableName()
	{
		return DbConstants.Constants.getNotificationTableName();
	}

	public void addOrUpdateNotification(NotificationModel notification)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		ContentValues values = new ContentValues();
		values.put(DbConstants.NotificationNames.NotificationID.Name(), notification.getNotificationID());
		values.put(DbConstants.NotificationNames.EventID.Name(), notification.getEventID());
		values.put(DbConstants.NotificationNames.Message.Name(), notification.getMessage());
		values.put(DbConstants.NotificationNames.Seen.Name(), notification.isSeen());
		values.put(DbConstants.NotificationNames.Title.Name(), notification.getTitle());
		values.put(DbConstants.NotificationNames.Updated.Name(), notification.isUpdated());

		if (database.update(DbConstants.Constants.getNotificationTableName(), values, DbConstants.NotificationNames.NotificationID.Name() + " = " + notification.getNotificationID(), null) < 1)
		{
			database.insert(DbConstants.Constants.getNotificationTableName(), null, values);
		}
	}

	@Override
	public synchronized void close()
	{
		//super.close();
	}

	public void addOrUpdateNotification(ArrayList<NotificationModel> notifications)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		String TABLENAME = DbConstants.Constants.getInterestTableName();
		String Notification_ID = DbConstants.NotificationNames.NotificationID.Name();
		String Event_ID = DbConstants.NotificationNames.EventID.Name();
		String Message = DbConstants.NotificationNames.Message.Name();
		String Title = DbConstants.NotificationNames.Title.Name();
		String Seen = DbConstants.NotificationNames.Seen.Name();
		String Update = DbConstants.NotificationNames.Updated.Name();

		for (NotificationModel notification : notifications)
		{
			ContentValues values = new ContentValues();
			values.put(Notification_ID, notification.getNotificationID());
			values.put(Event_ID, notification.getEventID());
			values.put(Message, notification.getMessage());
			values.put(Title, notification.getTitle());
			values.put(Seen, notification.isSeen());
			values.put(Update, notification.isUpdated());

			if (database.update(TABLENAME, values, Notification_ID + " = " + notification.getNotificationID(), null) < 1)
			{
				database.insert(TABLENAME, null, values);
			}
		}
	}

	public long getRowCount()
	{
		return DatabaseUtils.queryNumEntries(dbRequest.getDatabase(), DbConstants.Constants.getNotificationTableName());
	}
}
