package com.nitkkr.gawds.tech17.activity.fragment;

import android.app.ProgressDialog;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.Dialog.TeamDialog;
import com.nitkkr.gawds.tech17.activity.TeamPage;
import com.nitkkr.gawds.tech17.adapter.TeamListAdapter;
import com.nitkkr.gawds.tech17.api.FetchData;
import com.nitkkr.gawds.tech17.api.iResponseCallback;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.model.EventModel;
import com.nitkkr.gawds.tech17.model.TeamModel;

public class TeamInvite extends Fragment
{
	private TeamPage teamPage;
	private ListView listView;
	private TeamListAdapter adapter;

	public static TeamInvite getFragment(TeamPage teamPage)
	{
		TeamInvite teamInvite=new TeamInvite();
		teamInvite.teamPage=teamPage;
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
		adapter = new TeamListAdapter(teamPage,Database.getInstance().getTeamDB().getAllTeamInvite());

		adapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				super.onChanged();
				if(adapter.getCount()==0)
				{
					view.findViewById(R.id.None).setVisibility(View.VISIBLE);
					teamPage.getAdapter().setFragmentTitle("Team Invites",1);
					teamPage.getAdapter().notifyDataSetChanged();
				}
				else
				{
					view.findViewById(R.id.None).setVisibility(View.GONE);
					teamPage.getAdapter().setFragmentTitle("Team Invites (" + adapter.getModels().size() + ")",1);
					teamPage.getAdapter().notifyDataSetChanged();
				}
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l)
			{
				final TeamDialog dialog=new TeamDialog(teamPage,adapter.getModels().get(i),true);
				dialog.setDeclineOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						final ProgressDialog progressDialog=new ProgressDialog(teamPage);
						progressDialog.setIndeterminate(true);
						progressDialog.setCancelable(false);
						progressDialog.setMessage("Please Wait");
						progressDialog.show();
						FetchData.getInstance().declineTeamInvite(ActivityHelper.getApplicationContext(), adapter.getModels().get(i).getTeamID(), new iResponseCallback()
						{
							@Override
							public void onResponse(ResponseStatus status)
							{
								if(status == ResponseStatus.SUCCESS)
								{
									Database.getInstance().getTeamDB().deleteInvite(adapter.getModels().get(i).getTeamID());
									adapter.setModels(Database.getInstance().getTeamDB().getAllTeamInvite());
									dialog.getDialog().getWindow().getAttributes().windowAnimations = R.style.SuccessCloseDialogTheme;
									dialog.dismiss();
									Toast.makeText(teamPage,"Invite Declined Successfully",Toast.LENGTH_LONG).show();
									adapter.setModels(Database.getInstance().getTeamDB().getAllTeamInvite());
								}
								else if(status ==ResponseStatus.FAILED)
								{
									Toast.makeText(teamPage,"Failed, Please Try Again",Toast.LENGTH_SHORT).show();
								}
								else if(status ==ResponseStatus.NONE)
								{
									Toast.makeText(teamPage,"No Network Connection",Toast.LENGTH_SHORT).show();
								}
								progressDialog.dismiss();
							}

							@Override
							public void onResponse(ResponseStatus status, Object object)
							{
								this.onResponse(status);
							}
						});
					}
				});
				dialog.setAcceptOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						final ProgressDialog progressDialog=new ProgressDialog(teamPage);
						progressDialog.setIndeterminate(true);
						progressDialog.setCancelable(false);
						progressDialog.setMessage("Please Wait");
						progressDialog.show();
						FetchData.getInstance().acceptTeamInvite(ActivityHelper.getApplicationContext(), adapter.getModels().get(i).getTeamID(), new iResponseCallback()
						{
							@Override
							public void onResponse(ResponseStatus status)
							{
								if(status == ResponseStatus.SUCCESS)
								{
									TeamModel myTeam=adapter.getModels().get(i);

									Database.getInstance().getTeamDB().addOrUpdateMyTeam(myTeam);
									Database.getInstance().getTeamDB().deleteInvite(myTeam.getTeamID());

									EventModel eventModel = Database.getInstance().getEventsDB().getEvent(myTeam.getEventID());
									eventModel.setRegistered(true);
									Database.getInstance().getEventsDB().addOrUpdateEvent(eventModel);

									adapter.setModels(Database.getInstance().getTeamDB().getAllTeamInvite());
									((MyTeams)teamPage.getAdapter().getItem(0)).getAdapter().setModels(Database.getInstance().getTeamDB().getAllMyTeams());
									dialog.getDialog().getWindow().getAttributes().windowAnimations = R.style.SuccessCloseDialogTheme;
									dialog.dismiss();
									Toast.makeText(teamPage,"Invite Accepted Successfully",Toast.LENGTH_LONG).show();
								}
								else if(status ==ResponseStatus.FAILED)
								{
									Toast.makeText(teamPage,"Failed, Please Try Again",Toast.LENGTH_SHORT).show();
								}
								else if(status ==ResponseStatus.NONE)
								{
									Toast.makeText(teamPage,"No Network Connection",Toast.LENGTH_SHORT).show();
								}
								progressDialog.dismiss();
							}

							@Override
							public void onResponse(ResponseStatus status, Object object)
							{
								this.onResponse(status);
							}
						});
					}
				});
				dialog.show();
			}
		});
		listView.setAdapter(adapter);

		adapter.notifyDataSetChanged();

		return view;
	}
}