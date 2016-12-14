package com.nitkkr.gawds.techspardha17.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nitkkr.gawds.techspardha17.Activity.SearchPage;
import com.nitkkr.gawds.techspardha17.Helper.TeamMemberItemHolder;
import com.nitkkr.gawds.techspardha17.Model.TeamModel;
import com.nitkkr.gawds.techspardha17.R;


/**
 * Created by Home Laptop on 08-Nov-16.
 */
//TODO:Fix
public class RegisterTeamAdapter extends BaseAdapter
{
	private Activity activity;
	private TeamModel teamModel;
	private boolean FixedTeam;
	private int Min,Max;
	public static final int SEARCH_USER=100;
	private boolean showAddButton;

	public RegisterTeamAdapter(Activity activity, TeamModel teamModel, int MinMembers, int MaxMembers, boolean showAddButton)
	{
		this.showAddButton=showAddButton;
		this.activity=activity;
		this.teamModel=teamModel;
		Min = MinMembers;
		Max = MaxMembers;
		FixedTeam = (Min==Max);
	}

	public TeamModel getTeamModel(){return teamModel;}

	@Override
	public int getCount()
	{
		if(showAddButton)
			return teamModel.getMembers().size() + 1;
		else return teamModel.getMembers().size();
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
		final int x=i;
		if(view==null)
		{
			if(i==teamModel.getMembers().size())
			{
				LayoutInflater inflater=LayoutInflater.from(activity);
				view=inflater.inflate(R.layout.layout_list_item_team_member_add,viewGroup,false);
			}
			else
			{
				LayoutInflater inflater=LayoutInflater.from(activity);
				view=inflater.inflate(R.layout.layout_list_item_team_member,viewGroup,false);
			}
		}

		if(i==teamModel.getMembers().size())
		{
			view.findViewById(R.id.team_member_Add).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					Intent intent=new Intent(activity, SearchPage.class);
					//TODO:---------Work----------
					intent.putExtra("Data_Type","User");
					activity.startActivityForResult(intent,SEARCH_USER);
				}
			});
		}
		else
		{
			TeamMemberItemHolder holder=new TeamMemberItemHolder(view);
			holder.setName(teamModel.getMembers().get(i).getName());
			holder.setEmail(teamModel.getMembers().get(i).getEmail());
			holder.setCollege(teamModel.getMembers().get(i).getRoll()+" "+teamModel.getMembers().get(i).getCollege());
			if(i==0 || !showAddButton)
				holder.hideCloseButton();
			else holder.showCloseButton();
			holder.setCloseListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					teamModel.getMembers().remove(x);
					RegisterTeamAdapter.this.notifyDataSetInvalidated();
				}
			});
		}
		return view;
	}
}
