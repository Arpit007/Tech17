package com.nitkkr.gawds.tech16.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class EventListAdapter extends BaseAdapter
{
	ArrayList<String> Event;
	Context context;

	public EventListAdapter(Context context, ArrayList<String> event)
	{
		this.context=context;
		this.Event = event;
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
		final String childText = Event.get(i);

		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.layout_event_list_item, null);
		}

		TextView txtListChild = (TextView) view.findViewById(R.id.event_list_item_label);

		txtListChild.setText(childText);
		return view;
	}
}
