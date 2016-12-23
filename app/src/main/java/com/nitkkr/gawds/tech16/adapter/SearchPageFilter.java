package com.nitkkr.gawds.tech16.adapter;

import android.widget.Filter;

import com.nitkkr.gawds.tech16.api.EventTargetType;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.database.DbConstants;
import com.nitkkr.gawds.tech16.model.EventKey;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 23-Dec-16.
 */

public class SearchPageFilter extends Filter
{
	private SearchPageAdapter adapter;
	private ArrayList<Holder> Keys;

	public class Holder
	{
		public EventKey key;
		public EventTargetType type;
		public Holder(EventKey Key, EventTargetType Type)
		{
			key = Key;
			type = Type;
		}
	}

	public SearchPageFilter(SearchPageAdapter adapter){this.adapter = adapter;}

	@Override
	protected FilterResults performFiltering(CharSequence charSequence)
	{
		if(charSequence.toString().isEmpty())
			Keys=new ArrayList<>();
		else
		{
			String Query=charSequence.toString().toLowerCase();
			ArrayList<EventKey> events= Database.getInstance().getEventsDB().getEventKeys(DbConstants.EventNames.EventName.Name() + " LIKE \"%" + Query + "%\"");
			ArrayList<EventKey> gtalk=Database.getInstance().getExhibitionDB().getExhibitionKeys(DbConstants.ExhibitionNames.EventName.Name() + " LIKE \"%" + Query + "%\" AND "
					+ DbConstants.ExhibitionNames.GTalk.Name() + " = 1");
			ArrayList<EventKey> exhibition=Database.getInstance().getExhibitionDB().getExhibitionKeys(DbConstants.ExhibitionNames.EventName.Name() + " LIKE \"%" + Query + "%\" AND "
					+ DbConstants.ExhibitionNames.GTalk.Name() + " = 0");

			Keys=new ArrayList<>(events.size()+gtalk.size()+exhibition.size());

			for(EventKey key:events)
				Keys.add(new Holder(key,EventTargetType.Event));
			for(EventKey key:gtalk)
				Keys.add(new Holder(key,EventTargetType.GuestTalk));
			for(EventKey key:exhibition)
				Keys.add(new Holder(key,EventTargetType.Exhibition));
		}
		FilterResults results = new FilterResults();
		results.count = Keys.size();
		results.values=Keys;
		return results;
	}

	@Override
	protected void publishResults(CharSequence charSequence, FilterResults filterResults)
	{
		adapter.setKeys((ArrayList<Holder>)filterResults.values);
		adapter.notifyDataSetChanged();
	}
}
