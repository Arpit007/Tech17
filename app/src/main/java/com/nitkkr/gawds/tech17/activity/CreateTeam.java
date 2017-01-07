package com.nitkkr.gawds.tech17.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.adapter.RegisterTeamAdapter;
import com.nitkkr.gawds.tech17.adapter.UserListAdapter;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.helper.ActionBarBack;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.model.AppUserModel;
import com.nitkkr.gawds.tech17.model.EventKey;
import com.nitkkr.gawds.tech17.model.EventModel;
import com.nitkkr.gawds.tech17.model.TeamModel;
import com.nitkkr.gawds.tech17.model.UserKey;
import com.nitkkr.gawds.tech17.model.UserModel;

import java.util.ArrayList;

public class CreateTeam extends AppCompatActivity
{
	private EventModel eventModel;
	private TeamModel teamModel;
	private final int GET_USER=101;
	private UserListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_team);
		ActivityHelper.setCreateAnimation(this);
		ActivityHelper.setStatusBarColor(this);

		EventKey key = (EventKey) getIntent().getSerializableExtra("Event");
		eventModel = Database.getInstance().getEventsDB().getEvent(key);

		ActionBarBack actionBarBack = new ActionBarBack(CreateTeam.this);
		actionBarBack.setLabel("Create Team");

		teamModel=new TeamModel();
		teamModel.getMembers().add(AppUserModel.MAIN_USER);

		(( TextView)findViewById(R.id.Event_Name)).setText(eventModel.getEventName());
		((TextView)findViewById(R.id.Team_Members_Count)).setText("Team ("+eventModel.getMinUsers()+"-"+eventModel.getMaxUsers()+") Members");

		ListView listView = (ListView)findViewById(R.id.User_List);
		adapter = new UserListAdapter(teamModel.getMembers(),CreateTeam.this,true, R.layout.layout_create_user_item);
		adapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				super.onChanged();
				teamModel.setMembers(adapter.getUsers());
				if(adapter.getUsers().size()==eventModel.getMaxUsers())
					findViewById(R.id.Add_Member).setVisibility(View.INVISIBLE);
				else findViewById(R.id.Add_Member).setVisibility(View.VISIBLE);
			}
		});
		listView.setAdapter(adapter);


		findViewById(R.id.Add_Member).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				startActivityForResult(new Intent(CreateTeam.this,UserSearch.class),GET_USER);
			}
		});

		findViewById(R.id.Team_Register).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == GET_USER)
		{
			if (resultCode == RESULT_OK)
			{
				adapter.getUsers().add(( UserKey)data.getExtras().getSerializable("User"));
				adapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void Register(View view)
	{/*
		if (!ActivityHelper.isInternetConnected())
		{
			Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();
			return;
		}
		ResponseStatus status = ResponseStatus.NONE;
		//TODO:--Register Team----------
		switch (status)
		{
			case FAILED:
				Toast.makeText(CreateTeam.this, "Failed, Please Try Again", Toast.LENGTH_LONG).show();
				break;
			case SUCCESS:
				Toast.makeText(CreateTeam.this, "Registered Successfully", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.putExtra("Register", true);
				setResult(RESULT_OK, intent);

				eventModel.setNotify(true);
				eventModel.setRegistered(true);
				Database.getInstance().getEventsDB().addOrUpdateEvent(eventModel);
				eventModel.callStatusListener();
				finish();
				ActivityHelper.setExitAnimation(this);
				break;
			default:
				Toast.makeText(CreateTeam.this, "----------------------Message--------------------", Toast.LENGTH_LONG).show();
				break;
		}*/
	}

	@Override
	public void onBackPressed()
	{
		if (!ActivityHelper.revertToHomeIfLast(CreateTeam.this))
		{
			super.onBackPressed();
		}
		ActivityHelper.setExitAnimation(this);
	}
}
