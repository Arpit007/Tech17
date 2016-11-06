package com.nitkkr.gawds.tech16.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.Helper.ActionBarBack;
import com.nitkkr.gawds.tech16.Model.EventModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.RegisterGroupEvent;

public class Event extends AppCompatActivity implements EventModel.EventStateListener
{
	private EventModel model;
	private ActionBarBack actionBar;
	private AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		actionBar=new ActionBarBack(this);
		LoadEvent();
		if(model.isRegistered())
		{
			Button Register=(Button)findViewById(R.id.event_Register);
			Register.setText("Registered");
			Register.setEnabled(false);
		}
		findViewById(R.id.event_Register).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(model.isSingleEvent())
				{
					AlertDialog.Builder builder=new AlertDialog.Builder(Event.this);
					builder.setCancelable(true);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialogInterface, int i)
						{
							//TODO: Implement
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
					builder.setMessage("Are you sure, you want to Register for "+model.getEventName()+"?");
					builder.setTitle("Register Event");
					alertDialog=builder.create();
					alertDialog.show();
				}
				else
				{
					startActivity(new Intent(Event.this, RegisterGroupEvent.class));
				}
			}
		});
	}

	@Override
	public void EventStateChanged(EventModel.EventStatus status)
	{
		//TODO: Event Status
	}

	@Override
	public void onBackPressed()
	{
		if(alertDialog!=null && alertDialog.isShowing())
			alertDialog.dismiss();
		else super.onBackPressed();
	}

	private void LoadEvent()
	{
		model=new EventModel(this);
		//TODO: Implement
		StringBuilder text=new StringBuilder("");
		text.append("Name: ").append(model.getEventName());
		text.append("\nVenue: ").append(model.getVenue());
		text.append("\nDescription: ").append(model.getDescription());
		text.append("\nRules: ").append(model.getRules());
		text.append("\nStatus: ").append(model.getEventStatus());
		//Date, Result, Cood, Participants
		actionBar.setLabel(model.getEventName());
		if(model.isGroupEvent())
			text.append("\nMember Count: ").append(model.getMinUsers());
		if(model.isVariableGroupEvent())
			text.append("\nTeam Size:").append(model.getMinUsers()).append("-").append(model.getMaxUsers());

		(( TextView)findViewById(R.id.event_detail)).setText(text.toString());
	}
}
