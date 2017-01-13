package com.nitkkr.gawds.tech17.activity.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.Event;
import com.nitkkr.gawds.tech17.activity.NotificationPage;
import com.nitkkr.gawds.tech17.adapter.UserListAdapter;
import com.nitkkr.gawds.tech17.api.FetchData;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.model.TeamModel;

/**
 * Created by Home Laptop on 06-Jan-17.
 */

public class TeamDialog
{
	Dialog dialog;
	private TeamModel model;
	private ListView userList;
	private UserListAdapter adapter;
	private Activity activity;

	public TeamDialog(final Activity activity, final TeamModel model, boolean isInvite)
	{
		this.activity=activity;
		this.model = model;
		dialog = new Dialog(activity);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.layout_dialog_team);

		if (isInvite)
		{
			( (TextView) dialog.findViewById(R.id.Team_Accept) ).setText("Accept");
			dialog.findViewById(R.id.Team_Accept).setVisibility(View.VISIBLE);
			dialog.findViewById(R.id.Team_Decline).setVisibility(View.VISIBLE);
			dialog.findViewById(R.id.Team_Later).setVisibility(View.VISIBLE);
		}
		else
		{
			( (TextView) dialog.findViewById(R.id.Team_Later) ).setText("OK");
			dialog.findViewById(R.id.Team_Accept).setVisibility(View.GONE);

			if(!(activity instanceof Event))
			{
				dialog.findViewById(R.id.Team_Decline).setVisibility(View.VISIBLE);
				( (TextView) dialog.findViewById(R.id.Team_Decline) ).setText("View Event");
				dialog.findViewById(R.id.Team_Decline).setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						Bundle bundle = new Bundle();
						bundle.putSerializable("Event", Database.getInstance().getEventsDB().getEventKey(model.getEventID()));
						Intent intent = new Intent(activity, Event.class);
						intent.putExtras(bundle);
						dialog.dismiss();
						view.getContext().startActivity(intent);
					}
				});
			}
			else dialog.findViewById(R.id.Team_Decline).setVisibility(View.GONE);

			dialog.findViewById(R.id.Team_Later).setVisibility(View.VISIBLE);
		}
		setUpContent(activity);
	}

	private void setUpContent(Context context)
	{
		(( TextView)dialog.findViewById(R.id.Team_Name)).setText(model.getTeamName());
		(( TextView)dialog.findViewById(R.id.Event_Name)).setText(Database.getInstance().getEventsDB().getEventKey(model.getEventID()).getEventName());
		(( TextView)dialog.findViewById(R.id.Team_Members_Count)).setText(model.getMembers().size() + " Members");

		dialog.findViewById(R.id.Team_Later).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				dialog.dismiss();
			}
		});

		userList=(ListView)dialog.findViewById(R.id.User_List);

		adapter = new UserListAdapter(model.getMembers(),context,false,R.layout.layout_view_user_item);
		adapter.setShowStatus(true);

		userList.setAdapter(adapter);

		adapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				super.onChanged();
				(( TextView)dialog.findViewById(R.id.Team_Members_Count)).setText(TeamDialog.this.model.getMembers().size() + " Members");
			}
		});

		dialog.getWindow().getAttributes().windowAnimations = R.style.CloseDialogTheme;
	}

	public TeamDialog setAcceptOnClickListener(View.OnClickListener listener)
	{
		dialog.findViewById(R.id.Team_Accept).setOnClickListener(listener);
		return this;
	}

	public TeamDialog setDeclineOnClickListener(View.OnClickListener listener)
	{
		dialog.findViewById(R.id.Team_Decline).setOnClickListener(listener);
		return this;
	}

	public void show()
	{
		if(dialog!=null)
			dialog.show();
	}

	public void dismiss()
	{
		if(dialog!=null)
			dialog.dismiss();
	}

	public Dialog getDialog() {return dialog;}
}
