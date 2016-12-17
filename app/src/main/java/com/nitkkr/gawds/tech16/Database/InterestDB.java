package com.nitkkr.gawds.tech16.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nitkkr.gawds.tech16.Model.InterestModel;
import com.nitkkr.gawds.tech16.Model.NotificationModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class InterestDB extends SQLiteOpenHelper
{
	private Context context;
	private String TABLENAME = "Interest";

	public enum InterestNames
	{
		Interest("INTEREST"),
		Selected("SELECTED");

		private String Name;

		InterestNames(String value)
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

	public InterestDB(Context context)
	{
		super(context, context.getString(R.string.DatabaseName), null, context.getResources().getInteger(R.integer.DatabaseVersion));
		this.context = context;

		onCreate(getWritableDatabase());
		this.close();
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		sqLiteDatabase.execSQL(context.getString(R.string.CreateInterestsTable));
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

	public ArrayList<InterestModel> getInterests(String Clause)
	{
		ArrayList<InterestModel> keys = new ArrayList<>();
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
							Columns.indexOf(InterestNames.Interest.Name()),
							Columns.indexOf(InterestNames.Selected.Name())
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
			this.close();
		}
		return keys;
	}

	public ArrayList<InterestModel> getAllInterests(String Clause)
	{
		return getInterests("");
	}

	public ArrayList<InterestModel> getSelectedInterests()
	{
		return getInterests(InterestNames.Selected.Name() + " = 1");
	}

	public void deleteInterest(InterestModel interest)
	{
		String Query = "DELETE FROM " + TABLENAME + " WHERE " + InterestNames.Interest.Name() + " = " + interest.getInterest() + ";";
		Log.d("Query:\t",Query);
		getWritableDatabase().rawQuery(Query, null);
		this.close();
	}

	public String getTableName()
	{
		return TABLENAME;
	}

	public void addOrUpdateInterest(InterestModel interest)
	{
		SQLiteDatabase database = getWritableDatabase();

		ContentValues values=new ContentValues();
		values.put(InterestNames.Interest.Name(),interest.getInterest());
		values.put(InterestNames.Selected.Name(),((interest.isSelected())?1:0));

		if(database.update(TABLENAME,values, InterestNames.Interest.Name() + " = " + interest.getInterest(),null)<1)
		{
			database.insert(TABLENAME,null,values);
		}

		this.close();
	}

	public void addOrUpdateInterest(ArrayList<InterestModel> interests)
	{
		SQLiteDatabase database=getWritableDatabase();

		for(InterestModel interest : interests)
		{
			ContentValues values=new ContentValues();
			values.put(InterestNames.Interest.Name(),interest.getInterest());
			values.put(InterestNames.Selected.Name(),((interest.isSelected())?1:0));

			if(database.update(TABLENAME,values, InterestNames.Interest.Name() + " = " + interest.getInterest(),null)<1)
			{
				database.insert(TABLENAME,null,values);
			}
		}

		this.close();
	}
}
