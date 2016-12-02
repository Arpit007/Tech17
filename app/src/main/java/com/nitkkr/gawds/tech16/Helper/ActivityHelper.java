package com.nitkkr.gawds.tech16.Helper;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.nitkkr.gawds.tech16.API.Query;
import com.nitkkr.gawds.tech16.Activity.About;
import com.nitkkr.gawds.tech16.Activity.EventList;
import com.nitkkr.gawds.tech16.Activity.Home;
import com.nitkkr.gawds.tech16.Activity.ListPage;
import com.nitkkr.gawds.tech16.Activity.Login;
import com.nitkkr.gawds.tech16.Activity.MusicalNight;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class ActivityHelper
{
	public static boolean onNavigationItemSelected(Activity activity, MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		Intent intent;

		if (id == R.id.nav_home)
		{
			if(activity instanceof Home)
				return true;

			intent=new Intent(activity,Home.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			activity.startActivity(intent);
		}
		else if (id == R.id.nav_events)
		{
			if(activity instanceof EventList)
				return true;

			intent=new Intent(activity,EventList.class);
			activity.startActivity(intent);
		}
		else if (id == R.id.nav_gusto_talks)
		{
			Query query=new Query("============Implement===========", Query.QueryType.SQl, Query.QueryTargetType.GuestTalk);
			startListActivity(activity,activity.getString(R.string.Guest_Talks),query);
		}
		else if (id == R.id.nav_informals)
		{
			Query query=new Query("============Implement===========", Query.QueryType.SQl, Query.QueryTargetType.Event);
			startListActivity(activity,activity.getString(R.string.Informals),query);
		}
		else if (id == R.id.nav_exhibitions)
		{
			Query query=new Query("============Implement===========", Query.QueryType.SQl, Query.QueryTargetType.Exhibition);
			startListActivity(activity,activity.getString(R.string.Exhibition),query);
		}
		else if (id == R.id.nav_musical_night)
		{
			intent=new Intent(activity, MusicalNight.class);
			activity.startActivity(intent);
		}
		else if (id == R.id.nav_About)
		{
			intent=new Intent(activity, About.class);
			activity.startActivity(intent);
		}
		else if (id == R.id.nav_admin)
		{
			//------------------------------DEPRECIATE--------------------------------------
		}
		else if (id == R.id.nav_logout)
		{
			AppUserModel.MAIN_USER.logoutUser(activity);
			intent=new Intent(activity,Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			activity.startActivity(intent);
		}
		else if(id==R.id.nav_login)
		{
			Intent intent1=new Intent(activity,Login.class);
			intent1.putExtra("Start_Home",false);
			activity.startActivity(intent1);
		}
		DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);

		return true;
	}

	public static boolean revertToHomeIfLast(Activity activity)
	{
		if(activity.isTaskRoot())
		{
			Intent intent=new Intent(activity,Home.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			activity.startActivity(intent);
			activity.finish();
			return true;
		}
		return false;
	}

	public static void startListActivity(Activity activity, String Label, Query query)
	{
		Intent intent=new Intent(activity, ListPage.class);
		intent.putExtra("Label",Label);
		intent.putExtra("Query", query);
		activity.startActivity(intent);
	}

}
