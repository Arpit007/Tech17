package com.nitkkr.gawds.tech16.adapter;

import android.content.Context;
import android.widget.Filter;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.model.EventKey;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class AllEventSearch extends Filter
{
	private HashMap<String, EventListAdapter> Events;
	private EventListAdapter adapter;
	private ListView listView;
	private int EventID = -1;

	public AllEventSearch(AllEventListAdapter adapter, Context context)
	{
		Events = adapter.getEvents();
		this.adapter = new EventListAdapter(context, new ArrayList<EventKey>(), false);
	}

	@Override
	protected FilterResults performFiltering(CharSequence charSequence)
	{
		FilterResults results = new FilterResults();

		ArrayList<EventKey> list = new ArrayList<>();

		for (String Label : Events.keySet())
		{
			for (EventKey key : Events.get(Label).getEvents())
			{
				if (key.getEventID() == EventID)
				{
					key.setNotify(Database.getInstance().getEventsDB().getEventKey(EventID).isNotify());
					EventID = -1;
				}
				if (key.getEventName().toLowerCase().contains(charSequence.toString().toLowerCase()))
				{
					list.add(key);
				}
			}
		}

		results.values = list;
		results.count = list.size();

		return results;
	}

	@Override
	protected void publishResults(CharSequence charSequence, FilterResults filterResults)
	{
		adapter.setEvents((ArrayList<EventKey>) filterResults.values);
		adapter.notifyDataSetChanged();
	}

	public void setSearchList(ListView list)
	{
		listView = list;
		listView.setAdapter(adapter);
	}

	public void onClick(int EventID)
	{
		this.EventID = EventID;
	}

	public void setEvents(HashMap<String, EventListAdapter> lists)
	{
		Events = lists;
	}

}
