package com.nitkkr.gawds.tech16.Helper;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;

/**
 * Created by Home Laptop on 03-Nov-16.
 */

public class ActionBarNavDrawer
{

	private iActionBar barNavDrawer;
	private AppCompatActivity activity;

	public ActionBarNavDrawer(final AppCompatActivity activity, iActionBar drawer)
	{
		this.activity = activity;
		barNavDrawer=drawer;

		NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
		{
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item)
			{
				return ActivityHelper.onNavigationItemSelected(activity,item);
			}
		});

		activity.findViewById(R.id.actionbar_navButton).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				DrawerLayout drawerx = (DrawerLayout) activity.findViewById(R.id.drawer_layout);

				if(AppUserModel.MAIN_USER.isUserLoaded())
				{
					NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
					navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
					navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
				}
				else
				{
					NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
					navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
					navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
				}
				drawerx.openDrawer(GravityCompat.START);

				barNavDrawer.NavButtonClicked();
			}
		});
		activity.findViewById(R.id.actionbar_search).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				//------------------Handle-------------------------
				barNavDrawer.SearchButtonClicked();
				//activity.startActivity(new Intent(activity, SearchPage.class));
			}
		});
	}

	public boolean onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
			return true;
		}
		return false;
	}

	public void setLabel(String label)
	{
		(( TextView)activity.findViewById(R.id.actionbar_title)).setText(label);
	}

}
