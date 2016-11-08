package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.Adapter.RegisterTeamAdapter;
import com.nitkkr.gawds.tech16.Helper.ActionBarBack;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.Model.TeamModel;
import com.nitkkr.gawds.tech16.Model.UserModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

public class CreateTeam extends AppCompatActivity
{
	private RegisterTeamAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_team);

		Intent intent=getIntent();

		String EventName=intent.getStringExtra("Event_Name");
		boolean fixedTeam=intent.getBooleanExtra("Fixed_Team",true);
		int MinMembers=intent.getIntExtra("Min_Members",1);
		int MaxMembers=intent.getIntExtra("Max_Members",MinMembers);

		ActionBarBack actionBarBack=new ActionBarBack(CreateTeam.this);
		actionBarBack.setLabel("Create Team");

		//------------------------Return data.getBooleanExtra("Register",true)--------------------------------

		TeamModel model=new TeamModel();
		ArrayList<UserModel> userModels=new ArrayList<>();
		userModels.add(AppUserModel.MAIN_USER);
		model.setMembers(userModels);

		adapter=new RegisterTeamAdapter(CreateTeam.this,model,MinMembers,MaxMembers,true);

		ListView listView=(ListView)findViewById(R.id.team_register_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				if(i!=adapter.getTeamModel().getMembers().size())
				{
					Intent intent=new Intent(CreateTeam.this,ViewUser.class);
					//---------------------View User---------------------------------
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
}
