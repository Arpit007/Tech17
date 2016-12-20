package com.nitkkr.gawds.tech16.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.Model.EventKey;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class EventListAdapter extends BaseAdapter implements Filterable
{
	private ArrayList<EventKey> Event;
	private Context context;
	private EventSearch eventSearch;

	public EventListAdapter(Context context, ArrayList<EventKey> event)
	{
		this.context=context;
		this.Event = event;
		eventSearch=new EventSearch(this);
	}

	@Override
	public int getCount()
	{
		return Event.size();
	}

	@Override
	public Object getItem(int i)
	{
		return Event.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup)
	{
		final String childText = Event.get(i).getEventName();

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.layout_event_list_item, null);
		}

		TextView txtListChild = (TextView) view.findViewById(R.id.event_list_item_label);
		txtListChild.setText(childText);
		return view;
	}

	public ArrayList<EventKey> getEvents(){return Event;}

	public void setEvents(ArrayList<EventKey> models){Event = models;}

	public EventSearch getFilter(){return eventSearch;}
}
