package com.nitkkr.gawds.tech16.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.model.CoordinatorModel;
import com.nitkkr.gawds.tech16.model.EventKey;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Home Laptop on 18-Dec-16.
 */

public class CoordinatorDB extends SQLiteOpenHelper implements iBaseDB
{
	@Override
	public void deleteTable()
	{
		String Query="DROP TABLE IF EXISTS " + DbConstants.Constants.getCoordinatorTableName() + ";";
		dbRequest.getDatabase().rawQuery(Query,null);
	}

	@Override
	public void resetTable()
	{
	}

	private iDbRequest dbRequest;

	public CoordinatorDB(Context context, iDbRequest dbRequest)
	{
		super(context, DbConstants.Constants.getDatabaseName(), null, DbConstants.Constants.getDatabaseVersion());
		this.dbRequest = dbRequest;

		onCreate(dbRequest.getDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		sqLiteDatabase.execSQL(ActivityHelper.getApplicationContext().getString(R.string.Query_Create_CoordinatorTable));
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbConstants.Constants.getCoordinatorTableName());
	}

	public ArrayList<CoordinatorModel> getCoordinators(EventKey key)
	{
		return getCoordinators(key.getEventID());
	}

	public ArrayList<CoordinatorModel> getCoordinators(int ID)
	{
		return getCoordinators(DbConstants.CoordinatorNames.EventID.Name() + " = " + ID);
	}

	public ArrayList<CoordinatorModel> getCoordinators(String Clause)
	{
		ArrayList<CoordinatorModel> keys = new ArrayList<>();
		String Query = "SELECT * FROM " + DbConstants.Constants.getCoordinatorTableName();
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
							Columns.indexOf(DbConstants.CoordinatorNames.CoordinatorName.Name()),
							Columns.indexOf(DbConstants.CoordinatorNames.EventID.Name()),
							Columns.indexOf(DbConstants.CoordinatorNames.Email.Name()),
							Columns.indexOf(DbConstants.CoordinatorNames.Designation.Name()),
							Columns.indexOf(DbConstants.CoordinatorNames.Mobile.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					CoordinatorModel coordinator=new CoordinatorModel();

					coordinator.setName(cursor.getString(ColumnIndex[0]));
					coordinator.setEventID(cursor.getInt(ColumnIndex[1]));
					coordinator.setEmail(cursor.getString(ColumnIndex[2]));
					coordinator.setDesignation(cursor.getString(ColumnIndex[3]));
					coordinator.setMobile(cursor.getString(ColumnIndex[4]));

					keys.add(coordinator);
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

	public ArrayList<CoordinatorModel> getAllCoordinators()
	{
		return getCoordinators("");
	}

	public void deleteCoordinator(EventKey key)
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getCoordinatorTableName() + " WHERE " + DbConstants.CoordinatorNames.EventID.Name() + " = " + key.getEventID() + ";";
		Log.d("Query:\t",Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	public void deleteCoordinator(String Name)
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getCoordinatorTableName() + " WHERE " + DbConstants.CoordinatorNames.CoordinatorName.Name() + " = " + Name + ";";
		Log.d("Query:\t",Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	public String getTableName()
	{
		return DbConstants.Constants.getCoordinatorTableName();
	}

	public void addOrUpdateCoordinator(CoordinatorModel coordinator)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		ContentValues values=new ContentValues();

		values.put(DbConstants.CoordinatorNames.CoordinatorName.Name(),coordinator.getName());
		values.put(DbConstants.CoordinatorNames.EventID.Name(),coordinator.getEventID());
		values.put(DbConstants.CoordinatorNames.Email.Name(),coordinator.getEmail());
		values.put(DbConstants.CoordinatorNames.Mobile.Name(),coordinator.getMobile());
		values.put(DbConstants.CoordinatorNames.Designation.Name(),coordinator.getDesignation());

		if(database.update(DbConstants.Constants.getCoordinatorTableName(),values, DbConstants.CoordinatorNames.CoordinatorName.Name() +
				" = " + coordinator.getName() + " AND " + DbConstants.CoordinatorNames.EventID.Name() + " = " + coordinator.getEventID(),null)<1)
		{
			database.insert(DbConstants.Constants.getCoordinatorTableName(),null,values);
		}
	}

	@Override
	public synchronized void close()
	{
		//super.close();
	}

	public void addOrUpdateCoordinator(ArrayList<CoordinatorModel> coordinators)
	{
		SQLiteDatabase database=dbRequest.getDatabase();

		String TABLENAME=DbConstants.Constants.getCoordinatorTableName();

		String Coordinator_Name=DbConstants.CoordinatorNames.CoordinatorName.Name();
		String Event_ID=DbConstants.CoordinatorNames.EventID.Name();
		String Coordinator_Email=DbConstants.CoordinatorNames.Email.Name();
		String Coordinator_Mobile=DbConstants.CoordinatorNames.Mobile.Name();
		String Coordinator_Designation=DbConstants.CoordinatorNames.Designation.Name();

		for(CoordinatorModel coordinator : coordinators)
		{
			ContentValues values=new ContentValues();

			values.put(Coordinator_Name,coordinator.getName());
			values.put(Event_ID,coordinator.getEventID());
			values.put(Coordinator_Email,coordinator.getEmail());
			values.put(Coordinator_Mobile,coordinator.getMobile());
			values.put(Coordinator_Designation,coordinator.getDesignation());

			if(database.update(TABLENAME,values, Coordinator_Name + " = \"" + coordinator.getName() + "\" AND " + Event_ID + " = " + coordinator.getEventID(),null)<1)
			{
				database.insert(DbConstants.Constants.getCoordinatorTableName(),null,values);
			}
		}
	}

	public long getRowCount()
	{
		return DatabaseUtils.queryNumEntries(dbRequest.getDatabase(), DbConstants.Constants.getCoordinatorTableName());
	}
}