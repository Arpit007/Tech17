package com.nitkkr.gawds.tech16.helper;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.R;

/**
 * Created by Home Laptop on 03-Nov-16.
 */

public class ActionBarBack
{
	//private AppCompatActivity activity;
	//private FragmentActivity fragmentActivity;
	private TextView actionBarTitle;

	public ActionBarBack(final AppCompatActivity activity)
	{
		//this.activity=activity;
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
			actionBarTitle = (TextView) activity.findViewById(R.id.actionbar_title);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public ActionBarBack(final FragmentActivity fragmentActivity) {
		//this.fragmentActivity = fragmentActivity;
		try {
			fragmentActivity.findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					fragmentActivity.onBackPressed();
				}
			});
			actionBarTitle = (TextView) fragmentActivity.findViewById(R.id.actionbar_title);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setLabel(String label)
	{
		actionBarTitle.setText(label);
	}
}
