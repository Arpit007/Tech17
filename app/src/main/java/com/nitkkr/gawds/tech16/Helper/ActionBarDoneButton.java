package com.nitkkr.gawds.tech16.Helper;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.R;

/**
 * Created by Home Laptop on 04-Nov-16.
 */

public class ActionBarDoneButton
{
	private AppCompatActivity activity;

	public ActionBarDoneButton(final AppCompatActivity activity, View.OnClickListener listener)
	{
		this.activity=activity;
		try
		{
			activity.findViewById(R.id.actionbar_DoneButton).setOnClickListener(listener);
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
		((TextView)activity.findViewById(R.id.actionbar_title)).setText(label);
	}

	public void setButtonLabel(String label)
	{
		Button button = (Button)activity.findViewById(R.id.actionbar_DoneButton);
		button.setText(label);
	}

	public void setButtonDrawable(int ResourceID)
	{
		Button button = (Button)activity.findViewById(R.id.actionbar_DoneButton);
		button.setCompoundDrawables(ContextCompat.getDrawable(activity,ResourceID),null,null,null);
	}

	public void setButtonVisibility(int visibility)
	{
		Button button = (Button)activity.findViewById(R.id.actionbar_DoneButton);
		button.setVisibility(visibility);
	}
}
