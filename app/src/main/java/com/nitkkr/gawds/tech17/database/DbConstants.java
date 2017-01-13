package com.nitkkr.gawds.tech17.database;

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
	private String MYTEAM_TABLE_NAME;
	private String TEAM_INVITE_TABLE_NAME;
	private int DATABASE_VERSION = 1;

	public String getDatabaseName()
	{
		return DATABASE_NAME;
	}

	public String getEventsTableName()
	{
		return EVENTS_TABLE_NAME;
	}

	public String getExhibitionTableName()
	{
		return EXHIBITION_TABLE_NAME;
	}

	public String getNotificationTableName()
	{
		return NOTIFICATION_TABLE_NAME;
	}

	public String getInterestTableName()
	{
		return INTEREST_TABLE_NAME;
	}

	public String getCoordinatorTableName()
	{
		return COORDINATOR_TABLE_NAME;
	}

	public String getSocietyTableName()
	{
		return SOCIETY_TABLE_NAME;
	}

	public String getMyTeamTableName(){return MYTEAM_TABLE_NAME;}

	public String getTeamInviteTableName(){return TEAM_INVITE_TABLE_NAME;}

	public int getDatabaseVersion()
	{
		return DATABASE_VERSION;
	}

	public void setDatabaseName(String databaseName, Context context)
	{
		DATABASE_NAME = databaseName;
		saveCache(context);
	}

	public void setEventsTableName(String eventsTableName, Context context)
	{
		EVENTS_TABLE_NAME = eventsTableName;
		saveCache(context);
	}

	public void setExhibitionTableName(String exhibitionTableName, Context context)
	{
		EXHIBITION_TABLE_NAME = exhibitionTableName;
		saveCache(context);
	}

	public void setNotificationTableName(String notificationTableName, Context context)
	{
		NOTIFICATION_TABLE_NAME = notificationTableName;
		saveCache(context);
	}

	public void setInterestTableName(String interestTableName, Context context)
	{
		INTEREST_TABLE_NAME = interestTableName;
		saveCache(context);
	}

	public void setCoordinatorTableName(String coordinatorTableName, Context context)
	{
		COORDINATOR_TABLE_NAME = coordinatorTableName;
		saveCache(context);
	}

	public void setSocietyTableName(String societyTableName, Context context)
	{
		SOCIETY_TABLE_NAME = societyTableName;
		saveCache(context);
	}

	public void setMyTeamTableName(String myTeamTableName, Context context)
	{
		MYTEAM_TABLE_NAME = myTeamTableName;
		saveCache(context);
	}

	public void setTeamInviteTableName(String teamInviteTableName, Context context)
	{
		TEAM_INVITE_TABLE_NAME = teamInviteTableName;
		saveCache(context);
	}

	public void setDatabaseVersion(int databaseVersion, Context context)
	{
		DATABASE_VERSION = databaseVersion;
		saveCache(context);
	}

	public static DbConstants Constants = new DbConstants();

	private DbConstants()
	{
		DATABASE_NAME = "Tech17_1.db";
		DATABASE_VERSION = 1;
		EVENTS_TABLE_NAME = "EventList";
		EXHIBITION_TABLE_NAME = "Exhibition";
		NOTIFICATION_TABLE_NAME = "NotificationList";
		INTEREST_TABLE_NAME = "Interest";
		COORDINATOR_TABLE_NAME = "Coordinator";
		SOCIETY_TABLE_NAME = "Society";
		MYTEAM_TABLE_NAME = "MyTeamList";
		TEAM_INVITE_TABLE_NAME = "TeamInviteList";
	}

	public DbConstants(Context context)
	{
		this();
		//loadCache(context);
	}

	public void loadCache(Context context)
	{
		SharedPreferences preferences = context.getSharedPreferences("DbConstants", Context.MODE_PRIVATE);

		DATABASE_NAME = preferences.getString("DbName", "Tech17_1.db");
		DATABASE_VERSION = preferences.getInt("DbVersion", 1);
		EVENTS_TABLE_NAME = preferences.getString("EventTableName", "EventList");
		EXHIBITION_TABLE_NAME = preferences.getString("ExhibitionTableName", "Exhibition");
		NOTIFICATION_TABLE_NAME = preferences.getString("NotificationTableName", "NotificationList");
		INTEREST_TABLE_NAME = preferences.getString("InterestTableName", "Interest");
		COORDINATOR_TABLE_NAME = preferences.getString("CoordinatorTableName", "Coordinator");
		SOCIETY_TABLE_NAME = preferences.getString("SocietyTableName", "Society");
		MYTEAM_TABLE_NAME = preferences.getString("MyTeamTableName","MyTeamList");
		TEAM_INVITE_TABLE_NAME = preferences.getString("TeamInviteTableName","TeamInviteList");
	}

	public void saveCache(Context context)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences("DbConstants", Context.MODE_PRIVATE).edit();

		editor.putString("DbName", DATABASE_NAME);
		editor.putInt("DbVersion", DATABASE_VERSION);
		editor.putString("EventTableName", EVENTS_TABLE_NAME);
		editor.putString("ExhibitionTableName", EXHIBITION_TABLE_NAME);
		editor.putString("NotificationTableName", NOTIFICATION_TABLE_NAME);
		editor.putString("InterestTableName", INTEREST_TABLE_NAME);
		editor.putString("CoordinatorTableName", COORDINATOR_TABLE_NAME);
		editor.putString("SocietyTableName", SOCIETY_TABLE_NAME);
		editor.putString("MyTeamTableName",MYTEAM_TABLE_NAME);
		editor.putString("TeamInviteTableName",TEAM_INVITE_TABLE_NAME);

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
		NotificationID("NOTIFICATION_ID"),
		EventID("EVENT_ID"),
		Title("TITLE"),
		Message("MESSAGE"),
		Seen("NOTIFICATION_SEEN"),
		Updated("UPDATED");


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
		Category("CATEGORY"),
		Informal("INFORMAL");

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

	public enum TeamNames
	{
		TeamID("TEAM_ID"),
		EventID("EVENT_ID"),
		TeamName("TEAM_NAME"),
		Participants("PARTICIPANTS"),
		Control("CONTROL");

		private String Name;

		TeamNames(String value)
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
		SocietyName("NAME"),
		Description("DESCRIPTION");

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
