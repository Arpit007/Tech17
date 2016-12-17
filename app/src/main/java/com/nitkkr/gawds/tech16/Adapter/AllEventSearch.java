package com.nitkkr.gawds.tech16.Adapter;

import android.content.Context;
import android.widget.Filter;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.Model.EventKey;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class AllEventSearch extends Filter
{
	private HashMap<String, EventListAdapter> Events;
	EventListAdapter adapter;
	ListView listView;

	public AllEventSearch(AllEventListAdapter adapter, Context context)
	{
		Events=adapter.getEvents();
		this.adapter=new EventListAdapter(context,new ArrayList<EventKey>());
	}

	@Override
	protected FilterResults performFiltering(CharSequence charSequence)
	{
		FilterResults results=new FilterResults();

		ArrayList<EventKey> list=new ArrayList<>();

		for (String Label: Events.keySet())
		{
			for(EventKey key: Events.get(Label).getEvents())
			{
				if(key.getEventName().toLowerCase().contains(charSequence.toString().toLowerCase()))
					list.add(key);
			}
		}

		results.values=list;
		results.count=list.size();

		return results;
	}

	@Override
	protected void publishResults(CharSequence charSequence, FilterResults filterResults)
	{
		adapter.setEvents((ArrayList<EventKey>)filterResults.values);
		adapter.notifyDataSetChanged();
	}

	public void setSearchList(ListView list)
	{
		listView=list;
		listView.setAdapter(adapter);
	}
}
