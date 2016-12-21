package com.nitkkr.gawds.tech16.adapter;

import android.widget.Filter;

import com.nitkkr.gawds.tech16.model.EventKey;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class EventSearch extends Filter
{
	EventListAdapter adapter;
	private ArrayList<EventKey> eventKeys;

	public EventSearch(EventListAdapter adapter)
	{
		this.adapter = adapter;
		this.eventKeys =adapter.getEvents();
	}

	@Override
	protected FilterResults performFiltering(CharSequence charSequence)
	{
		FilterResults filterResults = new FilterResults();

		if (charSequence!=null && charSequence.length()>0)
		{
			ArrayList<EventKey> tempList = new ArrayList<>();

			for (EventKey model : eventKeys)
			{
				if (model.getEventName().toLowerCase().contains(charSequence.toString().toLowerCase()))
				{
					tempList.add(model);
				}
			}

			filterResults.count = tempList.size();
			filterResults.values = tempList;
		}
		else
		{
			filterResults.count = eventKeys.size();
			filterResults.values = eventKeys;
		}

		return filterResults;
	}

	@Override
	protected void publishResults(CharSequence charSequence, FilterResults filterResults)
	{
		adapter.setEvents((ArrayList<EventKey>) filterResults.values);
		adapter.notifyDataSetChanged();
	}
}
