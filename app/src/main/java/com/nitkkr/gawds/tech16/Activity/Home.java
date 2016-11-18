package com.nitkkr.gawds.tech16.Activity;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.Helper.ActionBarNavDrawer;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.Src.CheckUpdate;

public class Home extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener
{
	private boolean Exit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		ActionBarNavDrawer barNavDrawer = new ActionBarNavDrawer(this, new ActionBarNavDrawer.iActionBarNavDrawer()
		{
			@Override
			public void NavButtonClicked()
			{
				drawer.openDrawer(GravityCompat.START);
			}

			@Override
			public void SearchButtonClicked()
			{

			}
		});
		barNavDrawer.setLabel(getString(R.string.FestName));

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		if(CheckUpdate.CHECK_UPDATE.isUpdateAvailable())
			CheckUpdate.CHECK_UPDATE.displayUpdate(Home.this);
	}

	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		}
		else
		{
			if (!Exit)
			{
				Exit = true;
				Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						Exit = false;
					}
				}, getResources().getInteger(R.integer.WarningDuration));
			}
			else
			{
				super.onBackPressed();
			}
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_home)
		{
			// Handle the camera action
		}
		else if (id == R.id.nav_events)
		{

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
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
