package com.nitkkr.gawds.tech17.adapter;

import android.view.View;
import android.widget.TextView;

import com.nitkkr.gawds.tech17.R;

/**
 * Created by Home Laptop on 08-Nov-16.
 */

public class TeamMemberItemHolder
{
	private View view;

	public TeamMemberItemHolder(View view)
	{
		this.view = view;
	}

	public void showCloseButton()
	{
		view.findViewById(R.id.team_member_Close).setVisibility(View.VISIBLE);
	}

	public void hideCloseButton()
	{
		view.findViewById(R.id.team_member_Close).setVisibility(View.GONE);
	}

	public void setName(String Name)
	{
		( (TextView) view.findViewById(R.id.team_member_Name) ).setText(Name);
	}

	public void setEmail(String email)
	{
		( (TextView) view.findViewById(R.id.team_member_Email) ).setText(email);
	}

	public void setCollege(String college)
	{
		( (TextView) view.findViewById(R.id.team_member_College) ).setText(college);
	}

	public void setCloseListener(View.OnClickListener listener)
	{
		view.findViewById(R.id.team_member_Close).setOnClickListener(listener);
	}
}
