package com.nitkkr.gawds.tech16.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.adapter.RegisterTeamAdapter;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.helper.ActionBarBack;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.ResponseStatus;
import com.nitkkr.gawds.tech16.model.EventKey;
import com.nitkkr.gawds.tech16.model.EventModel;
import com.nitkkr.gawds.tech16.model.TeamModel;

//TODO:Fix
public class ViewTeam extends AppCompatActivity
{
	EventModel model;
	TeamModel teamModel =new TeamModel();
	ActionBarBack barBack;
	RegisterTeamAdapter adapter;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_team);
		ActivityHelper.setCreateAnimation(this);
		ActivityHelper.setStatusBarColor(this);

		barBack=new ActionBarBack(ViewTeam.this);
		barBack.setLabel("");

		EventKey key=(EventKey) getIntent().getExtras().getSerializable("Event");
		model= Database.getInstance().getEventsDB().getEvent(key);

		progressDialog=new ProgressDialog(ViewTeam.this);
		progressDialog.setMessage("Loading, Please Wait");
		progressDialog.show();

		//TODO:Load Team Model------------------------
		ResponseStatus status=ResponseStatus.SUCCESS;

		switch (status)
		{
			case SUCCESS:
				loadTeam();
				adapter.notifyDataSetChanged();
				progressDialog.dismiss();
				break;
			case OTHER:
				Toast.makeText(ViewTeam.this,"==================MESSAGE==================",Toast.LENGTH_LONG).show();
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{

						finish();
						ActivityHelper.setExitAnimation(ViewTeam.this);
					}
				},getResources().getInteger(R.integer.AutoCloseDuration));
				break;
			default:
				Toast.makeText(ViewTeam.this,"Failed to Fetch Team Details",Toast.LENGTH_LONG).show();
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{

						finish();
						ActivityHelper.setExitAnimation(ViewTeam.this);
					}
				},getResources().getInteger(R.integer.AutoCloseDuration));
				break;
		}

		ListView listView=(ListView)findViewById(R.id.view_team_list);
		adapter=new RegisterTeamAdapter(ViewTeam.this, teamModel,model.getMinUsers(),model.getMaxUsers(),false);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				Intent intent=new Intent(ViewTeam.this,ViewUser.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("User", teamModel.getMembers().get(i));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	void loadTeam()
	{
		//TODO:Implement
		barBack.setLabel("Team: "+ teamModel.getTeamName());
	}

	@Override
	public void onBackPressed()
	{
		if(progressDialog!=null && progressDialog.isShowing())
			return;


		if(ActivityHelper.revertToHomeIfLast(ViewTeam.this));
			else super.onBackPressed();
		ActivityHelper.setExitAnimation(this);
	}
}
