package com.nitkkr.gawds.tech16.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.helper.ActionBarNavDrawer;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.iActionBar;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.src.CheckUpdate;
import com.nitkkr.gawds.tech16.src.RateApp;

public class Home extends AppCompatActivity
{
	private ActionBarNavDrawer barNavDrawer;
	private boolean Exit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		ActivityHelper.setStatusBarColor(this);

		barNavDrawer = new ActionBarNavDrawer(this, new iActionBar()
		{
			@Override
			public void NavButtonClicked()
			{
			}

			@Override
			public void SearchQuery(String Query)
			{
			}
		},R.id.nav_home);
		barNavDrawer.setLabel(getString(R.string.FestName));
		barNavDrawer.setOpenNewSearchPage(true);


		if(CheckUpdate.getInstance().isUpdateAvailable())
		{
			if (!CheckUpdate.getInstance().displayUpdate(Home.this))
				if (RateApp.getInstance().isReadyForRating(Home.this))
					RateApp.getInstance().displayRating(Home.this);
		}
		else if (RateApp.getInstance().isReadyForRating(Home.this))
				RateApp.getInstance().displayRating(Home.this);

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
