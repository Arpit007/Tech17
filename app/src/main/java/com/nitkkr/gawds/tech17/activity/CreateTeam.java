package com.nitkkr.gawds.tech17.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.adapter.UserListAdapter;
import com.nitkkr.gawds.tech17.api.FetchData;
import com.nitkkr.gawds.tech17.api.iResponseCallback;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.helper.ActionBarBack;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.model.AppUserModel;
import com.nitkkr.gawds.tech17.model.EventKey;
import com.nitkkr.gawds.tech17.model.EventModel;
import com.nitkkr.gawds.tech17.model.TeamModel;
import com.nitkkr.gawds.tech17.model.UserKey;

public class CreateTeam extends AppCompatActivity
{
	private EventModel eventModel;
	private TeamModel teamModel;
	private final int GET_USER=101;
	private UserListAdapter adapter;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_team);
		ActivityHelper.setCreateAnimation(this);
		ActivityHelper.setStatusBarColor(this);

		EventKey key = (EventKey) getIntent().getSerializableExtra("Event");
		eventModel = Database.getInstance().getEventsDB().getEvent(key);

		ActionBarBack actionBarBack = new ActionBarBack(CreateTeam.this);
		actionBarBack.setLabel("Create Team");

		teamModel=new TeamModel();
		teamModel.getMembers().add(AppUserModel.MAIN_USER);
		teamModel.getMembers().get(0).setTeamControl(TeamModel.TeamControl.Leader);

		(( TextView)findViewById(R.id.Event_Name)).setText(eventModel.getEventName());
		((TextView)findViewById(R.id.Team_Members_Count)).setText("Team ("+eventModel.getMinUsers()+"-"+eventModel.getMaxUsers()+") Members");

		ListView listView = (ListView)findViewById(R.id.User_List);
		adapter = new UserListAdapter(teamModel.getMembers(),CreateTeam.this,true, R.layout.layout_create_user_item);
		adapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				super.onChanged();
				teamModel.setMembers(adapter.getUsers());
				if(adapter.getUsers().size()==eventModel.getMaxUsers())
					findViewById(R.id.Add_Member).setVisibility(View.INVISIBLE);
				else findViewById(R.id.Add_Member).setVisibility(View.VISIBLE);
			}
		});
		listView.setAdapter(adapter);

		dialog = new Dialog(this);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.layout_dialog_team_name);
		dialog.getWindow().getAttributes().windowAnimations = R.style.CloseDialogTheme;
		dialog.findViewById(R.id.CheckAvailability).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				final String Name=(( EditText)dialog.findViewById(R.id.Team_Name)).getText().toString();
				if(Name.equals(""))
				{
					((TextView)findViewById(R.id.Team_Name)).setText("Team Name");
				}
				else
				{
					((TextView)findViewById(R.id.Team_Name)).setText(Name);
				}
				dialog.dismiss();
			}
		});

		findViewById(R.id.Edit_Team_Name).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(dialog!=null)
				{
					if(((TextView)findViewById(R.id.Team_Name)).getText().toString().toLowerCase().equals("team name"))
						((EditText) dialog.findViewById(R.id.Team_Name)).setText("");
					dialog.show();
					dialog.findViewById(R.id.Team_Name).requestFocus();
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(dialog.findViewById(R.id.Team_Name), InputMethodManager.SHOW_IMPLICIT);
				}
			}
		});

		findViewById(R.id.Add_Member).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				startActivityForResult(new Intent(CreateTeam.this,UserSearch.class),GET_USER);
			}
		});

		findViewById(R.id.Team_Register).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(((TextView)findViewById(R.id.Team_Name)).getText().toString().toLowerCase().equals("team name"))
				{
					Toast.makeText(CreateTeam.this,"Set Team Name",Toast.LENGTH_SHORT).show();
					return;
				}
				if(adapter.getUsers().size()<eventModel.getMinUsers())
				{
					Toast.makeText(CreateTeam.this,"Set Minimum Users",Toast.LENGTH_SHORT).show();
					return;
				}
				final String Name=((TextView)findViewById(R.id.Team_Name)).getText().toString();

				final ProgressDialog progressDialog=new ProgressDialog(CreateTeam.this);
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(false);
				progressDialog.setMessage("Registering, Please Wait");
				progressDialog.show();

				FetchData.getInstance().createTeam(getApplicationContext(), Name, eventModel.getEventID(), new iResponseCallback()
				{
					@Override
					public void onResponse(ResponseStatus status)
					{
						this.onResponse(status,0);
					}

					@Override
					public void onResponse(ResponseStatus status, Object object)
					{
						if(status ==ResponseStatus.SUCCESS && ((int)object)!=0)
						{
							final TeamModel model=new TeamModel();
							model.setTeamID((int)object);
							model.setTeamName(Name);
							model.setEventID(eventModel.getEventID());
							model.setControl(TeamModel.TeamControl.Leader);
							model.setMembers(adapter.getUsers());
							if(model.getMembers().size()>1)
							{
								FetchData.getInstance().sendInvite(getApplicationContext(), model.getTeamID(), model.getInviteString(), new iResponseCallback()
								{
									@Override
									public void onResponse(ResponseStatus status)
									{
										progressDialog.dismiss();
										if (status == ResponseStatus.SUCCESS)
										{
											Database.getInstance().getTeamDB().addOrUpdateMyTeam(model);

											Intent intent = new Intent();
											intent.putExtra("Register", true);
											setResult(RESULT_OK, intent);

											eventModel.setNotify(true);
											eventModel.setRegistered(true);
											Database.getInstance().getEventsDB().addOrUpdateEvent(eventModel);
											eventModel.callStatusListener();

											Toast.makeText(CreateTeam.this, "Registered Successfully", Toast.LENGTH_LONG).show();

											if (!ActivityHelper.isDebugMode(CreateTeam.this))
												Answers.getInstance().logCustom(new CustomEvent("Team Register"));

											finish();
											ActivityHelper.setExitAnimation(CreateTeam.this);
										}
										else if (status == ResponseStatus.FAILED)
											Toast.makeText(CreateTeam.this, "Failed, Please Try Again", Toast.LENGTH_SHORT).show();
										else
											Toast.makeText(CreateTeam.this, "No Network Connection", Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onResponse(ResponseStatus status, Object object)
									{
										this.onResponse(status);
									}
								});
							}
							else
							{
								progressDialog.dismiss();
									Database.getInstance().getTeamDB().addOrUpdateMyTeam(model);

									Intent intent = new Intent();
									intent.putExtra("Register", true);
									setResult(RESULT_OK, intent);

									eventModel.setNotify(true);
									eventModel.setRegistered(true);
									Database.getInstance().getEventsDB().addOrUpdateEvent(eventModel);
									eventModel.callStatusListener();

									Toast.makeText(CreateTeam.this, "Registered Successfully", Toast.LENGTH_LONG).show();

									if (!ActivityHelper.isDebugMode(CreateTeam.this))
										Answers.getInstance().logCustom(new CustomEvent("Team Register"));

									finish();
									ActivityHelper.setExitAnimation(CreateTeam.this);
							}
						}
						else
						{
							progressDialog.dismiss();
							if (status == ResponseStatus.OTHER)
							{
								Toast.makeText(CreateTeam.this, "Choose Different Team Name", Toast.LENGTH_SHORT).show();
							}
							else if (status == ResponseStatus.FAILED)
							{
								Toast.makeText(CreateTeam.this, "Failed, Please Try Again", Toast.LENGTH_SHORT).show();
							}
							else
							{
								Toast.makeText(CreateTeam.this, "No Network Connection", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == GET_USER)
		{
			if (resultCode == RESULT_OK)
			{
				boolean found=false;
				UserKey newUser=( UserKey)data.getExtras().getSerializable("User");

				if(newUser==null)
					newUser = new UserKey();

				for(UserKey key: adapter.getUsers())
				{
					if(key.getUserID().equals(newUser.getUserID()))
					{
						found = true;
						break;
					}
				}
				if(!found)
				{
					adapter.getUsers().add((UserKey) data.getExtras().getSerializable("User"));
					adapter.notifyDataSetChanged();
				}
				else Toast.makeText(CreateTeam.this,"User Already Added",Toast.LENGTH_LONG).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed()
	{
		if (!ActivityHelper.revertToHomeIfLast(CreateTeam.this))
		{
			super.onBackPressed();
		}
		ActivityHelper.setExitAnimation(this);
	}
}
