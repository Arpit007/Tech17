package com.nitkkr.gawds.tech17.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.model.TeamModel;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 05-Jan-17.
 */

public class TeamListAdapter extends BaseAdapter
{
	private ArrayList<TeamModel> models;
	private Context context;
	private int ResourceID = R.layout.layout_team_item;

	public TeamListAdapter(Context context, ArrayList<TeamModel> models)
	{
		this.context=context;
		this.models=models;
	}

	@Override
	public int getCount()
	{
		return models.size();
	}

	@Override
	public Object getItem(int i)
	{
		return null;
	}

	@Override
	public long getItemId(int i)
	{
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup)
	{
		if (view == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(ResourceID, null);
		}
		(( TextView)view.findViewById(R.id.team_name)).setText("Team " + models.get(i).getTeamName());

		if(ResourceID==R.layout.layout_team_item)
		{
			( (TextView) view.findViewById(R.id.Event_Name) ).setText("Event: " + Database.getInstance().getEventsDB().getEventKey(models.get(i).getEventID()).getEventName());

			if (models.get(i).getControl() != TeamModel.TeamControl.Leader)
				view.findViewById(R.id.team_control).setVisibility(View.GONE);
			else
			{
				view.findViewById(R.id.team_control).setVisibility(View.VISIBLE);
				( (TextView) view.findViewById(R.id.team_control) ).setText(TeamModel.TeamControl.Leader.getValue());
			}
		}
		return view;
	}

	public void setModels(ArrayList<TeamModel> models)
	{
		this.models=models;
		this.notifyDataSetChanged();
	}

	public ArrayList<TeamModel> getModels(){return models;}

	public void setResourceID(int resourceID) {ResourceID = resourceID;}
}
