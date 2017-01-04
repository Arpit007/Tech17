package com.nitkkr.gawds.tech17.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.fragment.MyTeams;
import com.nitkkr.gawds.tech17.activity.fragment.TeamInvite;
import com.nitkkr.gawds.tech17.adapter.ViewPagerAdapter;
import com.nitkkr.gawds.tech17.api.FetchData;
import com.nitkkr.gawds.tech17.api.iResponseCallback;
import com.nitkkr.gawds.tech17.helper.ActionBarBack;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.model.TeamKey;

import java.util.ArrayList;

public class TeamPage extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_page);
		ActivityHelper.setCreateAnimation(this);
		ActivityHelper.setStatusBarColor(this);

		ActionBarBack barBack = new ActionBarBack(TeamPage.this);
		barBack.setLabel("Team Management");


		TabLayout tabLayout = (TabLayout) findViewById(R.id.team_list_tab);
		ViewPager viewPager = (ViewPager) findViewById(R.id.team_list_page_view);

		final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(MyTeams.getFragment(TeamPage.this), "My Teams");
		adapter.addFragment(TeamInvite.getFragment(TeamPage.this), "Team Invites");
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);



		final ProgressDialog dialog=new ProgressDialog(TeamPage.this);
		dialog.setMessage("Fetching...");
		dialog.setCancelable(false);
		dialog.setIndeterminate(true);
		dialog.show();

		FetchData.getInstance().getMyTeams(TeamPage.this, new iResponseCallback()
		{
			@Override
			public void onResponse(ResponseStatus status)
			{
				this.onResponse(status,null);
			}

			@Override
			public void onResponse(ResponseStatus status, Object object)
			{
				if(status==ResponseStatus.SUCCESS && object!=null)
				{
					((MyTeams)adapter.getItem(0)).setModels((ArrayList<TeamKey>)object);
					((TeamInvite)adapter.getItem(1)).setModels((ArrayList<TeamKey>)object);
				}
				else if (status== ResponseStatus.FAILED)
					Toast.makeText(TeamPage.this,"Failed To Fetch Team Information",Toast.LENGTH_LONG).show();
				else
					Toast.makeText(TeamPage.this,"No Network Connection",Toast.LENGTH_LONG).show();

				dialog.dismiss();
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		if (!ActivityHelper.revertToHomeIfLast(TeamPage.this))
		{
			super.onBackPressed();
		}
		ActivityHelper.setExitAnimation(this);
	}
}
