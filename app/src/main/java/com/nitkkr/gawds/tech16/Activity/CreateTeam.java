package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.Adapter.RegisterTeamAdapter;
import com.nitkkr.gawds.tech16.Database.Database;
import com.nitkkr.gawds.tech16.Helper.ActionBarBack;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.ResponseStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.Model.EventModel;
import com.nitkkr.gawds.tech16.Model.TeamModel;
import com.nitkkr.gawds.tech16.Model.UserModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

public class CreateTeam extends AppCompatActivity
{
	private RegisterTeamAdapter adapter;
	private EventModel eventModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_team);

		eventModel=( EventModel)getIntent().getExtras().getSerializable("Event");

		ActionBarBack actionBarBack=new ActionBarBack(CreateTeam.this);
		actionBarBack.setLabel("Create Team");

		TeamModel model=new TeamModel();
		ArrayList<UserModel> userModels=new ArrayList<>();
		userModels.add(AppUserModel.MAIN_USER);
		model.setMembers(userModels);

		adapter=new RegisterTeamAdapter(CreateTeam.this,model,eventModel.getMinUsers(),eventModel.getMaxUsers(),true);

		ListView listView=(ListView)findViewById(R.id.team_register_list);
		listView.setAdapter(adapter);
		//TODO---Change users left count on reg button
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				if(i!=adapter.getTeamModel().getMembers().size())
				{
					Intent intent=new Intent(CreateTeam.this,ViewUser.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("User",adapter.getTeamModel().getMembers().get(i));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode==RegisterTeamAdapter.SEARCH_USER)
		{
			if(resultCode==RESULT_OK)
			{
				UserModel userModel=new UserModel();
				//Get new User
				adapter.getTeamModel().getMembers().add(userModel);
				adapter.notifyDataSetInvalidated();
			}
		}
		else super.onActivityResult(requestCode, resultCode, data);
	}

	public void Register(View view)
	{
		ResponseStatus status = ResponseStatus.NONE;
		//TODO:--Register Team----------
		switch (status)
		{
			case FAILED:
				Toast.makeText(CreateTeam.this, "Failed, Please Try Again", Toast.LENGTH_LONG).show();
				break;
			case SUCCESS:
				Toast.makeText(CreateTeam.this, "Registered Successfully", Toast.LENGTH_LONG).show();
				Intent intent=new Intent();
				intent.putExtra("Register",true);
				setResult(RESULT_OK,intent);

				eventModel.setRegistered(true);
				Database.database.getEventsDB().addOrUpdateEvent(eventModel);
				Database.database.getNotificationDB().UpdateTable();
				eventModel.callStatusListener();

				finish();
				break;
			case OTHER:
				Toast.makeText(CreateTeam.this, "----------------------Message--------------------", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}
	}

	@Override
	public void onBackPressed()
	{
		if(ActivityHelper.revertToHomeIfLast(CreateTeam.this))
			return;
		super.onBackPressed();
	}
}
