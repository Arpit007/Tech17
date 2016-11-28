package com.nitkkr.gawds.tech16.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nitkkr.gawds.tech16.Helper.ActionBarBack;
import com.nitkkr.gawds.tech16.Helper.ApplicationHelper;
import com.nitkkr.gawds.tech16.Model.ExhibitionModel;
import com.nitkkr.gawds.tech16.R;

public class MusicalNight extends AppCompatActivity
{

	private ExhibitionModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_musical_night);

		ActionBarBack barBack=new ActionBarBack(MusicalNight.this);
		barBack.setLabel("Musical Night");

		model.setEventID(getIntent().getStringExtra("Event_ID"));
		//TODO:Load Event

	}

	@Override
	public void onBackPressed()
	{
		if(ApplicationHelper.revertToHomeIfLast(MusicalNight.this))
			return;
		super.onBackPressed();
	}
}
