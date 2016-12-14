package com.nitkkr.gawds.techspardha17.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nitkkr.gawds.techspardha17.Activity.Fragment.AllEventList;
import com.nitkkr.gawds.techspardha17.Activity.Fragment.RegisteredEventList;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class EventListPagerAdapter extends FragmentStatePagerAdapter
{
	private int mNumOfTabs;

	public EventListPagerAdapter(FragmentManager fm, int NumOfTabs, Context context) {
		super(fm);
		this.mNumOfTabs = NumOfTabs;
	}

	@Override
	public Fragment getItem(int position)
	{
		switch (position) {
			case 0:
				return new AllEventList();
			case 1:
				return new RegisteredEventList();
			default:
				return null;
		}
	}

	@Override
	public int getCount() {
		return mNumOfTabs;
	}
}