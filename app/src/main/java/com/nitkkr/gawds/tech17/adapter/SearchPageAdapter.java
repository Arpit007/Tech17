package com.nitkkr.gawds.tech17.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.Event;
import com.nitkkr.gawds.tech17.activity.Exhibition;
import com.nitkkr.gawds.tech17.api.EventTargetType;
import com.nitkkr.gawds.tech17.database.Database;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 23-Dec-16.
 */

public class SearchPageAdapter extends BaseAdapter
{
	private Context context;
	private SearchPageFilter filter = new SearchPageFilter(this);
	private ArrayList<SearchPageFilter.Holder> Keys = new ArrayList<>();
	public int ID = -1;

	public SearchPageAdapter(Context context)
	{
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return Keys.size();
	}

	@Override
	public Object getItem(int i)
	{
		return Keys.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup)
	{

		if (ID == Keys.get(i).key.getEventID())
		{
			SearchPageFilter.Holder holder = Keys.get(i);
			switch (holder.type)
			{
				case Event:
				case Informals:
					holder.key.setNotify(Database.getInstance().getEventsDB().getEventKey(holder.key.getEventID()).isNotify());
					break;
				case GuestTalk:
				case Exhibition:
					holder.key.setNotify(Database.getInstance().getExhibitionDB().getExhibitionKey(holder.key.getEventID()).isNotify());
					break;
			}
			ID = -1;
		}

		final String childText = Keys.get(i).key.getEventName();

		if (view == null)
		{
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.layout_event_list_item, null);
		}

		ImageView Star = (ImageView) view.findViewById(R.id.Event_Star);

		SearchPageFilter.Holder holder = Keys.get(i);

		if (holder.key.isNotify())
		{
			Star.setImageResource(R.drawable.icon_starred);
			Star.setVisibility(View.VISIBLE);
		}
		else
		{
			Star.setVisibility(View.INVISIBLE);
		}

		TextView txtListChild = (TextView) view.findViewById(R.id.event_list_item_label);
		txtListChild.setText(childText);
		return view;
	}

	public void onClick(int ID)
	{
		this.ID = Keys.get(ID).key.getEventID();
		SearchPageFilter.Holder holder = Keys.get(ID);
		if (holder.type == EventTargetType.Event || holder.type == EventTargetType.Informals)
		{
			Bundle bundle = new Bundle();
			bundle.putSerializable("Event", holder.key);
			Intent intent = new Intent(context, Event.class);
			intent.putExtras(bundle);
			context.startActivity(intent);
		}
		else if (holder.type == EventTargetType.GuestTalk || holder.type == EventTargetType.Exhibition)
		{
			Bundle bundle = new Bundle();
			bundle.putSerializable("Event", holder.key);
			Intent intent = new Intent(context, Exhibition.class);
			intent.putExtras(bundle);
			context.startActivity(intent);
		}
	}

	public void setKeys(ArrayList<SearchPageFilter.Holder> keys)
	{
		this.Keys = keys;
	}

	public SearchPageFilter getFilter()
	{
		return filter;
	}

}
