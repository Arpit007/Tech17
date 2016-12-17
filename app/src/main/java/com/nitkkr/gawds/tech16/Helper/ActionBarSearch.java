package com.nitkkr.gawds.tech16.Helper;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;

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
				activity.findViewById(R.id.main_bar).setVisibility(View.GONE);

				SearchView searchView=(SearchView)activity.findViewById(R.id.search);
				searchView.setVisibility(View.VISIBLE);
				searchView.onActionViewExpanded();
			}
		});

		SearchView searchView=(SearchView)activity.findViewById(R.id.search);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
		{
			@Override
			public boolean onQueryTextSubmit(String query)
			{
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText)
			{
				barNavDrawer.SearchQuery(newText);
				return true;
			}
		});
	}

	public void setLabel(String label)
	{
		((TextView)activity.findViewById(R.id.actionbar_title)).setText(label);
	}

	public void setSearchHint(String hint)
	{
		SearchView searchView=(SearchView)activity.findViewById(R.id.search);
		searchView.setQueryHint(hint);
	}

	public boolean backPressed()
	{
		if(activity.findViewById(R.id.search).getVisibility()==View.VISIBLE)
		{
			activity.findViewById(R.id.search).setVisibility(View.GONE);
			activity.findViewById(R.id.main_bar).setVisibility(View.VISIBLE);
			SearchView searchView=(SearchView)activity.findViewById(R.id.search);
			searchView.setQuery("",false);
			return true;
		}
		else
		return false;
	}
}
