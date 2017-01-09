package com.nitkkr.gawds.tech17.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.model.EventKey;
import com.nitkkr.gawds.tech17.model.ExhibitionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Home Laptop on 18-Dec-16.
 */

public class ExhibitionDB extends SQLiteOpenHelper implements iBaseDB
{
	private Context context;

	@Override
	public void deleteTable()
	{
		String Query = "DROP TABLE IF EXISTS " + DbConstants.Constants.getExhibitionTableName() + ";";
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	@Override
	public void resetTable()
	{
		String Query = "UPDATE " + DbConstants.Constants.getExhibitionTableName() + " SET " + DbConstants.ExhibitionNames.Notify.Name() + " = 0;";
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	private iDbRequest dbRequest;


	public ExhibitionDB(Context context, iDbRequest dbRequest)
	{
		super(context, DbConstants.Constants.getDatabaseName(), null, DbConstants.Constants.getDatabaseVersion());
		this.dbRequest = dbRequest;
		this.context = context;

		if (DbConstants.Constants == null)
		{
			DbConstants.Constants = new DbConstants(context);
		}

		onCreate(dbRequest.getDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		sqLiteDatabase.execSQL(context.getString(R.string.Query_Create_ExhibitionTable));
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbConstants.Constants.getExhibitionTableName());
	}

	public ArrayList<ExhibitionModel> getExhibitions(String Clause)
	{
		ArrayList<ExhibitionModel> keys = new ArrayList<>();
		String Query = "SELECT * FROM " + DbConstants.Constants.getExhibitionTableName();
		if (Clause.equals(""))
		{
			Query += ";";
		}
		else
		{
			Query += " WHERE " + Clause + ";";
		}
		Log.d("Query:\t", Query);

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.ExhibitionNames.EventName.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.EventID.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Date.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Notify.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Venue.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Description.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.ImageUrl.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Author.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Pdf.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.GTalk.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					ExhibitionModel exhibition = new ExhibitionModel();

					exhibition.setEventName(cursor.getString(ColumnIndex[0]));
					exhibition.setEventID(Integer.parseInt(cursor.getString(ColumnIndex[1])));
					exhibition.setEventDate(cursor.getLong(ColumnIndex[2]));
					exhibition.setNotify(cursor.getInt(ColumnIndex[3]) != 0);
					exhibition.setVenue(cursor.getString(ColumnIndex[4]));
					exhibition.setDescription(cursor.getString(ColumnIndex[5]));
					exhibition.setImage_URL(cursor.getString(ColumnIndex[6]));
					exhibition.setAuthor(cursor.getString(ColumnIndex[7]));
					exhibition.setPdfLink(cursor.getString(ColumnIndex[8]));
					exhibition.setGTalk(cursor.getInt(ColumnIndex[9]));

					keys.add(exhibition);
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

	public ExhibitionModel getExhibition(int ID)
	{
		String Query = "SELECT * FROM " + DbConstants.Constants.getExhibitionTableName() + " WHERE " + DbConstants.ExhibitionNames.EventID.Name() + " = " + ID + ";";
		Log.d("Query:\t", Query);

		ExhibitionModel exhibition = new ExhibitionModel();

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.ExhibitionNames.EventName.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.EventID.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Date.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Notify.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Venue.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Description.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.ImageUrl.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Author.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Pdf.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.GTalk.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				exhibition.setEventName(cursor.getString(ColumnIndex[0]));
				exhibition.setEventID(Integer.parseInt(cursor.getString(ColumnIndex[1])));
				exhibition.setEventDate(cursor.getLong(ColumnIndex[2]));
				exhibition.setNotify(cursor.getInt(ColumnIndex[3]) != 0);
				exhibition.setVenue(cursor.getString(ColumnIndex[4]));
				exhibition.setDescription(cursor.getString(ColumnIndex[5]));
				exhibition.setImage_URL(cursor.getString(ColumnIndex[6]));
				exhibition.setAuthor(cursor.getString(ColumnIndex[7]));
				exhibition.setPdfLink(cursor.getString(ColumnIndex[8]));
				exhibition.setGTalk(cursor.getInt(ColumnIndex[9]));
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
		return exhibition;
	}

	public ExhibitionModel getExhibition(EventKey key)
	{
		return getExhibition(key.getEventID());
	}

	public ArrayList<ExhibitionModel> getRegisteredExhibitions()
	{
		return getExhibitions(DbConstants.ExhibitionNames.Notify.Name() + " = 1");
	}

	public ArrayList<ExhibitionModel> getAllExhibitions()
	{
		return getExhibitions("");
	}

	public EventKey getExhibitionKey(EventKey key)
	{
		return getExhibitionKey(key.getEventID());
	}

	public EventKey getExhibitionKey(int ID)
	{
		String Query = "SELECT " + DbConstants.ExhibitionNames.EventName.Name() + ", " + DbConstants.ExhibitionNames.EventID.Name() + ", " +
				DbConstants.ExhibitionNames.Notify.Name() + " FROM " + DbConstants.Constants.getExhibitionTableName() + " WHERE " +
				DbConstants.ExhibitionNames.EventID.Name() + " = " + ID + ";";
		Log.d("Query:\t", Query);

		EventKey key = new EventKey();

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.ExhibitionNames.EventName.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.EventID.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Notify.Name()),
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				key.setEventName(cursor.getString(ColumnIndex[0]));
				key.setEventID(Integer.parseInt(cursor.getString(ColumnIndex[1])));
				key.setNotify(cursor.getInt(ColumnIndex[2]) != 0);
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

	public ArrayList<EventKey> getExhibitionKeys(String Clause)
	{
		ArrayList<EventKey> keys = new ArrayList<>();
		String Query = "SELECT " + DbConstants.ExhibitionNames.EventName.Name() + ", " + DbConstants.ExhibitionNames.EventID.Name() + ", " +
				DbConstants.ExhibitionNames.Notify.Name() + " FROM " + DbConstants.Constants.getExhibitionTableName();
		if (Clause.equals(""))
		{
			Query += ";";
		}
		else
		{
			Query += " WHERE " + Clause + ";";
		}
		Log.d("Query:\t", Query);

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.ExhibitionNames.EventName.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.EventID.Name()),
							Columns.indexOf(DbConstants.ExhibitionNames.Notify.Name()),
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					EventKey key = new EventKey();

					key.setEventName(cursor.getString(ColumnIndex[0]));
					key.setEventID(Integer.parseInt(cursor.getString(ColumnIndex[1])));
					key.setNotify(cursor.getInt(ColumnIndex[2]) != 0);

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

	public ArrayList<EventKey> getRegisteredExhibitionKeys()
	{
		return getExhibitionKeys(DbConstants.ExhibitionNames.Notify.Name() + " = 1");
	}

	public ArrayList<EventKey> getAllExhibitionKeys()
	{
		return getExhibitionKeys("");
	}

	public void deleteExhibition(EventKey key)
	{
		deleteExhibition(key.getEventID());
	}

	public void deleteExhibition(int ID)
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getExhibitionTableName() + " WHERE " + DbConstants.ExhibitionNames.EventID.Name() + " = " + ID + ";";
		Log.d("Query:\t", Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	@Override
	public String getTableName()
	{
		return DbConstants.Constants.getExhibitionTableName();
	}

	public void addOrUpdateExhibition(ExhibitionModel exhibition)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		ContentValues values = new ContentValues();

		values.put(DbConstants.ExhibitionNames.EventName.Name(), exhibition.getEventName());
		values.put(DbConstants.ExhibitionNames.EventID.Name(), exhibition.getEventID());
		values.put(DbConstants.ExhibitionNames.Date.Name(), exhibition.getEventDate());
		values.put(DbConstants.ExhibitionNames.Notify.Name(), ( ( exhibition.isNotify() ) ? 1 : 0 ));
		values.put(DbConstants.ExhibitionNames.Venue.Name(), exhibition.getVenue());
		values.put(DbConstants.ExhibitionNames.Description.Name(), exhibition.getDescription());
		values.put(DbConstants.ExhibitionNames.ImageUrl.Name(), exhibition.getImage_URL());
		values.put(DbConstants.ExhibitionNames.Author.Name(), exhibition.getAuthor());
		values.put(DbConstants.ExhibitionNames.Pdf.Name(), exhibition.getPdfLink());
		values.put(DbConstants.ExhibitionNames.GTalk.Name(), exhibition.isGTalk());

		if (database.update(DbConstants.Constants.getExhibitionTableName(), values, DbConstants.ExhibitionNames.EventID.Name() + " = " + exhibition.getEventID(), null) < 1)
		{
			database.insert(DbConstants.Constants.getExhibitionTableName(), null, values);
		}
	}

	@Override
	public synchronized void close()
	{
		//super.close();
	}

	public void addOrUpdateExhibition(ArrayList<ExhibitionModel> exhibitions)
	{
		Log.v("DEBUG", "YES...............");
		SQLiteDatabase database = dbRequest.getDatabase();

		String TABLENAME = DbConstants.Constants.getExhibitionTableName();

		String Event_Name = DbConstants.ExhibitionNames.EventName.Name();
		String Event_ID = DbConstants.ExhibitionNames.EventID.Name();
		String Event_Date = DbConstants.ExhibitionNames.Date.Name();
		String Event_Notify = DbConstants.ExhibitionNames.Notify.Name();
		String Event_Venue = DbConstants.ExhibitionNames.Venue.Name();
		String Event_Description = DbConstants.ExhibitionNames.Description.Name();
		String Event_ImageURL = DbConstants.ExhibitionNames.ImageUrl.Name();
		String Event_Author = DbConstants.ExhibitionNames.Author.Name();
		String Event_Pdf = DbConstants.ExhibitionNames.Pdf.Name();
		String Event_GTalk = DbConstants.ExhibitionNames.GTalk.Name();

		for (ExhibitionModel exhibition : exhibitions)
		{
			ContentValues values = new ContentValues();

			values.put(Event_Name, exhibition.getEventName());
			values.put(Event_ID, exhibition.getEventID());
			values.put(Event_Date, exhibition.getEventDate());
			values.put(Event_Notify, ( ( exhibition.isNotify() ) ? 1 : 0 ));
			values.put(Event_Venue, exhibition.getVenue());
			values.put(Event_Description, exhibition.getDescription());
			values.put(Event_ImageURL, exhibition.getImage_URL());
			values.put(Event_Author, exhibition.getAuthor());
			values.put(Event_Pdf, exhibition.getPdfLink());
			values.put(Event_GTalk, exhibition.isGTalk());

			if (database.update(TABLENAME, values, Event_ID + " = " + exhibition.getEventID(), null) < 1)
			{
				database.insert(TABLENAME, null, values);
			}
		}
	}

	@Override
	public long getRowCount()
	{
		return DatabaseUtils.queryNumEntries(dbRequest.getDatabase(), DbConstants.Constants.getExhibitionTableName());
	}
}
