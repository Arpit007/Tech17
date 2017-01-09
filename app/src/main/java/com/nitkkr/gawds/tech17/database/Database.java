package com.nitkkr.gawds.tech17.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nitkkr.gawds.tech17.helper.ActivityHelper;

/**
 * Created by Home Laptop on 18-Dec-16.
 */

public class Database implements iDbRequest
{
	private SQLiteDatabase sqLiteDatabase = null;

	private ExhibitionDB exhibitionDB;
	private NotificationDB notificationDB;
	private CoordinatorDB coordinatorDB;
	private InterestDB interestDB;
	private SocietyDB societyDB;
	private EventsDB eventsDB;
	private TeamDB teamDB;

	private static Database database = null;

	public Database(Context context)
	{
		if (database != null)
		{
			return;
		}

		database = this;

		DbConstants.Constants = new DbConstants(context);

		startDatabase(context,false);

		eventsDB = new EventsDB(context, Database.this);
		interestDB = new InterestDB(context, Database.this);
		societyDB = new SocietyDB(context, Database.this);
		exhibitionDB = new ExhibitionDB(context, Database.this);
		notificationDB = new NotificationDB(context, Database.this);
		coordinatorDB = new CoordinatorDB(context, Database.this);
		teamDB = new TeamDB(context, Database.this);
	}

	public static Database getServiceInstance()
	{
		return database;
	}

	public static Database getInstance()
	{
		if (database == null)
		{
			database = new Database(ActivityHelper.getApplicationContext());
		}
		return database;
	}

	@Override
	public SQLiteDatabase getDatabase()
	{
		/*if (sqLiteDatabase == null || !sqLiteDatabase.isOpen())
		{
			startDatabase(false);
		}*/

		return sqLiteDatabase;
	}

	@Override
	protected void finalize() throws Throwable
	{
		closeDatabase();

		super.finalize();
	}

	public void closeDatabase()
	{
		if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
		{
			sqLiteDatabase.close();
			database=null;
		}
	}

	public void startDatabase(Context context, boolean Restart)
	{
		if (sqLiteDatabase != null && ( sqLiteDatabase.isOpen() || Restart ))
		{
			sqLiteDatabase.close();
		}

		if (sqLiteDatabase == null)
		{
			Log.d("Database:\t", "Opening");
			sqLiteDatabase = context.openOrCreateDatabase(DbConstants.Constants.getDatabaseName(), android.content.Context.MODE_PRIVATE, null);
		}
		else if (!sqLiteDatabase.isOpen() || Restart)
		{
			sqLiteDatabase = context.openOrCreateDatabase(DbConstants.Constants.getDatabaseName(), android.content.Context.MODE_PRIVATE, null);
		}
	}

	public Cursor runQuery(String Query)
	{
		return getDatabase().rawQuery(Query, null);
	}

	public ExhibitionDB getExhibitionDB()
	{
		return exhibitionDB;
	}

	public NotificationDB getNotificationDB()
	{
		return notificationDB;
	}

	public CoordinatorDB getCoordinatorDB()
	{
		return coordinatorDB;
	}

	public SocietyDB getSocietyDB()
	{
		return societyDB;
	}

	public InterestDB getInterestDB()
	{
		return interestDB;
	}

	public EventsDB getEventsDB()
	{
		return eventsDB;
	}

	public TeamDB getTeamDB()
	{
		return teamDB;
	}

	public void ResetTables()
	{
		exhibitionDB.resetTable();
		notificationDB.resetTable();
		coordinatorDB.resetTable();
		societyDB.resetTable();
		interestDB.resetTable();
		eventsDB.resetTable();
		teamDB.resetTable();
	}
}
