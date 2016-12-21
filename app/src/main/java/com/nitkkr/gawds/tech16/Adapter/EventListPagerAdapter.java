package com.nitkkr.gawds.tech16.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nitkkr.gawds.tech16.activity.Fragment.AllEventList;
import com.nitkkr.gawds.tech16.activity.Fragment.RegisteredEventList;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class EventListPagerAdapter extends FragmentStatePagerAdapter
{
	private int mNumOfTabs;
	private AllEventList allEventList =new AllEventList();
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
				return allEventList;
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
		allEventList.SearchQuery(Query);
		registeredEventList.SearchQuery(Query);
	}

}