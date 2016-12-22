package com.nitkkr.gawds.tech16.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;

public class SearchPage extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_page);

		ActivityHelper.setStatusBarColor(this);

		String DataType=getIntent().getStringExtra("Data_Type");
		/*
			User for Team Registration
			All for Global Search
		 */

	}
}
