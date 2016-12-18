package com.nitkkr.gawds.tech16.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Home Laptop on 18-Dec-16.
 */

public class Database implements iDbRequest
{
	private SQLiteDatabase sqLiteDatabase =null;

	private ExhibitionDB exhibitionDB;
	private NotificationDB notificationDB;
	private CoordinatorDB coordinatorDB;
	private InterestDB interestDB;
	private EventsDB eventsDB;

	public static Database database = null;

	private Database(){}

	public Database(Context context)
	{
		database = this;

		DbConstants.Constants=new DbConstants(context);

		eventsDB=new EventsDB(context,Database.this);
		interestDB=new InterestDB(context,Database.this);
		exhibitionDB=new ExhibitionDB(context,Database.this);
		notificationDB=new NotificationDB(context,Database.this);
		coordinatorDB = new CoordinatorDB(context,Database.this);
	}

	@Override
	public SQLiteDatabase getDatabase()
	{
		if(sqLiteDatabase ==null)
			startDatabase(false);

		if (!sqLiteDatabase.isOpen())
			startDatabase(false);

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
		if(sqLiteDatabase !=null && sqLiteDatabase.isOpen())
			sqLiteDatabase.close();
	}

	public void startDatabase(boolean Restart)
	{
		try
		{
			if (sqLiteDatabase == null)
			{
				sqLiteDatabase = eventsDB.getWritableDatabase();
			}
			else if (!sqLiteDatabase.isOpen())
			{
				sqLiteDatabase = eventsDB.getWritableDatabase();
			}
			else if (sqLiteDatabase.isOpen() && Restart)
			{
				sqLiteDatabase = eventsDB.getWritableDatabase();
			}
			if(sqLiteDatabase==null || !sqLiteDatabase.isOpen())
				throw new Exception("Unable to get Database");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			sqLiteDatabase = exhibitionDB.getWritableDatabase();
		}
	}

	public void runQuery(String Query)
	{
		getDatabase().rawQuery(Query,null);
	}

	public ExhibitionDB getExhibitionDB(){return exhibitionDB;}
	public NotificationDB getNotificationDB(){return notificationDB;}
	public CoordinatorDB getCoordinatorDB(){return coordinatorDB;}
	public InterestDB getInterestDB(){return interestDB;}
	public EventsDB getEventsDB(){return eventsDB;}

}
