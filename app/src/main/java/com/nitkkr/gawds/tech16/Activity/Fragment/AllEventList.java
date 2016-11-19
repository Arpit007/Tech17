package com.nitkkr.gawds.tech16.Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.nitkkr.gawds.tech16.Activity.Event;
import com.nitkkr.gawds.tech16.Adapter.AllEventListAdapter;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AllEventList extends Fragment
{
	AllEventListAdapter listAdapter;
	ExpandableListView expListView;
	ArrayList<String> listDataHeader;
	HashMap<String, ArrayList<String>> listDataChild;

	public  AllEventList(){}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View view= inflater.inflate(R.layout.fragment_all_event_list, container, false);

		expListView = (ExpandableListView) view.findViewById(R.id.all_event_list);

		prepareListData();

		listAdapter = new AllEventListAdapter(view.getContext(), listDataHeader, listDataChild);

		expListView.setAdapter(listAdapter);

		expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
		{
			@Override
			public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l)
			{
				//============================Implemet================================

				view.getContext().startActivity(new Intent(view.getContext(), Event.class));
				return false;
			}
		});

		return view;
	}

	private void prepareListData()
	{
		listDataHeader = new ArrayList<>();
		listDataChild = new HashMap<>();

		// Adding child data
		listDataHeader.add("Top 250");
		listDataHeader.add("Now Showing");
		listDataHeader.add("Coming Soon..");

		// Adding child data
		ArrayList<String> top250 = new ArrayList<>();
		top250.add("The Shawshank Redemption");
		top250.add("The Godfather");
		top250.add("The Godfather: Part II");
		top250.add("Pulp Fiction");
		top250.add("The Good, the Bad and the Ugly");
		top250.add("The Dark Knight");
		top250.add("12 Angry Men");

		ArrayList<String> nowShowing = new ArrayList<>();
		nowShowing.add("The Conjuring");
		nowShowing.add("Despicable Me 2");
		nowShowing.add("Turbo");
		nowShowing.add("Grown Ups 2");
		nowShowing.add("Red 2");
		nowShowing.add("The Wolverine");

		ArrayList<String> comingSoon = new ArrayList<>();
		comingSoon.add("2 Guns");
		comingSoon.add("The Smurfs 2");
		comingSoon.add("The Spectacular Now");
		comingSoon.add("The Canyons");
		comingSoon.add("Europa Report");

		listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
		listDataChild.put(listDataHeader.get(1), nowShowing);
		listDataChild.put(listDataHeader.get(2), comingSoon);
	}
}
