package com.nitkkr.gawds.techspardha17.Activity.Fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.nitkkr.gawds.techspardha17.Activity.Event;
import com.nitkkr.gawds.techspardha17.Adapter.AllEventListAdapter;
import com.nitkkr.gawds.techspardha17.Model.BaseEventModel;
import com.nitkkr.gawds.techspardha17.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AllEventList extends Fragment
{
	ExpandableListView expListView;
	ArrayList<String> listDataHeader;
	HashMap<String, ArrayList<BaseEventModel>> listDataChild;

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
		final View view= inflater.inflate(R.layout.fragment_all_event_list, container, false);

		expListView = (ExpandableListView) view.findViewById(R.id.all_event_list);

		prepareListData();

		final AllEventListAdapter listAdapter = new AllEventListAdapter(view.getContext(), listDataHeader, listDataChild);

		listAdapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if(listAdapter.getGroupCount()==0)
				{
					view.findViewById(R.id.None).setVisibility(View.VISIBLE);
				}
				else view.findViewById(R.id.None).setVisibility(View.INVISIBLE);
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
				bundle.putSerializable("Event",(BaseEventModel)((AllEventListAdapter)expListView.getAdapter()).getChild(i,i1));
				Intent intent=new Intent(view.getContext(), Event.class);
				intent.putExtras(bundle);
				view.getContext().startActivity(intent);
				return false;
			}
		});

		return view;
	}

	private void prepareListData()
	{
		listDataHeader = new ArrayList<>();
		listDataChild = new HashMap<>();

		// Adding Head data
		listDataHeader.add("Top 250");
		listDataHeader.add("Now Showing");
		listDataHeader.add("Coming Soon..");

		// Adding child data
		ArrayList<BaseEventModel> top250 = new ArrayList<>();
		top250.add(new BaseEventModel("The Shawshank Redemption"));
		top250.add(new BaseEventModel("The Godfather"));
		top250.add(new BaseEventModel("The Godfather: Part II"));
		top250.add(new BaseEventModel("Pulp Fiction"));
		top250.add(new BaseEventModel("The Good, the Bad and the Ugly"));
		top250.add(new BaseEventModel("The Dark Knight"));
		top250.add(new BaseEventModel("12 Angry Men"));

		ArrayList<BaseEventModel> nowShowing = new ArrayList<>();
		nowShowing.add(new BaseEventModel("The Conjuring"));
		nowShowing.add(new BaseEventModel("Despicable Me 2"));
		nowShowing.add(new BaseEventModel("Turbo"));
		nowShowing.add(new BaseEventModel("Grown Ups 2"));
		nowShowing.add(new BaseEventModel("Red 2"));
		nowShowing.add(new BaseEventModel("The Wolverine"));

		ArrayList<BaseEventModel> comingSoon = new ArrayList<>();
		comingSoon.add(new BaseEventModel("2 Guns"));
		comingSoon.add(new BaseEventModel("The Smurfs 2"));
		comingSoon.add(new BaseEventModel("The Spectacular Now"));
		comingSoon.add(new BaseEventModel("The Canyons"));
		comingSoon.add(new BaseEventModel("Europa Report"));

		listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
		listDataChild.put(listDataHeader.get(1), nowShowing);
		listDataChild.put(listDataHeader.get(2), comingSoon);
	}
}
