package com.nitkkr.gawds.tech16.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nitkkr.gawds.tech16.Activity.Fragment.AllEventList;
import com.nitkkr.gawds.tech16.Activity.Fragment.RegisteredEventList;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class EventListPagerAdapter extends FragmentStatePagerAdapter
{
	private int mNumOfTabs;
	private AllEventList allEventListist=new AllEventList();
	private RegisteredEventList registeredEventList=new RegisteredEventList();

	public EventListPagerAdapter(FragmentManager fm, int NumOfTabs) {
		super(fm);
		this.mNumOfTabs = NumOfTabs;
	}

	@Override
	public Fragment getItem(int position)
	{
		switch (position) {
			case 0:
				return allEventListist;
			case 1:
				return registeredEventList;
			default:
				return null;
		}
	}

	@Override
	public int getCount() {
		return mNumOfTabs;
	}

	public void Filter(String Query)
	{
		allEventListist.SearchQuery(Query);
		registeredEventList.SearchQuery(Query);
	}

}