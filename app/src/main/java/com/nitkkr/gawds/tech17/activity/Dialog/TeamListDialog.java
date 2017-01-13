package com.nitkkr.gawds.tech17.activity.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.adapter.TeamListAdapter;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.model.EventModel;
import com.nitkkr.gawds.tech17.model.TeamModel;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 06-Jan-17.
 */

public class TeamListDialog
{
	Dialog dialog;
	private ListView teamList;
	private TeamListAdapter adapter;

	public TeamListDialog(final Activity activity, EventModel model, ArrayList<TeamModel> teamModels)
	{
		dialog = new Dialog(activity);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.layout_dialog_team_list);

		(( TextView)dialog.findViewById(R.id.Event_Name)).setText(model.getEventName());
		(( TextView)dialog.findViewById(R.id.Interest_Name)).setText(Database.getInstance().getInterestDB().getInterest(model.getCategory()));

		teamList=(ListView)dialog.findViewById(R.id.Team_List);

		adapter = new TeamListAdapter(activity,teamModels);

		adapter.setResourceID(R.layout.layout_view_team_item);

		(( TextView)dialog.findViewById(R.id.Team_Count)).setText(adapter.getCount() + " Teams");

		teamList.setAdapter(adapter);

		dialog.getWindow().getAttributes().windowAnimations = R.style.CloseDialogTheme;

		dialog.findViewById(R.id.OK).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				dialog.dismiss();
			}
		});

		teamList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				TeamDialog teamDialog=new TeamDialog(activity,adapter.getModels().get(i),false);
				teamDialog.show();
			}
		});
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
}
