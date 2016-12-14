package com.nitkkr.gawds.techspardha17.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nitkkr.gawds.techspardha17.Helper.ActionBarBack;
import com.nitkkr.gawds.techspardha17.Helper.ActivityHelper;
import com.nitkkr.gawds.techspardha17.R;

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
		if(ActivityHelper.revertToHomeIfLast(About.this))
			return;
		super.onBackPressed();
	}
}
