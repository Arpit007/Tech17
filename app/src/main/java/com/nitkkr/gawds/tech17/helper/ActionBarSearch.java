package com.nitkkr.gawds.tech17.helper;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;

import com.nitkkr.gawds.tech17.R;

/**
 * Created by Home Laptop on 02-Dec-16.
 */

public class ActionBarSearch
{

	private iActionBar callback;
	private AppCompatActivity activity;
	private boolean resetOnBack=true;

	public ActionBarSearch(final AppCompatActivity activity, iActionBar callback)
	{
		this.activity = activity;
		this.callback = callback;

		activity.findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				activity.onBackPressed();
			}
		});

		final SearchView searchView = (SearchView) activity.findViewById(R.id.search);
		searchView.setIconifiedByDefault(true);

		activity.findViewById(R.id.actionbar_search).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				activity.findViewById(R.id.main_bar).setVisibility(View.GONE);

				searchView.setVisibility(View.VISIBLE);
				searchView.onActionViewExpanded();
			}
		});

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
				ActionBarSearch.this.callback.SearchQuery(newText.trim());
				return true;
			}
		});
	}

	public void setLabel(String label)
	{
		( (TextView) activity.findViewById(R.id.actionbar_title) ).setText(label);
	}

	public void setSearchHint(String hint)
	{
		SearchView searchView = (SearchView) activity.findViewById(R.id.search);
		searchView.setQueryHint(hint);
	}

	public boolean backPressed()
	{
		SearchView searchView = (SearchView) activity.findViewById(R.id.search);
		if (searchView.getVisibility() == View.VISIBLE)
		{
			activity.findViewById(R.id.main_bar).setVisibility(View.VISIBLE);
			searchView.setVisibility(View.GONE);
			if(resetOnBack)
				searchView.setQuery("", false);
			return false;
		}
		else
		{
			return true;
		}
	}

	public void setResetOnBack(boolean resetOnBack){this.resetOnBack=resetOnBack;}

	public void setSearchButtonVisibility(int visibility)
	{
		activity.findViewById(R.id.actionbar_search).setVisibility(visibility);
	}
}
