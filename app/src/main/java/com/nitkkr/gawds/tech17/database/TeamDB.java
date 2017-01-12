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
import com.nitkkr.gawds.tech17.model.EventModel;
import com.nitkkr.gawds.tech17.model.TeamModel;
import com.nitkkr.gawds.tech17.model.UserKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Home Laptop on 07-Jan-17.
 */

public class TeamDB extends SQLiteOpenHelper
{
	private iDbRequest dbRequest;
	private Context context;

	public TeamDB(Context context, iDbRequest dbRequest)
	{
		super(context, DbConstants.Constants.getDatabaseName(), null, DbConstants.Constants.getDatabaseVersion());
		this.context = context;
		this.dbRequest = dbRequest;

		if (DbConstants.Constants == null)
		{
			DbConstants.Constants = new DbConstants(context);
		}

		onCreate(dbRequest.getDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		sqLiteDatabase.execSQL(context.getString(R.string.Query_Create_MyTeamTable));
		sqLiteDatabase.execSQL(context.getString(R.string.Query_Create_TeamInviteTable));
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbConstants.Constants.getMyTeamTableName());
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbConstants.Constants.getTeamInviteTableName());
	}

	public void resetTable()
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getTeamInviteTableName() + ";";
		Log.d("Query: ", Query);
		dbRequest.getDatabase().rawQuery(Query, null);
		Query = "DELETE FROM " + DbConstants.Constants.getMyTeamTableName() + ";";
		Log.d("Query: ", Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	public void deleteTable()
	{
		String Query = "DROP TABLE IF EXISTS " + DbConstants.Constants.getMyTeamTableName() + ";";
		dbRequest.getDatabase().rawQuery(Query, null);

		Query = "DROP TABLE IF EXISTS " + DbConstants.Constants.getTeamInviteTableName() + ";";
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	@Override
	public synchronized void close()
	{
		//super.close();
	}

	public void addOrUpdateMyTeam(TeamModel team)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		ContentValues values = new ContentValues();

		values.put(DbConstants.TeamNames.TeamID.Name(), team.getTeamID());
		values.put(DbConstants.TeamNames.TeamName.Name(), team.getTeamName());
		values.put(DbConstants.TeamNames.EventID.Name(), team.getEventID());
		values.put(DbConstants.TeamNames.Control.Name(), team.getControl().getValue());

		try
		{
			ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
			ObjectOutput output=new ObjectOutputStream(byteArrayOutputStream);
			output.writeObject(team.getMembers());
			values.put(DbConstants.TeamNames.Participants.Name(),byteArrayOutputStream.toByteArray());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (database.update(DbConstants.Constants.getMyTeamTableName(), values, DbConstants.TeamNames.TeamID.Name() + " = " + team.getTeamID(), null) < 1)
		{
			database.insert(DbConstants.Constants.getMyTeamTableName(), null, values);
		}
	}

	public void addOrUpdateTeamInvite(TeamModel team)
	{
		SQLiteDatabase database = dbRequest.getDatabase();

		ContentValues values = new ContentValues();

		values.put(DbConstants.TeamNames.TeamID.Name(), team.getTeamID());
		values.put(DbConstants.TeamNames.TeamName.Name(), team.getTeamName());
		values.put(DbConstants.TeamNames.EventID.Name(), team.getEventID());

		try
		{
			ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
			ObjectOutput output=new ObjectOutputStream(byteArrayOutputStream);
			output.writeObject(team.getMembers());
			values.put(DbConstants.TeamNames.Participants.Name(),byteArrayOutputStream.toByteArray());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (database.update(DbConstants.Constants.getTeamInviteTableName(), values, DbConstants.TeamNames.TeamID.Name() + " = " + team.getTeamID(), null) < 1)
		{
			database.insert(DbConstants.Constants.getTeamInviteTableName(), null, values);
		}
	}

	public void addOrUpdateMyTeam(ArrayList<TeamModel> teams)
	{
		Log.v("DEBUG", "YES...............");
		SQLiteDatabase database = dbRequest.getDatabase();

		String TABLENAME = DbConstants.Constants.getMyTeamTableName();

		String Team_ID = DbConstants.TeamNames.TeamID.Name();
		String Event_ID = DbConstants.TeamNames.EventID.Name();
		String Team_Name = DbConstants.TeamNames.TeamName.Name();
		String Participants = DbConstants.TeamNames.Participants.Name();
		String Control = DbConstants.TeamNames.Control.Name();

		for (TeamModel team : teams)
		{
			ContentValues values = new ContentValues();

			values.put(Team_ID, team.getTeamID());
			values.put(Event_ID,team.getEventID());
			values.put(Team_Name,team.getTeamName());
			values.put(Control, team.getControl().getValue());

			try
			{
				ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
				ObjectOutput output=new ObjectOutputStream(byteArrayOutputStream);
				output.writeObject(team.getMembers());
				values.put(Participants,byteArrayOutputStream.toByteArray());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			if (database.update(TABLENAME, values, Team_ID + " = " + team.getTeamID(), null) < 1)
			{
				database.insert(TABLENAME, null, values);
			}
		}
	}

	public void addOrUpdateTeamInvite(ArrayList<TeamModel> teams)
	{
		Log.v("DEBUG", "YES...............");
		SQLiteDatabase database = dbRequest.getDatabase();

		String TABLENAME = DbConstants.Constants.getTeamInviteTableName();

		String Team_ID = DbConstants.TeamNames.TeamID.Name();
		String Event_ID = DbConstants.TeamNames.EventID.Name();
		String Team_Name = DbConstants.TeamNames.TeamName.Name();
		String Participants = DbConstants.TeamNames.Participants.Name();

		for (TeamModel team : teams)
		{
			ContentValues values = new ContentValues();

			values.put(Team_ID, team.getTeamID());
			values.put(Event_ID,team.getEventID());
			values.put(Team_Name,team.getTeamName());

			try
			{
				ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
				ObjectOutput output=new ObjectOutputStream(byteArrayOutputStream);
				output.writeObject(team.getMembers());
				values.put(Participants,byteArrayOutputStream.toByteArray());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			if (database.update(TABLENAME, values, Team_ID + " = " + team.getTeamID(), null) < 1)
			{
				database.insert(TABLENAME, null, values);
			}
		}
	}

	public ArrayList<TeamModel> getAllMyTeams()
	{
		return getMyTeams("");
	}

	public ArrayList<TeamModel> getAllTeamInvite()
	{
		return getTeamInvite("");
	}

	public ArrayList<TeamModel> getMyTeams(String Clause)
	{
		ArrayList<TeamModel> keys = new ArrayList<>();
		String Query = "SELECT * FROM " + DbConstants.Constants.getMyTeamTableName();
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
							Columns.indexOf(DbConstants.TeamNames.TeamID.Name()),
							Columns.indexOf(DbConstants.TeamNames.TeamName.Name()),
							Columns.indexOf(DbConstants.TeamNames.EventID.Name()),
							Columns.indexOf(DbConstants.TeamNames.Participants.Name()),
							Columns.indexOf(DbConstants.TeamNames.Control.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					TeamModel model = new TeamModel();

					model.setTeamID(cursor.getInt(ColumnIndex[0]));
					model.setTeamName(cursor.getString(ColumnIndex[1]));
					model.setEventID(cursor.getInt(ColumnIndex[2]));
					model.setControl(TeamModel.TeamControl.Parse(cursor.getString(ColumnIndex[4])));

					try
					{
						ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(cursor.getBlob(ColumnIndex[3]));
						ObjectInput objectInput=new ObjectInputStream(byteArrayInputStream);
						model.setMembers((ArrayList<UserKey>)objectInput.readObject());
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

					keys.add(model);
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

	public ArrayList<TeamModel> getTeamInvite(String Clause)
	{
		ArrayList<TeamModel> keys = new ArrayList<>();
		String Query = "SELECT * FROM " + DbConstants.Constants.getTeamInviteTableName();
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
							Columns.indexOf(DbConstants.TeamNames.TeamID.Name()),
							Columns.indexOf(DbConstants.TeamNames.TeamName.Name()),
							Columns.indexOf(DbConstants.TeamNames.EventID.Name()),
							Columns.indexOf(DbConstants.TeamNames.Participants.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					TeamModel model = new TeamModel();

					model.setTeamID(cursor.getInt(ColumnIndex[0]));
					model.setTeamName(cursor.getString(ColumnIndex[1]));
					model.setEventID(cursor.getInt(ColumnIndex[2]));

					try
					{
						ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(cursor.getBlob(ColumnIndex[3]));
						ObjectInput objectInput=new ObjectInputStream(byteArrayInputStream);
						model.setMembers((ArrayList<UserKey>)objectInput.readObject());
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

					keys.add(model);
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

	public TeamModel getMyTeam(int ID)
	{
		String Query = "SELECT * FROM " + DbConstants.Constants.getMyTeamTableName() + " WHERE " + DbConstants.TeamNames.TeamID.Name() + " = " + ID + ";";
		Log.d("Query:\t", Query);

		TeamModel model = new TeamModel();

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.TeamNames.TeamID.Name()),
							Columns.indexOf(DbConstants.TeamNames.TeamName.Name()),
							Columns.indexOf(DbConstants.TeamNames.EventID.Name()),
							Columns.indexOf(DbConstants.TeamNames.Participants.Name()),
							Columns.indexOf(DbConstants.TeamNames.Control.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				model.setTeamID(cursor.getInt(ColumnIndex[0]));
				model.setTeamName(cursor.getString(ColumnIndex[1]));
				model.setEventID(cursor.getInt(ColumnIndex[2]));
				model.setControl(TeamModel.TeamControl.Parse(cursor.getString(ColumnIndex[4])));

				try
				{
					ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(cursor.getBlob(ColumnIndex[3]));
					ObjectInput objectInput=new ObjectInputStream(byteArrayInputStream);
					model.setMembers((ArrayList<UserKey>)objectInput.readObject());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
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
		return model;
	}

	public TeamModel getInviteTeam(int ID)
	{
		String Query = "SELECT * FROM " + DbConstants.Constants.getTeamInviteTableName() + " WHERE " + DbConstants.TeamNames.TeamID.Name() + " = " + ID + ";";
		Log.d("Query:\t", Query);

		TeamModel model = new TeamModel();

		Cursor cursor = null;
		try
		{
			cursor = dbRequest.getDatabase().rawQuery(Query, null);

			List<String> Columns = Arrays.asList(cursor.getColumnNames());
			int[] ColumnIndex =
					{
							Columns.indexOf(DbConstants.TeamNames.TeamID.Name()),
							Columns.indexOf(DbConstants.TeamNames.TeamName.Name()),
							Columns.indexOf(DbConstants.TeamNames.EventID.Name()),
							Columns.indexOf(DbConstants.TeamNames.Participants.Name())
					};

			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();

				model.setTeamID(cursor.getInt(ColumnIndex[0]));
				model.setTeamName(cursor.getString(ColumnIndex[1]));
				model.setEventID(cursor.getInt(ColumnIndex[2]));

				try
				{
					ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(cursor.getBlob(ColumnIndex[3]));
					ObjectInput objectInput=new ObjectInputStream(byteArrayInputStream);
					model.setMembers((ArrayList<UserKey>)objectInput.readObject());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
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
		return model;
	}

	public void deleteInvite(int ID)
	{
		String Query = "DELETE FROM " + DbConstants.Constants.getTeamInviteTableName() + " WHERE " + DbConstants.TeamNames.TeamID.Name() + " = " + ID + ";";
		Log.d("Query:\t", Query);
		dbRequest.getDatabase().rawQuery(Query, null);
	}

	public long getInviteCount()
	{
		return DatabaseUtils.queryNumEntries(dbRequest.getDatabase(), DbConstants.Constants.getTeamInviteTableName());
	}
}
