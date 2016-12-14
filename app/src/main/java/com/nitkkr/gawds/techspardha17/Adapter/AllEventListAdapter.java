package com.nitkkr.gawds.techspardha17.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.nitkkr.gawds.techspardha17.Model.BaseEventModel;
import com.nitkkr.gawds.techspardha17.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class AllEventListAdapter extends BaseExpandableListAdapter
{
	private Context context;
	private ArrayList<String> Categories;
	private HashMap<String, EventListAdapter> Events;

	public AllEventListAdapter(Context context, ArrayList<String> categories, HashMap<String,ArrayList<BaseEventModel>> events)
	{
		this.context = context;
		this.Categories = categories;
		Events=new HashMap<>(events.size());
		for(int x=0;x<categories.size();x++)
		{
			Events.put(Categories.get(x),new EventListAdapter(context,events.get(categories.get(x))));
		}
	}

	@Override
	public Object getChild(int i, int i1)
	{
		return this.Events.get(this.Categories.get(i)).getItem(i1);
	}

	@Override
	public int getGroupCount()
	{
		return Categories.size();
	}

	@Override
	public int getChildrenCount(int i)
	{
		return this.Events.get(this.Categories.get(i)).getCount();
	}

	@Override
	public Object getGroup(int i)
	{
		return this.Events.get(i);
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
	public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup)
	{
		String headerTitle = (String) getGroup(i);
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.layout_event_list_head, null);
		}

		TextView lblListHeader = (TextView) view
				.findViewById(R.id.event_list_head_label);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return view;
	}

	@Override
	public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup)
	{
		return this.Events.get(this.Categories.get(i)).getView(i1,view,viewGroup);
	}

	@Override
	public boolean isChildSelectable(int i, int i1)
	{
		return true;
	}
}
