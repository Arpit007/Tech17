package com.nitkkr.gawds.tech16.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.Helper.ActionBarBack;
import com.nitkkr.gawds.tech16.Helper.SignInStatus;
import com.nitkkr.gawds.tech16.Model.EventModel;
import com.nitkkr.gawds.tech16.R;

public class Event extends AppCompatActivity implements EventModel.EventStatusListener
{
	private final int REGISTER=100;
	private EventModel model;
	private ActionBarBack actionBar;
	private AlertDialog alertDialog;

	private void setRegister()
	{
		Button Register = (Button) findViewById(R.id.event_Register);
		if (model.isRegistered())
		{
			if (model.isSingleEvent())
			{
				Register.setText("Registered");
				Register.setEnabled(false);
			}
			if (model.isGroupEvent() || model.isVariableGroupEvent())
			{
				Register.setText("View Team");
				Register.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						//TODO: Pass Team Model
						Intent intent=new Intent(Event.this,ViewTeam.class);
						intent.putExtra("Event_Name","------------Event Name-------------------");
						intent.putExtra("Fixed_Team",!model.isVariableGroupEvent());
						intent.putExtra("Min_Members",model.getMinUsers());
						if(model.isVariableGroupEvent())
							intent.putExtra("Max_Users",model.getMaxUsers());
						startActivity(intent);
					}
				});
			}
		}
		else
		{
			Register.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if (model.isSingleEvent())
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(Event.this);
						builder.setCancelable(true);
						builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialogInterface, int i)
							{
								SignInStatus status=SignInStatus.NONE;
								//TODO: Register Single Event
								switch (status)
								{
									case FAILED:
										Toast.makeText(Event.this,"Failed, Please Try Again",Toast.LENGTH_LONG).show();
										break;
									case SUCCESS:
										Toast.makeText(Event.this,"Registered Successfully",Toast.LENGTH_LONG).show();
										LoadEvent();
										break;
									case OTHER:
										Toast.makeText(Event.this,"----------------------Message--------------------",Toast.LENGTH_LONG).show();
										break;
									default:
										break;
								}
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialogInterface, int i)
							{
								dialogInterface.dismiss();
							}
						});
						builder.setMessage("Are you sure, you want to Register for " + model.getEventName() + "?");
						builder.setTitle("Register Event");
						alertDialog = builder.create();
						alertDialog.show();
					}
					else
					{
						Intent intent=new Intent(Event.this,CreateTeam.class);
						intent.putExtra("Event_Name","------------Event Name-------------------");
						intent.putExtra("Fixed_Team",!model.isVariableGroupEvent());
						intent.putExtra("Min_Members",model.getMinUsers());
						if(model.isVariableGroupEvent())
							intent.putExtra("Max_Users",model.getMaxUsers());
						startActivityForResult(new Intent(Event.this, CreateTeam.class),REGISTER);
					}
				}
			});
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		actionBar = new ActionBarBack(this);
		LoadEvent();
	}


	@Override
	public void onBackPressed()
	{
		if (alertDialog != null && alertDialog.isShowing())
		{
			alertDialog.dismiss();
		}
		else
		{
			super.onBackPressed();
		}
	}

	private void LoadEvent()
	{
		model = new EventModel();
		model.setStatusListener(this);
		//TODO: Implement
		StringBuilder text = new StringBuilder("");
		text.append("Name: ").append(model.getEventName());
		text.append("\nVenue: ").append(model.getVenue());
		text.append("\nDescription: ").append(model.getDescription());
		text.append("\nRules: ").append(model.getRules());
		text.append("\nStatus: ").append(model.getEventStatus());

		//----------------Date, Result, Cood, Participants------------------------

		actionBar.setLabel(model.getEventName());
		if (model.isGroupEvent())
		{
			text.append("\nMember Count: ").append(model.getMinUsers());
		}
		if (model.isVariableGroupEvent())
		{
			text.append("\nTeam Size:").append(model.getMinUsers()).append("-").append(model.getMaxUsers());
		}

		( (TextView) findViewById(R.id.event_detail) ).setText(text.toString());
		setRegister();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode==REGISTER)
		{
			if(resultCode==RESULT_OK)
			{
				if(data.getBooleanExtra("Register",false))
				{
					Toast.makeText(Event.this,"Registered Successfully",Toast.LENGTH_LONG).show();
					LoadEvent();
				}
			}
		}
		else
			super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void EventStatusChanged(EventModel.EventStatus status)
	{
		//TODO: Event Status
	}
}
