package com.nitkkr.gawds.tech16.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.helper.ActionBarNavDrawer;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.iActionBar;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.src.CheckUpdate;
import com.nitkkr.gawds.tech16.src.RateApp;

public class Home extends AppCompatActivity implements View.OnClickListener
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


		ImageView live_imagView=(ImageView) findViewById(R.id.Live_events_imgview);
		ImageView interested_imgView=(ImageView) findViewById(R.id.interested_imgView);
		ImageView wishlist_imgView=(ImageView) findViewById(R.id.wishlist_imgview);
		ImageView notifications_imgView=(ImageView) findViewById(R.id.noti_imgview);

		live_imagView.setOnClickListener(this);
		interested_imgView.setOnClickListener(this);
		wishlist_imgView.setOnClickListener(this);
		notifications_imgView.setOnClickListener(this);


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

	@Override
	public void onClick(View v) {
		int id=v.getId();
		Bundle bundle=new Bundle();
		switch (id){
			case R.id.Live_events_imgview:
				bundle.putString("Event","Live_Event");
				break;
			case R.id.wishlist_imgview:
				bundle.putString("Event","Wishlist_Event");
				break;
			case R.id.interested_imgView:
				bundle.putString("Event","Interested_Event");
				break;
			case R.id.noti_imgview:
				bundle.putString("Event","Notification_Event");
				break;

		}

		Intent intent=new Intent(Home.this,Home_page.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
