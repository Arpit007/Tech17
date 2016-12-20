package com.nitkkr.gawds.tech16.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Model.EventKey;
import com.nitkkr.gawds.tech16.Model.EventModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class EventsDB extends SQLiteOpenHelper implements iBaseDB
{
	@Override
	public void deleteTable()
	{
		String Query="DROP TABLE " + DbConstants.Constants.getEventsTableName() + ";";
		dbRequest.getDatabase().rawQuery(Query,null);
	}

	@Override
	public void resetTable()
	{
		String Query="UPDATE " + DbConstants.Constants.getEventsTableName()+ " SET " + DbConstants.EventNames.Notify.Name() + " = 0, " +
				DbConstants.EventNames.Registered.Name() + " = 0;";
		Log.d("Query: ",Query);
		dbRequest.getDatabase().rawQuery(Query,null);
	}

	private iDbRequest dbRequest;


	public EventsDB(Context context, iDbRequest dbRequest)
	{
		super(context, DbConstants.Constants.getDatabaseName(), null, DbConstants.Constants.getDatabaseVersion());
		this.dbRequest = dbRequest;

		if(DbConstants.Constants==null)
			DbConstants.Constants=new DbConstants(context);

		onCreate(dbRequest.getDatabase());
	}

	@Override
	public void onCreate( SQLiteDatabase sqLiteDatabase)
	{
		sqLiteDatabase.execSQL(ActivityHelper.getApplicationContext().getString(R.string.Query_Create_EventsTable));
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbConstants.Constants.getEventsTableName());
	}

	public ArrayList<EventModel> getEvents(String Clause)
	{
		ArrayList<EventModel> keys = new ArrayList<>();
		String Query = "SELECT * FROM " + DbConstants.Constants.getEventsTableName();
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
							Columns.indexOf(DbConstants.EventNames.EventName.Name()),
							Columns.indexOf(DbConstants.EventNames.EventID.Name()),
							Columns.indexOf(DbConstants.EventNames.Date.Name()),
							Columns.indexOf(DbConstants.EventNames.Notify.Name()),
							Columns.indexOf(DbConstants.EventNames.Venue.Name()),
							Columns.indexOf(DbConstants.EventNames.Description.Name()),
							Columns.indexOf(DbConstants.EventNames.ImageUrl.Name()),
							Columns.indexOf(DbConstants.EventNames.EndDate.Name()),
							Columns.indexOf(DbConstants.EventNames.Rules.Name()),
							Columns.indexOf(DbConstants.EventNames.MaxUser.Name()),
							Columns.indexOf(DbConstants.EventNames.Pdf.Name()),
							Columns.indexOf(DbConstants.EventNames.Registered.Name()),
							Columns.indexOf(DbConstants.EventNames.Society.Name()),
							Columns.indexOf(DbConstants.EventNames.Category.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					EventModel event = new EventModel();

					event.setEventName(cursor.getString(ColumnIndex[0]));
					event.setEventID(Integer.parseInt(cursor.getString(ColumnIndex[1])));
					event.setEventDate(cursor.getLong(ColumnIndex[2]));
					event.setNotify(cursor.getInt(ColumnIndex[3]) != 0);
					event.setVenue(cursor.getString(ColumnIndex[4]));
					event.setDescription(cursor.getString(ColumnIndex[5]));
					event.setImage_URL(cursor.getString(ColumnIndex[6]));
					event.setEventEndDate(cursor.getLong(ColumnIndex[7]));
					event.setRules(cursor.getString(ColumnIndex[8]));
					event.setMaxUsers(cursor.getInt(ColumnIndex[9]));
					event.setPdfLink(cursor.getString(ColumnIndex[10]));
					event.setRegistered(cursor.getInt(ColumnIndex[11])!=0);
					event.setSociety(cursor.getInt(ColumnIndex[12]));
					event.setCategory(cursor.getInt(ColumnIndex[13]));

					keys.add(event);
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

	public EventModel getEvent(int ID)
	{
		String Query = "SELECT * FROM " + DbConstants.Constants.getEventsTableName() + " WHERE " + DbConstants.EventNames.EventID.Name() + " = " + ID + ";";
		Log.d("Query:\t",Query);

		EventModel event = new EventModel();

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.EventNames.EventName.Name()),
							Columns.indexOf(DbConstants.EventNames.EventID.Name()),
							Columns.indexOf(DbConstants.EventNames.Date.Name()),
							Columns.indexOf(DbConstants.EventNames.Notify.Name()),
							Columns.indexOf(DbConstants.EventNames.Venue.Name()),
							Columns.indexOf(DbConstants.EventNames.Description.Name()),
							Columns.indexOf(DbConstants.EventNames.ImageUrl.Name()),
							Columns.indexOf(DbConstants.EventNames.EndDate.Name()),
							Columns.indexOf(DbConstants.EventNames.Rules.Name()),
							Columns.indexOf(DbConstants.EventNames.MaxUser.Name()),
							Columns.indexOf(DbConstants.EventNames.Pdf.Name()),
							Columns.indexOf(DbConstants.EventNames.Registered.Name()),
							Columns.indexOf(DbConstants.EventNames.Society.Name()),
							Columns.indexOf(DbConstants.EventNames.Category.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				event.setEventName(cursor.getString(ColumnIndex[0]));
				event.setEventID(Integer.parseInt(cursor.getString(ColumnIndex[1])));
				event.setEventDate(cursor.getLong(ColumnIndex[2]));
				event.setNotify(cursor.getInt(ColumnIndex[3]) != 0);
				event.setVenue(cursor.getString(ColumnIndex[4]));
				event.setDescription(cursor.getString(ColumnIndex[5]));
				event.setImage_URL(cursor.getString(ColumnIndex[6]));
				event.setEventEndDate(cursor.getLong(ColumnIndex[7]));
				event.setRules(cursor.getString(ColumnIndex[8]));
				event.setMaxUsers(cursor.getInt(ColumnIndex[9]));
				event.setPdfLink(cursor.getString(ColumnIndex[10]));
				event.setRegistered(cursor.getInt(ColumnIndex[11])!=0);
				event.setSociety(cursor.getInt(ColumnIndex[12]));
				event.setCategory(cursor.getInt(ColumnIndex[13]));
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
		return event;
	}

	public EventModel getEvent(EventKey key)
	{
		return getEvent(key.getEventID());
	}

	public ArrayList<EventModel> getAllEvents()
	{
		return getEvents("");
	}

	public ArrayList<EventModel> getRegisteredEvents()
	{
		return getEvents(DbConstants.EventNames.Registered.Name() + " = 1");
	}

	public EventKey getEventKey(EventKey key)
	{
		return getEventKey(key.getEventID());
	}

	public EventKey getEventKey(int ID)
	{
		String Query = "SELECT " + DbConstants.EventNames.EventName.Name() + ", " + DbConstants.EventNames.EventID.Name() + ", "+
				DbConstants.EventNames.Notify.Name() + ", " + DbConstants.EventNames.Society.Name() +" FROM " + DbConstants.Constants.getEventsTableName() + " WHERE " +
				DbConstants.EventNames.EventID.Name() + " = " + ID + ";";
		Log.d("Query:\t",Query);

		EventKey key = new EventKey();

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.EventNames.EventName.Name()),
							Columns.indexOf(DbConstants.EventNames.EventID.Name()),
							Columns.indexOf(DbConstants.EventNames.Notify.Name()),
							Columns.indexOf(DbConstants.EventNames.Society.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				key.setEventName(cursor.getString(ColumnIndex[0]));
				key.setEventID(cursor.getInt(ColumnIndex[1]));
				key.setNotify(cursor.getInt(ColumnIndex[2]) != 0);
				key.setSociety(cursor.getInt(ColumnIndex[3]));
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
		return key;
	}

	public ArrayList<EventKey> getEventKeys(String Clause)
	{
		ArrayList<EventKey> keys = new ArrayList<>();
		String Query = "SELECT " + DbConstants.EventNames.EventName.Name() + ", " + DbConstants.EventNames.EventID.Name() + ", "+
				DbConstants.EventNames.Notify.Name() + ", " + DbConstants.EventNames.Society.Name() +" FROM " + DbConstants.Constants.getEventsTableName();
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
							Columns.indexOf(DbConstants.EventNames.EventName.Name()),
							Columns.indexOf(DbConstants.EventNames.EventID.Name()),
							Columns.indexOf(DbConstants.EventNames.Notify.Name()),
							Columns.indexOf(DbConstants.EventNames.Society.Name()),
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					EventKey key = new EventKey();

					key.setEventName(cursor.getString(ColumnIndex[0]));
					key.setEventID(cursor.getInt(ColumnIndex[1]));
					key.setNotify(cursor.getInt(ColumnIndex[2]) != 0);
					key.setSociety(cursor.getInt(ColumnIndex[3]));
					keys.add(key);
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

	public ArrayList<EventKey> getRegisteredEventKeys()
	{
		return getEventKeys(DbConstants.EventNames.Registered.Name() + " = 1");
	}

	public ArrayList<EventKey> getAllEventKeys()
	{
		return getEventKeys("");
	}

	public void deleteEvent(EventKey key)
	{
		deleteEvent(key.getEventID());
	}

	public void deleteEvent(int ID)
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getEventsTableName() + " WHERE " + DbConstants.EventNames.EventID.Name() + " = " + ID + ";";
		Log.d("Query:\t",Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	@Override
	public String getTableName()
	{
		return DbConstants.Constants.getEventsTableName();
	}

	public void addOrUpdateEvent(EventModel event)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		ContentValues values=new ContentValues();

		values.put(DbConstants.EventNames.EventName.Name(),event.getEventName());
		values.put(DbConstants.EventNames.EventID.Name(),event.getEventID());
		values.put(DbConstants.EventNames.Date.Name(),event.getEventDate());
		values.put(DbConstants.EventNames.Notify.Name(),((event.isNotify())?1:0));
		values.put(DbConstants.EventNames.Venue.Name(),event.getVenue());
		values.put(DbConstants.EventNames.Description.Name(),event.getDescription());
		values.put(DbConstants.EventNames.ImageUrl.Name(),event.getImage_URL());
		values.put(DbConstants.EventNames.EndDate.Name(),event.getEventEndDate());
		values.put(DbConstants.EventNames.Rules.Name(),event.getRules());
		values.put(DbConstants.EventNames.MaxUser.Name(),event.getMaxUsers());
		values.put(DbConstants.EventNames.Pdf.Name(),event.getPdfLink());
		values.put(DbConstants.EventNames.Registered.Name(),event.isRegistered());
		values.put(DbConstants.EventNames.Society.Name(),event.getSociety());
		values.put(DbConstants.EventNames.Category.Name(),event.getCategory());

		if(database.update(DbConstants.Constants.getEventsTableName(),values, DbConstants.EventNames.EventID.Name() + " = "+event.getEventID(),null)<1)
		{
			database.insert(DbConstants.Constants.getEventsTableName(),null,values);
		}
	}

	@Override
	public synchronized void close()
	{
		//super.close();
	}

	public void addOrUpdateEvent(ArrayList<EventModel> events)
	{
		SQLiteDatabase database=dbRequest.getDatabase();

		String TABLENAME=DbConstants.Constants.getEventsTableName();

		String Event_Name= DbConstants.EventNames.EventName.Name();
		String Event_ID= DbConstants.EventNames.EventID.Name();
		String Event_Date= DbConstants.EventNames.Date.Name();
		String Event_Notify= DbConstants.EventNames.Notify.Name();
		String Event_Venue= DbConstants.EventNames.Venue.Name();
		String Event_Description= DbConstants.EventNames.Description.Name();
		String Event_ImageURL= DbConstants.EventNames.ImageUrl.Name();
		String Event_EndDate= DbConstants.EventNames.EndDate.Name();
		String Event_Rules= DbConstants.EventNames.Rules.Name();
		String Event_MaxUser= DbConstants.EventNames.MaxUser.Name();
		String Event_Pdf= DbConstants.EventNames.Pdf.Name();
		String Event_Registered= DbConstants.EventNames.Registered.Name();
		String Event_Society= DbConstants.EventNames.Society.Name();
		String Event_Category= DbConstants.EventNames.Category.Name();

		for(EventModel event : events)
		{
			ContentValues values=new ContentValues();

			values.put(Event_Name,event.getEventName());
			values.put(Event_ID,event.getEventID());
			values.put(Event_Date,event.getEventDate());
			values.put(Event_Notify,((event.isNotify())?1:0));
			values.put(Event_Venue,event.getVenue());
			values.put(Event_Description,event.getDescription());
			values.put(Event_ImageURL,event.getImage_URL());
			values.put(Event_EndDate,event.getEventEndDate());
			values.put(Event_Rules,event.getRules());
			values.put(Event_MaxUser,event.getMaxUsers());
			values.put(Event_Pdf,event.getPdfLink());
			values.put(Event_Registered,event.isRegistered());
			values.put(Event_Society,event.getSociety());
			values.put(Event_Category,event.getCategory());

			if(database.update(TABLENAME,values,Event_ID + " = "+event.getEventID(),null)<1)
			{
				database.insert(TABLENAME,null,values);
			}
		}
	}

	@Override
	public long getRowCount()
	{
		return DatabaseUtils.queryNumEntries(dbRequest.getDatabase(), DbConstants.Constants.getEventsTableName());
	}
}
