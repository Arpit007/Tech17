package com.nitkkr.gawds.tech17.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.CreateTeam;
import com.nitkkr.gawds.tech17.adapter.TeamListAdapter;
import com.nitkkr.gawds.tech17.model.TeamKey;

import java.util.ArrayList;

public class MyTeams extends Fragment
{
	private final int TEAM=600;
	private Context context;
	private ListView listView;
	private TeamListAdapter adapter;

	public static MyTeams getFragment(Context context)
	{
		MyTeams myTeams=new MyTeams();
		myTeams.context=context;
		return myTeams;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		final View view= inflater.inflate(R.layout.fragment_my_teams, container, false);

		listView=(ListView)view.findViewById(R.id.MyTeam_List);
		adapter = new TeamListAdapter(context,new ArrayList<TeamKey>());
		listView.setAdapter(adapter);

		adapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				super.onChanged();
				if(adapter.getCount()==0)
					view.findViewById(R.id.None).setVisibility(View.VISIBLE);
				else view.findViewById(R.id.None).setVisibility(View.GONE);
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				//No info provided from backend
			}
		});

		view.findViewById(R.id.Add_Team).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				startActivityForResult(new Intent(context, CreateTeam.class),TEAM);
			}
		});

		adapter.notifyDataSetChanged();

		return view;
	}

	public void setModels(ArrayList<TeamKey> keys)
	{
		ArrayList<TeamKey> models=new ArrayList<>();
		for(TeamKey key: keys)
		{
			if(key.getControl()== TeamKey.TeamControl.Participant || key.getControl()== TeamKey.TeamControl.Leader)
				models.add(key);
		}
		adapter.setModels(models);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
	}
}
