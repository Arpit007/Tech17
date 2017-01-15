package com.nitkkr.gawds.tech17.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.model.InterestModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class InterestDB extends SQLiteOpenHelper implements iBaseDB
{
	@Override
	public void deleteTable()
	{
		String Query = "DROP TABLE IF EXISTS " + DbConstants.Constants.getInterestTableName() + ";";
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	public void deleteTable(Database database)
	{
		String Query = "DROP TABLE IF EXISTS " + DbConstants.Constants.getInterestTableName() + ";";
		database.getDatabase().rawQuery(Query, null);
	}

	@Override
	public void resetTable()
	{
		String Query = "UPDATE " + DbConstants.Constants.getInterestTableName() + " SET " + DbConstants.InterestNames.Selected.Name() + " = 0;";
		Log.d("Query: ", Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	private iDbRequest dbRequest;
	private Context context;

	public InterestDB(Context context, iDbRequest dbRequest)
	{
		super(context, DbConstants.Constants.getDatabaseName(), null, DbConstants.Constants.getDatabaseVersion());
		this.context = context;
		this.dbRequest = dbRequest;

		onCreate(dbRequest.getDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		sqLiteDatabase.execSQL(context.getString(R.string.Query_Create_InterestsTable));
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbConstants.Constants.getInterestTableName());
	}

	public String getInterest(InterestModel model)
	{
		return getInterest(model.getID());
	}

	public String getInterest(int ID)
	{
		String Query = "SELECT " + DbConstants.InterestNames.Interest.Name() + " FROM " + DbConstants.Constants.getInterestTableName() +
				" WHERE " + DbConstants.InterestNames.Id.Name() + " = " + ID + ";";
		Log.d("Query:\t", Query);

		String Name = "";

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.InterestNames.Interest.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				Name = cursor.getString(ColumnIndex[0]);
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
		return Name;
	}

	public InterestModel getInterestModel(int ID)
	{
		String Query = "SELECT * FROM " + DbConstants.Constants.getInterestTableName() + " WHERE " + DbConstants.InterestNames.Id.Name() + " = " + ID + ";";
		Log.d("Query:\t", Query);

		InterestModel interest = new InterestModel();

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.InterestNames.Interest.Name()),
							Columns.indexOf(DbConstants.InterestNames.Selected.Name()),
							Columns.indexOf(DbConstants.InterestNames.Id.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				interest.setInterest(cursor.getString(ColumnIndex[0]));
				interest.setSelected(cursor.getInt(ColumnIndex[1]) != 0);
				interest.setID(cursor.getInt(ColumnIndex[2]));
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
		return interest;
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
		Log.d("Query:\t", Query);

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.InterestNames.Interest.Name()),
							Columns.indexOf(DbConstants.InterestNames.Selected.Name()),
							Columns.indexOf(DbConstants.InterestNames.Id.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					InterestModel interest = new InterestModel();
					interest.setInterest(cursor.getString(ColumnIndex[0]));
					interest.setSelected(cursor.getInt(ColumnIndex[1]) != 0);
					interest.setID(cursor.getInt(ColumnIndex[2]));
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

	public ArrayList<String> getInterestStrings(String Clause)
	{
		ArrayList<InterestModel> models = getInterests(Clause);
		ArrayList<String> keys = new ArrayList<>(models.size());

		for (InterestModel model : models)
		{
			keys.add(model.getInterest());
		}

		return keys;
	}

	public ArrayList<String> getAllInterestStrings()
	{
		return getInterestStrings("");
	}

	public ArrayList<String> getSelectedInterestStrings()
	{
		return getInterestStrings(DbConstants.InterestNames.Selected.Name() + " = 1");
	}

	public void deleteInterest(InterestModel interest)
	{
		deleteInterest(interest.getID());
	}

	public void deleteInterest(int ID)
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getInterestTableName() + " WHERE " + DbConstants.InterestNames.Id.Name() + " = " + ID + ";";
		Log.d("Query:\t", Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	@Override
	public String getTableName()
	{
		return DbConstants.Constants.getInterestTableName();
	}

	public void addOrUpdateInterest(InterestModel interest)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		ContentValues values = new ContentValues();
		values.put(DbConstants.InterestNames.Interest.Name(), interest.getInterest());
		values.put(DbConstants.InterestNames.Selected.Name(), ( ( interest.isSelected() ) ? 1 : 0 ));
		values.put(DbConstants.InterestNames.Id.Name(), interest.getID());

		if (database.update(DbConstants.Constants.getInterestTableName(), values, DbConstants.InterestNames.Id.Name() + " = " + interest.getID(), null) < 1)
		{
			database.insert(DbConstants.Constants.getInterestTableName(), null, values);
		}
	}

	public void addSelectedInterest(ArrayList<String> list)
	{
		String Query = "UPDATE " + DbConstants.Constants.getInterestTableName() + " SET " + DbConstants.InterestNames.Selected.Name() + " = 0;";
		dbRequest.getDatabase().rawQuery(Query, null);

		Query = "UPDATE " + DbConstants.Constants.getInterestTableName() + " SET " + DbConstants.InterestNames.Selected.Name() + " = 1 WHERE " +
				DbConstants.InterestNames.Id.Name() + " = ";

		for (String key : list)
		{
			dbRequest.getDatabase().rawQuery(Query + key + ";", null);
		}
	}

	@Override
	public synchronized void close()
	{
		//super.close();
	}

	public void addOrUpdateInterest(ArrayList<InterestModel> interests)
	{
		if (interests == null)
		{
			return;
		}
		SQLiteDatabase database = dbRequest.getDatabase();

		String TABLENAME = DbConstants.Constants.getInterestTableName();
		String Interest_Name = DbConstants.InterestNames.Interest.Name();
		String Selected = DbConstants.InterestNames.Selected.Name();
		String Interest_ID = DbConstants.InterestNames.Id.Name();

		for (InterestModel interest : interests)
		{
			ContentValues values = new ContentValues();
			values.put(Interest_Name, interest.getInterest());
			values.put(Selected, ( ( interest.isSelected() ) ? 1 : 0 ));
			values.put(Interest_ID, interest.getID());

			if (database.update(TABLENAME, values, Interest_ID + " = " + interest.getID(), null) < 1)
			{
				database.insert(TABLENAME, null, values);
			}
		}
	}

	@Override
	public long getRowCount()
	{
		return DatabaseUtils.queryNumEntries(dbRequest.getDatabase(), DbConstants.Constants.getInterestTableName());
	}
}
