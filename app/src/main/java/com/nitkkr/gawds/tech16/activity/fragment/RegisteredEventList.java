package com.nitkkr.gawds.tech16.activity.fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.activity.Event;
import com.nitkkr.gawds.tech16.adapter.EventListAdapter;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.model.EventKey;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

public class RegisteredEventList extends Fragment
{
	private EventListAdapter listAdapter;

	public void SearchQuery(String Query)
	{
		listAdapter.getFilter().filter(Query);
	}

	private ListView listView;
	private ArrayList<EventKey> listDataChild;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		final View view= inflater.inflate(R.layout.fragment_registered_event_list, container, false);

		listView = (ListView) view.findViewById(R.id.registered_event_list);

		prepareListData();

		listAdapter = new EventListAdapter(view.getContext(), listDataChild);

		listAdapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if(listAdapter.getCount()==0)
				{
					view.findViewById(R.id.None).setVisibility(View.VISIBLE);
				}
				else view.findViewById(R.id.None).setVisibility(View.INVISIBLE);
				super.onChanged();
			}
		});

		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				Bundle bundle=new Bundle();
				bundle.putSerializable("Event",(EventKey)listView.getAdapter().getItem(i));
				Intent intent=new Intent(view.getContext(), Event.class);
				intent.putExtras(bundle);
				view.getContext().startActivity(intent);
			}
		});

		return view;
	}

	void prepareListData()
	{
		listDataChild=Database.database.getEventsDB().getRegisteredEventKeys();
	}
}
