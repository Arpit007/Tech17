package com.nitkkr.gawds.tech17.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.fragment.MyTeams;
import com.nitkkr.gawds.tech17.activity.fragment.TeamInvite;
import com.nitkkr.gawds.tech17.adapter.ViewPagerAdapter;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.helper.ActionBarBack;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;

public class TeamPage extends AppCompatActivity
{
	private ViewPagerAdapter adapter;

	public ViewPagerAdapter getAdapter(){return adapter;}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_page);
		ActivityHelper.setCreateAnimation(this);
		ActivityHelper.setStatusBarColor(this);

		ActionBarBack barBack = new ActionBarBack(TeamPage.this);
		barBack.setLabel("Team Management");

		if(getIntent().getBooleanExtra("IsNotification",false) && isTaskRoot())
		{
			new Database(getApplicationContext());
			ActivityHelper.setUpHelper(getApplicationContext());
		}


		TabLayout tabLayout = (TabLayout) findViewById(R.id.team_list_tab);
		ViewPager viewPager = (ViewPager) findViewById(R.id.team_list_page_view);

		adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(MyTeams.getFragment(TeamPage.this), "My Teams");
		adapter.addFragment(TeamInvite.getFragment(TeamPage.this), "Team Invites");
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);

		viewPager.setCurrentItem(getIntent().getIntExtra("PageID",0));

	}

	@Override
	public void onBackPressed()
	{
		if(getIntent().getBooleanExtra("IsNotification",false) && isTaskRoot())
		{
			startActivity(new Intent(this,Splash.class));
		}
		else if(!ActivityHelper.revertToHomeIfLast(this))
			super.onBackPressed();
		ActivityHelper.setExitAnimation(this);
	}
}
