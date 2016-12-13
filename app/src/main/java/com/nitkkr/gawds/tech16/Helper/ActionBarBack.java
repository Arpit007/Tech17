package com.nitkkr.gawds.tech16.Helper;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.R;

/**
 * Created by Home Laptop on 03-Nov-16.
 */

public class ActionBarBack
{
	private AppCompatActivity activity;

	public ActionBarBack(final AppCompatActivity activity)
	{
		this.activity=activity;
		try
		{
			activity.findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					activity.onBackPressed();
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setLabel(String label)
	{
		(( TextView)activity.findViewById(R.id.actionbar_title)).setText(label);
	}
}
