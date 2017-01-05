package com.nitkkr.gawds.tech17.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.model.TeamKey;
import com.nitkkr.gawds.tech17.model.TeamModel;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 05-Jan-17.
 */

public class TeamListAdapter extends BaseAdapter
{
	private ArrayList<TeamKey> models;
	private Context context;

	public TeamListAdapter(Context context, ArrayList<TeamKey> models)
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
			view = inflater.inflate(R.layout.layout_team_item, null);
		}
		(( TextView)view.findViewById(R.id.team_name)).setText("Team: " + models.get(i).getTeamName());

		if(models.get(i).getControl()== TeamKey.TeamControl.Participant)
			view.findViewById(R.id.team_control).setVisibility(View.GONE);
		else
		{
			view.findViewById(R.id.team_control).setVisibility(View.VISIBLE);
			((TextView) view.findViewById(R.id.team_control)).setText(models.get(i).getControl().getValue());
		}
		return view;
	}

	public void setModels(ArrayList<TeamKey> models)
	{
		this.models=models;
		this.notifyDataSetChanged();
	}

	public ArrayList<TeamKey> getModels(){return models;}

}
