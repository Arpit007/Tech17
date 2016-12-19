package com.nitkkr.gawds.tech16.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Model.InterestModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class InterestDB extends SQLiteOpenHelper
{
	private iDbRequest dbRequest;

	public InterestDB(Context context, iDbRequest dbRequest)
	{
		super(context, DbConstants.Constants.getDatabaseName(), null, DbConstants.Constants.getDatabaseVersion());
		this.dbRequest = dbRequest;

		onCreate(dbRequest.getDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		sqLiteDatabase.execSQL(ActivityHelper.getApplicationContext().getString(R.string.Query_Create_InterestsTable));
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbConstants.Constants.getInterestTableName());
	}

	public ArrayList<InterestModel> getInterests(String Clause)
	{
		ArrayList<InterestModel> keys = new ArrayList<>();
		String Query = "SELECT * FROM " + DbConstants.Constants.getInterestTableName();
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
							Columns.indexOf(DbConstants.InterestNames.Interest.Name()),
							Columns.indexOf(DbConstants.InterestNames.Selected.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					InterestModel interest=new InterestModel();
					interest.setInterest(cursor.getString(ColumnIndex[0]));
					interest.setSelected(cursor.getInt(ColumnIndex[1])!=0);
					keys.add(interest);
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

	public ArrayList<InterestModel> getAllInterests()
	{
		return getInterests("");
	}

	public ArrayList<InterestModel> getSelectedInterests()
	{
		return getInterests(DbConstants.InterestNames.Selected.Name() + " = 1");
	}

	public void deleteInterest(InterestModel interest)
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getInterestTableName() + " WHERE " + DbConstants.InterestNames.Interest.Name() + " = " + interest.getInterest() + ";";
		Log.d("Query:\t",Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	public String getTableName()
	{
		return DbConstants.Constants.getInterestTableName();
	}

	public void addOrUpdateInterest(InterestModel interest)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		ContentValues values=new ContentValues();
		values.put(DbConstants.InterestNames.Interest.Name(),interest.getInterest());
		values.put(DbConstants.InterestNames.Selected.Name(),((interest.isSelected())?1:0));

		if(database.update(DbConstants.Constants.getInterestTableName(),values, DbConstants.InterestNames.Interest.Name() + " = " + interest.getInterest(),null)<1)
		{
			database.insert(DbConstants.Constants.getInterestTableName(),null,values);
		}
	}

	@Override
	public synchronized void close()
	{
		//super.close();
	}

	public void addOrUpdateInterest(ArrayList<InterestModel> interests)
	{
		SQLiteDatabase database=dbRequest.getDatabase();

		String TABLENAME=DbConstants.Constants.getInterestTableName();
		String Interest_Name=DbConstants.InterestNames.Interest.Name();
		String Selected_Name=DbConstants.InterestNames.Selected.Name();

		for(InterestModel interest : interests)
		{
			ContentValues values=new ContentValues();
			values.put(Interest_Name,interest.getInterest());
			values.put(Selected_Name,((interest.isSelected())?1:0));

			if(database.update(TABLENAME,values, Interest_Name + " = " + interest.getInterest(),null)<1)
			{
				database.insert(TABLENAME,null,values);
			}
		}
	}

	public long getRowCount()
	{
		return DatabaseUtils.queryNumEntries(dbRequest.getDatabase(), DbConstants.Constants.getInterestTableName());
	}
}
