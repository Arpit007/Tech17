package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.Adapter.RegisterTeamAdapter;
import com.nitkkr.gawds.tech16.Helper.ActionBarBack;
import com.nitkkr.gawds.tech16.Model.TeamModel;
import com.nitkkr.gawds.tech16.Model.UserModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

public class ViewTeam extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_team);

		TeamModel model=new TeamModel();
		model.setMembers(new ArrayList<UserModel>());

		//--------------------Load Team Model------------------------

		ActionBarBack barBack=new ActionBarBack(ViewTeam.this);
		barBack.setLabel("Team: "+model.getTeamName());

		Intent intent=getIntent();
		boolean fixedTeam=intent.getBooleanExtra("Fixed_Team",true);
		int MinMembers=intent.getIntExtra("Min_Members",1);
		int MaxMembers=intent.getIntExtra("Max_Members",MinMembers);

		ListView listView=(ListView)findViewById(R.id.view_team_list);
		RegisterTeamAdapter adapter=new RegisterTeamAdapter(ViewTeam.this,model,MinMembers,MaxMembers,false);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				Intent intent=new Intent(ViewTeam.this,ViewUser.class);
				//---------------------View User---------------------------------
				startActivity(intent);
			}
		});
	}
}
