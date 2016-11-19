package com.nitkkr.gawds.tech16.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class AllEventListAdapter extends BaseExpandableListAdapter
{
	private Context context;
	private ArrayList<String> Categories;
	private HashMap<String, ArrayList<String>> Events;

	public AllEventListAdapter(Context context, ArrayList<String> categories,
	                           HashMap<String, ArrayList<String>> events) {
		this.context = context;
		this.Categories = categories;
		this.Events = events;
	}

	@Override
	public Object getChild(int i, int i1)
	{
		return this.Events.get(this.Categories.get(i)).get(i1);
	}

	@Override
	public int getGroupCount()
	{
		return Categories.size();
	}

	@Override
	public int getChildrenCount(int i)
	{
		return this.Events.get(this.Categories.get(i)).size();
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
		final String childText = (String) getChild(i, i1);

		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.layout_event_list_item, null);
		}

		TextView txtListChild = (TextView) view.findViewById(R.id.event_list_item_label);

		txtListChild.setText(childText);
		return view;
	}

	@Override
	public boolean isChildSelectable(int i, int i1)
	{
		return true;
	}
}
