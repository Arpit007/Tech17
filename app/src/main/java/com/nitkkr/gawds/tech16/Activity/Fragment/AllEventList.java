package com.nitkkr.gawds.tech16.Activity.Fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.Activity.Event;
import com.nitkkr.gawds.tech16.Adapter.AllEventListAdapter;
import com.nitkkr.gawds.tech16.Helper.iActionBar;
import com.nitkkr.gawds.tech16.Model.BaseEventModel;
import com.nitkkr.gawds.tech16.Model.EventKey;
import com.nitkkr.gawds.tech16.Model.EventModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AllEventList extends Fragment implements iActionBar
{
	View MyView;
	ExpandableListView expListView;
	HashMap<String, ArrayList<EventKey>> listDataChild;
	AllEventListAdapter listAdapter;
	public  AllEventList()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		MyView= inflater.inflate(R.layout.fragment_all_event_list, container, false);

		expListView = (ExpandableListView) MyView.findViewById(R.id.all_event_list);

		prepareListData();

		listAdapter = new AllEventListAdapter(MyView.getContext(), listDataChild);
		listAdapter.getFilter().setSearchList(( ListView)MyView.findViewById(R.id.search_event_list));

		listAdapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if(listAdapter.getGroupCount()==0)
				{
					MyView.findViewById(R.id.None).setVisibility(View.VISIBLE);
				}
				else MyView.findViewById(R.id.None).setVisibility(View.INVISIBLE);
				super.onChanged();
			}
		});

		expListView.setAdapter(listAdapter);

		expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
		{
			@Override
			public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l)
			{

				Bundle bundle=new Bundle();

				bundle.putSerializable("Event",(EventKey)(listAdapter.getChild(i,i1)));
				Intent intent=new Intent(view.getContext(), Event.class);
				intent.putExtras(bundle);
				view.getContext().startActivity(intent);
				return false;
			}
		});

		return MyView;
	}

	private void prepareListData()
	{
		listDataChild = new HashMap<>();

		ArrayList<EventKey> top250 = new ArrayList<>();
		top250.add(new EventKey("The Shawshank Redemption",123,false));
		top250.add(new EventKey("The Godfather",123,false));
		top250.add(new EventKey("The Godfather: Part II",123,false));
		top250.add(new EventKey("Pulp Fiction",123,false));
		top250.add(new EventKey("The Good, the Bad and the Ugly",123,false));
		top250.add(new EventKey("The Dark Knight",123,false));
		top250.add(new EventKey("12 Angry Men",123,false));

		ArrayList<EventKey> nowShowing = new ArrayList<>();
		nowShowing.add(new EventKey("The Conjuring",123,false));
		nowShowing.add(new EventKey("Despicable Me 2",123,false));
		nowShowing.add(new EventKey("Turbo",123,false));
		nowShowing.add(new EventKey("Grown Ups 2",123,false));
		nowShowing.add(new EventKey("Red 2",123,false));
		nowShowing.add(new EventKey("The Wolverine",123,false));

		ArrayList<EventKey> comingSoon = new ArrayList<>();
		comingSoon.add(new EventKey("2 Guns",123,false));
		comingSoon.add(new EventKey("The Smurfs 2",123,false));
		comingSoon.add(new EventKey("The Spectacular Now",123,false));
		comingSoon.add(new EventKey("The Canyons",123,false));
		comingSoon.add(new EventKey("Europa Report",123,false));

		listDataChild.put("Top 250", top250); // Header, Child data
		listDataChild.put("Now Showing", nowShowing);
		listDataChild.put("Coming Soon", comingSoon);
	}

	@Override
	public void NavButtonClicked()
	{
		//-----------not needed here--------------
	}

	@Override
	public void SearchQuery(String Query)
	{
		if(Query.equals(""))
		{
			MyView.findViewById(R.id.all_event_list).setVisibility(View.VISIBLE);
			MyView.findViewById(R.id.search_event_list).setVisibility(View.GONE);
		}
		else
		{
			MyView.findViewById(R.id.all_event_list).setVisibility(View.GONE);
			MyView.findViewById(R.id.search_event_list).setVisibility(View.VISIBLE);
			listAdapter.getFilter().filter(Query);
		}
	}
}
