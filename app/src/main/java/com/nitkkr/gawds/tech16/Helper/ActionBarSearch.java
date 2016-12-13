package com.nitkkr.gawds.tech16.Helper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.Activity.SearchPage;
import com.nitkkr.gawds.tech16.R;

/**
 * Created by Home Laptop on 02-Dec-16.
 */

public class ActionBarSearch
{

	private iActionBar barNavDrawer;
	private AppCompatActivity activity;

	public ActionBarSearch(final AppCompatActivity activity, iActionBar drawer)
	{
		this.activity = activity;
		barNavDrawer=drawer;

		activity.findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				activity.onBackPressed();
			}
		});

		activity.findViewById(R.id.actionbar_search).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				barNavDrawer.SearchButtonClicked();
				//TODO:Handle
			}
		});
	}

	public void setLabel(String label)
	{
		((TextView)activity.findViewById(R.id.actionbar_title)).setText(label);
	}
}
