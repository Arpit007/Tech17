package com.nitkkr.gawds.tech16.Database;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Home Laptop on 18-Dec-16.
 */

public class DbConstants
{
	private String DATABASE_NAME;
	private String EVENTS_TABLE_NAME;
	private String EXHIBITION_TABLE_NAME;
	private String NOTIFICATION_TABLE_NAME;
	private String INTEREST_TABLE_NAME;
	private String COORDINATOR_TABLE_NAME;
	private String SOCIETY_TABLE_NAME;
	private int DATABASE_VERSION=1;

	public String getDatabaseName()
	{
		return DATABASE_NAME;
	}
	public String getEventsTableName(){return EVENTS_TABLE_NAME;}
	public String getExhibitionTableName(){return  EXHIBITION_TABLE_NAME;}
	public String getNotificationTableName(){return NOTIFICATION_TABLE_NAME;}
	public String getInterestTableName(){return INTEREST_TABLE_NAME;}
	public String getCoordinatorTableName(){return COORDINATOR_TABLE_NAME;}
	public String getSocietyTableName(){return SOCIETY_TABLE_NAME;}
	public int getDatabaseVersion(){return DATABASE_VERSION;}

	public void setDatabaseName(String databaseName, Context context){DATABASE_NAME=databaseName; saveCache(context);}
	public void setEventsTableName(String eventsTableName, Context context){EVENTS_TABLE_NAME = eventsTableName; saveCache(context);}
	public void setExhibitionTableName(String exhibitionTableName, Context context){EXHIBITION_TABLE_NAME = exhibitionTableName; saveCache(context);}
	public void setNotificationTableName(String notificationTableName, Context context){NOTIFICATION_TABLE_NAME = notificationTableName; saveCache(context);}
	public void setInterestTableName(String interestTableName, Context context){INTEREST_TABLE_NAME = interestTableName; saveCache(context);}
	public void setCoordinatorTableName(String coordinatorTableName, Context context){COORDINATOR_TABLE_NAME = coordinatorTableName; saveCache(context);}
	public void setSocietyTableName(String societyTableName, Context context){SOCIETY_TABLE_NAME = societyTableName; saveCache(context);}
	public void setDatabaseVersion(int databaseVersion, Context context){DATABASE_VERSION = databaseVersion; saveCache(context);}

	public static DbConstants Constants=new DbConstants();

	private DbConstants()
	{
		DATABASE_NAME="Tech17";
		DATABASE_VERSION=1;
		EVENTS_TABLE_NAME="EventList";
		EXHIBITION_TABLE_NAME="Exhibition";
		NOTIFICATION_TABLE_NAME="NotificationList";
		INTEREST_TABLE_NAME="Interests";
		COORDINATOR_TABLE_NAME="Coordinator";
		SOCIETY_TABLE_NAME="Society";
	}

	public DbConstants(Context context)
	{
		loadCache(context);
	}

	public void loadCache(Context context)
	{
		SharedPreferences preferences=context.getSharedPreferences("DbConstants",Context.MODE_PRIVATE);

		DATABASE_NAME=preferences.getString("DbName","Tech17.db");
		DATABASE_VERSION=preferences.getInt("DbVersion",1);
		EVENTS_TABLE_NAME=preferences.getString("EventTableName","EventList");
		EXHIBITION_TABLE_NAME=preferences.getString("ExhibitionTableName","Exhibition");
		NOTIFICATION_TABLE_NAME=preferences.getString("NotificationTableName","NotificationList");
		INTEREST_TABLE_NAME=preferences.getString("InterestTableName","Interests");
		COORDINATOR_TABLE_NAME=preferences.getString("CoordinatorTableName","Coordinator");
		SOCIETY_TABLE_NAME=preferences.getString("SocietyTableName","Society");
	}

	public  void saveCache(Context context)
	{
		SharedPreferences.Editor editor=context.getSharedPreferences("DbConstants",Context.MODE_PRIVATE).edit();

		editor.putString("DbName",DATABASE_NAME);
		editor.putInt("DbVersion", DATABASE_VERSION);
		editor.putString("EventTableName",EVENTS_TABLE_NAME);
		editor.putString("ExhibitionTableName",EXHIBITION_TABLE_NAME);
		editor.putString("NotificationTableName",NOTIFICATION_TABLE_NAME);
		editor.putString("InterestTableName",INTEREST_TABLE_NAME);
		editor.putString("CoordinatorTableName",COORDINATOR_TABLE_NAME);
		editor.putString("SocietyTableName",SOCIETY_TABLE_NAME);

		editor.apply();
	}

	public enum InterestNames
	{
		Id("ID"),
		Interest("_INTEREST"),
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

	public enum NotificationNames
	{
		EventName("EVENT_NAME"),
		EventID("EVENT_ID"),
		Notify("NOTIFY"),
		Date("_DATE"),
		Generated("GENERATED");

		private String Name;

		NotificationNames(String value)
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

	public enum ExhibitionNames
	{
		EventName("EVENT_NAME"),
		EventID("EVENT_ID"),
		Notify("NOTIFY"),
		Date("_DATE"),
		Venue("VENUE"),
		Description("_DESCRIPTION"),
		ImageUrl("IMAGE_URL"),
		Author("AUTHOR"),
		Pdf("PDF"),
		GTalk("GTALK");

		private String Name;

		ExhibitionNames(String value)
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

	public enum EventNames
	{
		EventName("EVENT_NAME"),
		EventID("EVENT_ID"),
		Notify("NOTIFY"),
		Date("_DATE"),
		Venue("VENUE"),
		Description("_DESCRIPTION"),
		ImageUrl("IMAGE_URL"),
		EndDate("END_DATE"),
		Rules("RULES"),
		MinUser("MIN_USER"),
		MaxUser("MAX_USER"),
		Pdf("PDF"),
		Registered("REGISTERED"),
		Society("SOCIETY"),
		Category("CATEGORY");

		private String Name;

		EventNames(String value)
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

	public enum CoordinatorNames
	{
		CoordinatorName("NAME"),
		EventID("EVENT_ID"),
		Email("EMAIL"),
		Mobile("MOBILE"),
		Designation("DESIGNATION");

		private String Name;

		CoordinatorNames(String value)
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

	public enum SocietyNames
	{
		Id("ID"),
		SocietyName("NAME");

		private String Name;

		SocietyNames(String value)
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
}
