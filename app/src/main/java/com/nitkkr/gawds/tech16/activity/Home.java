package com.nitkkr.gawds.tech16.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.helper.ActionBarNavDrawer;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.iActionBar;
import com.nitkkr.gawds.tech16.model.AppUserModel;
import com.nitkkr.gawds.tech16.src.RateApp;
import com.nitkkr.gawds.tech16.src.UpdateCheck;

import static com.nitkkr.gawds.tech16.activity.Login.mGoogleApiClient;

public class Home extends AppCompatActivity implements View.OnClickListener
{
	private ActionBarNavDrawer barNavDrawer;
	private boolean Exit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if(getIntent().getBooleanExtra("AnimStart",true))
			ActivityHelper.setCreateAnimation(this);
		else overridePendingTransition(R.anim.anim_left_in,R.anim.anim_none);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

			ActivityHelper.setStatusBarColor(this);
		}
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


		if(UpdateCheck.getInstance().isUpdateAvailable())
		{
			if (!UpdateCheck.getInstance().displayUpdate(Home.this))
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
				ActivityHelper.setExitAnimation(this);
			}
		}
	}

	@Override
	public void onClick(View v)
	{
		int id=v.getId();
		DashboardPage.Page page = DashboardPage.Page.Live;
		switch (id)
		{
			case R.id.LiveEvents:
				page = DashboardPage.Page.Live;
				break;
			case R.id.NotificationEvents:
				page = DashboardPage.Page.Notification;
				break;
			case R.id.WishlistEvents:
				page = DashboardPage.Page.Wishlist;
				break;
			case R.id.InterestedEvents:
				page = DashboardPage.Page.Interest;
				break;
		}

		if(page== DashboardPage.Page.Notification)
		{
			ActivityHelper.comingSoonSnackBar("Feature Coming Soon",Home.this);
			return;
		}

		if(AppUserModel.MAIN_USER.isUserLoggedIn(Home.this) || page== DashboardPage.Page.Live)
		{
			Intent intent = new Intent(Home.this, DashboardPage.class);
			intent.putExtra("Navigation", page.value);
			startActivity(intent);
		}
		else
		{
			Snackbar.make(findViewById(android.R.id.content), "Login to Access this Feature", Snackbar.LENGTH_LONG)
				.setAction("Login", new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						AppUserModel.MAIN_USER.LoginUserNoHome(Home.this,false);
					}
				})
				.setActionTextColor(ContextCompat.getColor(Home.this,R.color.neon_green))
				.show();
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		boolean Skip=getSharedPreferences(getString(R.string.App_Preference), Context.MODE_PRIVATE).getBoolean("Skip",false);

		if(!Skip && Database.getInstance().getInterestDB().getSelectedInterests().size()<=0)
		{
			startActivity(new Intent(this,Interests.class));
			finish();
			ActivityHelper.setExitAnimation(this);
		}

	}
}
