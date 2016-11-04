package com.nitkkr.gawds.tech16;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nitkkr.gawds.tech16.Helper.ActionBarNavDrawer;

public class Home extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		ActionBarNavDrawer barNavDrawer=new ActionBarNavDrawer(this, new ActionBarNavDrawer.iActionBarNavDrawer()
		{
			@Override
			public void NavButtonClicked()
			{

			}

			@Override
			public void SearchButtonClicked()
			{

			}
		});
		barNavDrawer.setLabel(getString(R.string.FestName));
	}
}
