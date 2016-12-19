package com.nitkkr.gawds.tech16.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nitkkr.gawds.tech16.Helper.ActivityHelper;

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
	private SocietyDB societyDB;
	private EventsDB eventsDB;

	public static Database database = null;

	private Database(){}

	public Database(Context context)
	{
		if(database!=null)
			return;

		database = this;

		DbConstants.Constants=new DbConstants(context);

		startDatabase(false);

		eventsDB=new EventsDB(context,Database.this);
		interestDB=new InterestDB(context,Database.this);
		societyDB = new SocietyDB(context,Database.this);
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
		if(sqLiteDatabase!=null && sqLiteDatabase.isOpen() && Restart)
			sqLiteDatabase.close();

		if (sqLiteDatabase == null)
		{
			sqLiteDatabase=SQLiteDatabase.openOrCreateDatabase(ActivityHelper.getApplicationContext().getDatabasePath(DbConstants.Constants.getDatabaseName()),null);
		}
		else if (!sqLiteDatabase.isOpen() || Restart)
		{
			sqLiteDatabase=SQLiteDatabase.openOrCreateDatabase(ActivityHelper.getApplicationContext().getDatabasePath(DbConstants.Constants.getDatabaseName()),null);
		}
	}

	public Cursor runQuery(String Query)
	{
		return getDatabase().rawQuery(Query,null);
	}

	public ExhibitionDB getExhibitionDB(){return exhibitionDB;}
	public NotificationDB getNotificationDB(){return notificationDB;}
	public CoordinatorDB getCoordinatorDB(){return coordinatorDB;}
	public SocietyDB getSocietyDB(){return societyDB;}
	public InterestDB getInterestDB(){return interestDB;}
	public EventsDB getEventsDB(){return eventsDB;}

}
