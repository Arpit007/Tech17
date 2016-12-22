package com.nitkkr.gawds.tech16.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nitkkr.gawds.tech16.helper.ActivityHelper;

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

	public static Database getInstance(){if(database==null)database=new Database(ActivityHelper.getApplicationContext());return database;}

	@Override
	public SQLiteDatabase getDatabase()
	{
		if(sqLiteDatabase ==null || !sqLiteDatabase.isOpen())
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
		if(sqLiteDatabase!=null && (sqLiteDatabase.isOpen() || Restart))
			sqLiteDatabase.close();

		if (sqLiteDatabase == null)
		{
			Log.d("Database:\t","Opening");
			sqLiteDatabase=ActivityHelper.getApplicationContext().openOrCreateDatabase(DbConstants.Constants.getDatabaseName(),android.content.Context.MODE_PRIVATE ,null);
		}
		else if (!sqLiteDatabase.isOpen() || Restart)
		{
			sqLiteDatabase=ActivityHelper.getApplicationContext().openOrCreateDatabase(DbConstants.Constants.getDatabaseName(),android.content.Context.MODE_PRIVATE ,null);
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

	public void ResetTables()
	{
		exhibitionDB.resetTable();
		notificationDB.resetTable();
		coordinatorDB.resetTable();
		societyDB.resetTable();
		interestDB.resetTable();
		eventsDB.resetTable();
	}


}
