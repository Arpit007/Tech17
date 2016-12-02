package com.nitkkr.gawds.tech16.Activity;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nitkkr.gawds.tech16.Adapter.EventListPagerAdapter;
import com.nitkkr.gawds.tech16.Helper.ActionBarNavDrawer;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.iActionBar;
import com.nitkkr.gawds.tech16.R;

public class EventList extends AppCompatActivity
{
	private ActionBarNavDrawer barNavDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

		barNavDrawer=new ActionBarNavDrawer(EventList.this, new iActionBar()
		{
			@Override
			public void NavButtonClicked()
			{

			}

			@Override
			public void SearchButtonClicked()
			{

			}
		});

		barNavDrawer.setLabel("Events");

		TabLayout tabLayout = (TabLayout) findViewById(R.id.event_list_tab);
		tabLayout.addTab(tabLayout.newTab().setText(R.string.Event_list_Tab_1));
		tabLayout.addTab(tabLayout.newTab().setText(R.string.Event_list_Tab_2));
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

		final ViewPager viewPager = (ViewPager) findViewById(R.id.event_list_page_view);
		final EventListPagerAdapter adapter = new EventListPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),EventList.this);
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		viewPager.setCurrentItem(0);

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
	}

	@Override
	public void onBackPressed()
	{
		if(!barNavDrawer.onBackPressed())
		{
			if(ActivityHelper.revertToHomeIfLast(EventList.this))
				return;

			super.onBackPressed();
		}
	}
}
