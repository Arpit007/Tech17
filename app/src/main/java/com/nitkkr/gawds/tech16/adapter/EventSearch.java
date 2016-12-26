package com.nitkkr.gawds.tech16.adapter;

import android.widget.Filter;

import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.model.EventKey;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 17-Dec-16.
 */

public class EventSearch extends Filter
{
	private EventListAdapter adapter;
	private ArrayList<EventKey> eventKeys;
	private boolean isExhibition = false;
	private int clickedEventId = -1;

	public EventSearch(EventListAdapter adapter)
	{
		this.adapter = adapter;
		this.eventKeys = adapter.getEvents();
	}

	@Override
	protected FilterResults performFiltering(CharSequence charSequence)
	{
		FilterResults filterResults = new FilterResults();

		if (charSequence != null && charSequence.length() > 0)
		{
			ArrayList<EventKey> tempList = new ArrayList<>();

			for (EventKey model : eventKeys)
			{
				if (model.getEventID() == clickedEventId)
				{
					if (isExhibition)
					{
						model.setNotify(Database.getInstance().getExhibitionDB().getExhibitionKey(clickedEventId).isNotify());
					}
					else
					{
						model.setNotify(Database.getInstance().getEventsDB().getEventKey(clickedEventId).isNotify());
					}
					clickedEventId = -1;
				}
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
		adapter.setEventsByfilter((ArrayList<EventKey>) filterResults.values);
		adapter.notifyDataSetChanged();
	}

	public void onClick(int Event_ID, boolean isExhibition)
	{
		this.clickedEventId = Event_ID;
		this.isExhibition = isExhibition;
	}

	public void setEventKeys(ArrayList<EventKey> keys)
	{
		eventKeys = keys;
	}
}
