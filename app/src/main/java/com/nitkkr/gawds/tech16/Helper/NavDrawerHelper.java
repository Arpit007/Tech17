package com.nitkkr.gawds.tech16.Helper;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.nitkkr.gawds.tech16.Activity.EventList;
import com.nitkkr.gawds.tech16.Activity.Home;
import com.nitkkr.gawds.tech16.R;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class NavDrawerHelper
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

		}
		else if (id == R.id.nav_informals)
		{

		}
		else if (id == R.id.nav_exhibitions)
		{

		}
		else if (id == R.id.nav_musical_night)
		{

		}
		else if (id == R.id.nav_social_something)
		{

		}
		else if (id == R.id.nav_something)
		{

		}
		else if (id == R.id.nav_techspardha_live)
		{

		}
		else if (id == R.id.nav_admin)
		{

		}
		else if (id == R.id.nav_musical_night)
		{

		}
		DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
