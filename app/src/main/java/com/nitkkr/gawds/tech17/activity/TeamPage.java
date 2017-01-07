package com.nitkkr.gawds.tech17.activity;

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
import com.nitkkr.gawds.tech17.model.AppUserModel;
import com.nitkkr.gawds.tech17.model.TeamModel;
import com.nitkkr.gawds.tech17.model.UserKey;

import java.util.ArrayList;

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



		TeamModel model = new TeamModel();
		model.setTeamID(134);
		model.setTeamName("Atlantis");
		model.setControl(TeamModel.TeamControl.Leader);
		model.setEventID(5);
		ArrayList<UserKey> keys = new ArrayList<>();
		keys.add(AppUserModel.MAIN_USER);
		UserKey key=new UserKey();
		key.setName("Shivam Rampal");
		keys.add(key);
		key=new UserKey();
		key.setName("Akash Kalra");
		keys.add(key);
		key=new UserKey();
		key.setName("Bhawna Bhatnagar");
		keys.add(key);
		key=new UserKey();
		key.setName("Chaudhary Kalra");
		keys.add(key);
		key=new UserKey();
		key.setName("Devage Rampal");
		keys.add(key);
		key=new UserKey();
		key.setName("Ekta Kalra");
		keys.add(key);
		model.setMembers(keys);
		Database.getInstance().getTeamDB().addOrUpdateTeamInvite(model);

		model = new TeamModel();
		model.setTeamID(1324);
		model.setTeamName("Beasty");
		model.setControl(TeamModel.TeamControl.Leader);
		model.setEventID(6);
		keys = new ArrayList<>();
		keys.add(AppUserModel.MAIN_USER);
		key=new UserKey();
		key.setName("Shivam Rampal");
		keys.add(key);
		key=new UserKey();
		key.setName("Akash Kalra");
		keys.add(key);
		key=new UserKey();
		key.setName("Shivam Rampal");
		keys.add(key);
		key=new UserKey();
		key.setName("Akash Kalra");
		keys.add(key);
		key=new UserKey();
		key.setName("Shivam Rampal");
		keys.add(key);
		key=new UserKey();
		key.setName("Akash Kalra");
		keys.add(key);
		model.setMembers(keys);
		Database.getInstance().getTeamDB().addOrUpdateTeamInvite(model);


		TabLayout tabLayout = (TabLayout) findViewById(R.id.team_list_tab);
		ViewPager viewPager = (ViewPager) findViewById(R.id.team_list_page_view);

		adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(MyTeams.getFragment(TeamPage.this), "My Teams");
		adapter.addFragment(TeamInvite.getFragment(TeamPage.this), "Team Invites");
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);



		/*final ProgressDialog dialog=new ProgressDialog(TeamPage.this);
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
					((MyTeams)adapter.getItem(0)).setModels((ArrayList<TeamModel>)object);
					((TeamInvite)adapter.getItem(1)).setModels((ArrayList<TeamModel>)object);
				}
				else if (status== ResponseStatus.FAILED)
					Toast.makeText(TeamPage.this,"Failed To Fetch Team Information",Toast.LENGTH_LONG).show();
				else
					Toast.makeText(TeamPage.this,"No Network Connection",Toast.LENGTH_LONG).show();

				dialog.dismiss();
			}
		});*/
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
