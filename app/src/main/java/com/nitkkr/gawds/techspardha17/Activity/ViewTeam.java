package com.nitkkr.gawds.techspardha17.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.techspardha17.Adapter.RegisterTeamAdapter;
import com.nitkkr.gawds.techspardha17.Helper.ActionBarBack;
import com.nitkkr.gawds.techspardha17.Helper.ActivityHelper;
import com.nitkkr.gawds.techspardha17.Model.EventModel;
import com.nitkkr.gawds.techspardha17.Model.TeamModel;
import com.nitkkr.gawds.techspardha17.Model.UserModel;
import com.nitkkr.gawds.techspardha17.R;

import java.util.ArrayList;
//TODO:Fix
public class ViewTeam extends AppCompatActivity
{
	TeamModel model;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_team);

		model=new TeamModel();
		model.setMembers(new ArrayList<UserModel>());

		//--------------------Load Team Model------------------------

		ActionBarBack barBack=new ActionBarBack(ViewTeam.this);
		barBack.setLabel("Team: "+model.getTeamName());

		EventModel eventModel=(EventModel)getIntent().getExtras().getSerializable("Event");

		ListView listView=(ListView)findViewById(R.id.view_team_list);
		RegisterTeamAdapter adapter=new RegisterTeamAdapter(ViewTeam.this,model,eventModel.getMinUsers(),eventModel.getMaxUsers(),false);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				Intent intent=new Intent(ViewTeam.this,ViewUser.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("User",model.getMembers().get(i));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}


	@Override
	public void onBackPressed()
	{
		if(ActivityHelper.revertToHomeIfLast(ViewTeam.this))
			return;
		super.onBackPressed();
	}
}
