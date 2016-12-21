package com.nitkkr.gawds.tech16.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.model.EventKey;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class AllEventListAdapter extends BaseExpandableListAdapter implements Filterable
{
	private Context context;
	private HashMap<String, EventListAdapter> Events;
	private AllEventSearch Filter;

	public AllEventListAdapter(Context context, HashMap<String,ArrayList<EventKey>> events)
	{
		this.context = context;
		Events=new HashMap<>(events.size());
		for(String Category: events.keySet())
		{
			Events.put(Category,new EventListAdapter(context,events.get(Category)));
		}
		Filter=new AllEventSearch(this, context);
	}

	@Override
	public Object getChild(int i, int i1)
	{
		return this.Events.get((String)getGroup(i)).getItem(i1);
	}

	@Override
	public int getGroupCount()
	{
		return Events.size();
	}

	@Override
	public int getChildrenCount(int i)
	{
		return this.Events.get((String)getGroup(i)).getCount();
	}

	@Override
	public Object getGroup(int i)
	{
		int index=0;
		for(String Category: Events.keySet())
		{
			if(index==i)
				return Category;
			index++;
		}
		return "";
	}

	@Override
	public long getGroupId(int i)
	{
		return i;
	}

	@Override
	public long getChildId(int i, int i1)
	{
		return i1;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup)
	{
		String headerTitle = (String) getGroup(i);
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.layout_event_list_head, null);
		}


		TextView lblListHeader = (TextView) view.findViewById(R.id.event_list_head_label);
		lblListHeader.setText(headerTitle);

		return view;
	}

	@Override
	public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup)
	{
		return this.Events.get((String)getGroup(i)).getView(i1,view,viewGroup);
	}

	@Override
	public boolean isChildSelectable(int i, int i1)
	{
		return true;
	}

	public AllEventSearch getFilter()
	{
		return Filter;
	}

	public HashMap<String,EventListAdapter> getEvents()
	{
		return Events;
	}

	public void setEvents(HashMap<String,ArrayList<EventKey>> events)
	{
		Events=new HashMap<>(events.size());
		for(String Category: events.keySet())
		{
			Events.put(Category,new EventListAdapter(context,events.get(Category)));
		}
	}
}
