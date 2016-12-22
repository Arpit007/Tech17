package com.nitkkr.gawds.tech16.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nitkkr.gawds.tech16.activity.fragment.ScreenSlidePageFragment;
import com.nitkkr.gawds.tech16.helper.ActionBarBack;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.R;

public class About extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		ActivityHelper.setStatusBarColor(this);

		ActionBarBack actionBarBack=new ActionBarBack(About.this);
		actionBarBack.setLabel("About");

		if(savedInstanceState==null){
			ScreenSlidePageFragment screenSlidePageFragment=new ScreenSlidePageFragment();
			FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.your_placeholder,screenSlidePageFragment);
			ft.commit();
		}

	}

	@Override
	public void onBackPressed()
	{
		if(ActivityHelper.revertToHomeIfLast(About.this))
			return;
		super.onBackPressed();
	}
}
