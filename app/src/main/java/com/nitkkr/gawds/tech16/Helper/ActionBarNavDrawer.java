package com.nitkkr.gawds.tech16.Helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.Activity.SearchPage;

/**
 * Created by Home Laptop on 03-Nov-16.
 */

public class ActionBarNavDrawer
{
	public interface iActionBarNavDrawer
	{
		void NavButtonClicked();
		void SearchButtonClicked();
	}

	private iActionBarNavDrawer barNavDrawer;
	private AppCompatActivity activity;

	public ActionBarNavDrawer(final AppCompatActivity activity, iActionBarNavDrawer drawer)
	{
		this.activity = activity;
		barNavDrawer=drawer;

		activity.findViewById(R.id.actionbar_navButton).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				barNavDrawer.NavButtonClicked();
			}
		});
		activity.findViewById(R.id.actionbar_search).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				barNavDrawer.SearchButtonClicked();
				activity.startActivity(new Intent(activity, SearchPage.class));
			}
		});
	}

	public void setLabel(String label)
	{
		(( TextView)activity.findViewById(R.id.actionbar_title)).setText(label);
	}

}
