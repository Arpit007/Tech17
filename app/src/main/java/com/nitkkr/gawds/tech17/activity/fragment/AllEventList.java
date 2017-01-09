package com.nitkkr.gawds.tech17.activity.fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.Event;
import com.nitkkr.gawds.tech17.adapter.AllEventListAdapter;
import com.nitkkr.gawds.tech17.adapter.EventListAdapter;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.database.DbConstants;
import com.nitkkr.gawds.tech17.model.EventKey;
import com.nitkkr.gawds.tech17.model.SocietyModel;

import java.util.ArrayList;
import java.util.HashMap;

public class AllEventList extends Fragment
{
	View MyView;
	ExpandableListView expListView;
	HashMap<String, ArrayList<EventKey>> HashData = new HashMap<>();
	AllEventListAdapter listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		MyView = inflater.inflate(R.layout.fragment_all_event_list, container, false);

		expListView = (ExpandableListView) MyView.findViewById(R.id.all_event_list);

		final ListView listView = (ListView) MyView.findViewById(R.id.search_event_list);

		listAdapter = new AllEventListAdapter(MyView.getContext(), HashData);
		listAdapter.getFilter().setSearchList(listView);

		listView.getAdapter().registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if (listView.getVisibility() == View.VISIBLE && listView.getAdapter().getCount() == 0)
				{
					MyView.findViewById(R.id.None).setVisibility(View.VISIBLE);
				}
				else
				{
					MyView.findViewById(R.id.None).setVisibility(View.GONE);
				}

				super.onChanged();
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				EventListAdapter adapter = (EventListAdapter) listView.getAdapter();
				adapter.onClick(( (EventKey) adapter.getItem(i) ).getEventID());

				Bundle bundle = new Bundle();
				bundle.putSerializable("Event", (EventKey) listView.getAdapter().getItem(i));
				Intent intent = new Intent(view.getContext(), Event.class);
				intent.putExtras(bundle);
				view.getContext().startActivity(intent);
			}
		});

		listAdapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if (listAdapter.getGroupCount() == 0)
				{
					MyView.findViewById(R.id.None).setVisibility(View.VISIBLE);
				}
				else
				{
					MyView.findViewById(R.id.None).setVisibility(View.GONE);
				}
				super.onChanged();
			}
		});

		expListView.setAdapter(listAdapter);

		expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
		{
			@Override
			public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l)
			{
				listAdapter.onClick(i, i1);

				Bundle bundle = new Bundle();
				bundle.putSerializable("Event", (EventKey) ( listAdapter.getChild(i, i1) ));

				Intent intent = new Intent(view.getContext(), Event.class);
				intent.putExtras(bundle);
				view.getContext().startActivity(intent);
				return false;
			}
		});

		if (Database.getInstance().getEventsDB().getRowCount() == 0)
		{
			MyView.findViewById(R.id.None).setVisibility(View.VISIBLE);
		}
		else
		{
			MyView.findViewById(R.id.None).setVisibility(View.GONE);
		}

		prepareListData();

		return MyView;
	}

	private void prepareListData()
	{
		HashData = new HashMap<>();
		ArrayList<SocietyModel> societies = Database.getInstance().getSocietyDB().getAllSocieties();

		for(SocietyModel model: societies)
		{
			if(model.getName().toLowerCase().equals("workshops"))
			{
				societies.remove(model);
				break;
			}
		}

		for (SocietyModel society : societies)
		{
			HashData.put(society.getName(), Database.getInstance().getEventsDB().getEventKeys(DbConstants.EventNames.Society.Name() + " = " + society.getID()));
		}
		listAdapter.setEvents(HashData);
		listAdapter.notifyDataSetChanged();
	}

	public void SearchQuery(String Query)
	{
		if (Query.equals(""))
		{
			if (Database.getInstance().getEventsDB().getRowCount() == 0)
			{
				getView().findViewById(R.id.None).setVisibility(View.VISIBLE);
			}
			else
			{
				getView().findViewById(R.id.None).setVisibility(View.GONE);
			}

			getView().findViewById(R.id.all_event_list).setVisibility(View.VISIBLE);
			getView().findViewById(R.id.search_event_list).setVisibility(View.GONE);
		}
		else
		{
			getView().findViewById(R.id.all_event_list).setVisibility(View.GONE);
			getView().findViewById(R.id.search_event_list).setVisibility(View.VISIBLE);
			listAdapter.getFilter().filter(Query);
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (listAdapter.ID != -1)
		{
			listAdapter.ID = -1;
			listAdapter.notifyDataSetChanged();
		}
	}
}
