package com.nitkkr.gawds.tech16.Activity;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.Helper.ActionBarNavDrawer;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.iActionBar;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.Src.CheckUpdate;
import com.nitkkr.gawds.tech16.Src.RateApp;

public class Home extends AppCompatActivity
{
	private ActionBarNavDrawer barNavDrawer;
	private boolean Exit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		barNavDrawer = new ActionBarNavDrawer(this, new iActionBar()
		{
			@Override
			public void NavButtonClicked()
			{

			}

			@Override
			public void SearchButtonClicked()
			{

			}
		},R.id.nav_home);
		barNavDrawer.setLabel(getString(R.string.FestName));

		if(CheckUpdate.CHECK_UPDATE.isUpdateAvailable() && CheckUpdate.CHECK_UPDATE.displayUpdate(Home.this));
		else if(RateApp.rateApp.isReadyForRating(Home.this))
					RateApp.rateApp.displayRating(Home.this);



	}

	@Override
	public void onBackPressed()
	{
		if(!barNavDrawer.onBackPressed())
		{
			if (!Exit)
			{
				Exit = true;
				Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						Exit = false;
					}
				}, getResources().getInteger(R.integer.WarningDuration));
			}
			else
			{
				super.onBackPressed();
			}
		}
	}
}
