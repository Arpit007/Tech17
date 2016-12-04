package com.nitkkr.gawds.tech16.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nitkkr.gawds.tech16.Helper.ActionBarBack;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Model.ExhibitionModel;
import com.nitkkr.gawds.tech16.R;

public class Exhibition extends AppCompatActivity
{
	ExhibitionModel model;
	ActionBarBack actionBarBack;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exhibition);

		actionBarBack=new ActionBarBack(Exhibition.this);

		model=(ExhibitionModel)getIntent().getExtras().getSerializable("Exhibition");

		LoadExhibition();

		final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.exhibition_notify);
		if(model.isNotify())
			fab.setImageResource(R.drawable.icon_starred);
		else fab.setImageResource(R.drawable.icon_unstarred);

		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				//TODO---------Save Notifications----------------
				if(model.isNotify())
				{
					model.setNotify(false);
					fab.setImageResource(R.drawable.icon_unstarred);
				}
				else
				{
					model.setNotify(true);
					fab.setImageResource(R.drawable.icon_starred);
				}
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});
	}

	private void LoadExhibition()
	{
		//TODO: Implement
		StringBuilder text = new StringBuilder("");
		text.append("Name: ").append(model.getEventName());
		text.append("\nVenue: ").append(model.getVenue());
		text.append("\nDescription: ").append(model.getDescription());
		text.append("\nAuthor: ").append(model.getAuthor());

		//----------------Date------------------------

		actionBarBack.setLabel(model.getEventName());
	}

	@Override
	public void onBackPressed()
	{
		if(ActivityHelper.revertToHomeIfLast(Exhibition.this))
			return;
		super.onBackPressed();
	}
}
