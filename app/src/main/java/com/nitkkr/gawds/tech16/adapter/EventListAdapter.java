package com.nitkkr.gawds.tech16.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.activity.Event;
import com.nitkkr.gawds.tech16.api.FetchData;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.model.EventKey;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.model.EventModel;
import com.nitkkr.gawds.tech16.model.ExhibitionModel;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class EventListAdapter extends BaseAdapter implements Filterable
{
	private ArrayList<EventKey> Events;
	private Context context;
	private EventSearch eventSearch;
	private boolean Notify;

	public EventListAdapter(Context context, ArrayList<EventKey> events, final boolean Notify)
	{
		this.Notify = Notify;
		this.context=context;
		this.Events = events;

		eventSearch=new EventSearch(this);
	}

	@Override
	public int getCount()
	{
		return Events.size();
	}

	@Override
	public Object getItem(int i)
	{
		return Events.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup)
	{
		final String childText = Events.get(i).getEventName();

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.layout_event_list_item, null);
		}

		ImageView Star=(ImageView) view.findViewById(R.id.Event_Star);

		if(!Notify)
		{
			if(Database.getInstance().getEventsDB().getEvent(Events.get(i)).isNotify())
			{
				Star.setImageResource(R.drawable.icon_starred);
				Star.setVisibility(View.VISIBLE);
			}
			else
			{
				Star.setImageResource(R.drawable.icon_unstarred);
				Star.setVisibility(View.INVISIBLE);
			}
		}
		else
		{
			Star.setVisibility(View.VISIBLE);
			final ExhibitionModel model= Database.getInstance().getExhibitionDB().getExhibition(Events.get(i));

			if(model.isNotify())
				Star.setImageResource(R.drawable.icon_starred);
			else Star.setImageResource(R.drawable.icon_unstarred);
		}

		TextView txtListChild = (TextView) view.findViewById(R.id.event_list_item_label);
		txtListChild.setText(childText);
		return view;
	}

	public ArrayList<EventKey> getEvents(){return Events;}

	public void setEvents(ArrayList<EventKey> models){
		Events = models;}

	public EventSearch getFilter(){return eventSearch;}

	public void updateList()
	{
		eventSearch.updateList();
	}

	public boolean getNotify(){return Notify;}

}
