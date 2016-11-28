package com.nitkkr.gawds.tech16.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nitkkr.gawds.tech16.Helper.ActionBarBack;
import com.nitkkr.gawds.tech16.Helper.ApplicationHelper;
import com.nitkkr.gawds.tech16.R;

public class About extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		ActionBarBack actionBarBack=new ActionBarBack(About.this);
		//--------------------------Set Label-------------------------------------------
		actionBarBack.setLabel("About...");
	}

	@Override
	public void onBackPressed()
	{
		if(ApplicationHelper.revertToHomeIfLast(About.this))
			return;
		super.onBackPressed();
	}
}
