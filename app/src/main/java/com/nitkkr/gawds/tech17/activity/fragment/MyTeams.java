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
import com.nitkkr.gawds.tech17.activity.Dialog.TeamDialog;
import com.nitkkr.gawds.tech17.adapter.TeamListAdapter;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.model.TeamModel;

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
		adapter = new TeamListAdapter(context, Database.getInstance().getTeamDB().getAllMyTeams());
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
				TeamDialog dialog=new TeamDialog(context,adapter.getModels().get(i),false);
				dialog.show();
			}
		});

		adapter.notifyDataSetChanged();

		return view;
	}

	public TeamListAdapter getAdapter() {return adapter;}
}
