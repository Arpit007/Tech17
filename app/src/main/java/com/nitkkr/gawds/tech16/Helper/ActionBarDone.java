package com.nitkkr.gawds.tech16.Helper;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.R;

/**
 * Created by Home Laptop on 04-Nov-16.
 */

public class ActionBarDone
{
	private AppCompatActivity activity;

	public ActionBarDone(final AppCompatActivity activity, View.OnClickListener listener)
	{
		this.activity=activity;
		try
		{
			activity.findViewById(R.id.actionbar_DoneButton).setOnClickListener(listener);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setLabel(String label)
	{
		((TextView)activity.findViewById(R.id.actionbar_title)).setText(label);
	}
}
