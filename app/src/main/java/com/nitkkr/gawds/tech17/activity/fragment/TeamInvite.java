package com.nitkkr.gawds.tech17.activity.fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.adapter.TeamListAdapter;
import com.nitkkr.gawds.tech17.model.TeamKey;

import java.util.ArrayList;

public class TeamInvite extends Fragment
{
	private Context context;
	private ListView listView;
	private TeamListAdapter adapter;

	public static TeamInvite getFragment(Context context)
	{
		TeamInvite teamInvite=new TeamInvite();
		teamInvite.context=context;
		return teamInvite;
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
		final View view= inflater.inflate(R.layout.fragment_team_invite, container, false);

		listView=(ListView)view.findViewById(R.id.MyTeam_List);
		adapter = new TeamListAdapter(context,new ArrayList<TeamKey>());

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
			if(key.getControl()== TeamKey.TeamControl.Pending)
				models.add(key);
		}
		adapter.setModels(models);
	}
}