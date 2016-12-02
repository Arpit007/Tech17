package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.API.Query;
import com.nitkkr.gawds.tech16.Adapter.EventListAdapter;
import com.nitkkr.gawds.tech16.Helper.ActionBarSearch;
import com.nitkkr.gawds.tech16.Helper.iActionBar;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

public class ListPage extends AppCompatActivity
{
	private Query query;
	private ListView listView;
	private ArrayList<String> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_page);

		ActionBarSearch actionBarSearch=new ActionBarSearch(ListPage.this, new iActionBar()
		{
			@Override
			public void NavButtonClicked()
			{
				//-----------------No Need Here-------------------------------------
			}

			@Override
			public void SearchButtonClicked()
			{

			}
		});

		String Label=getIntent().getStringExtra("Label");
		this.query=(Query) getIntent().getSerializableExtra("Query");

		actionBarSearch.setLabel(Label);


		listView = (ListView) this.findViewById(R.id.event_list);

		prepareListData();

		final EventListAdapter listAdapter = new EventListAdapter(ListPage.this, listDataChild);
		listAdapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if(listAdapter.getCount()==0)
				{
					findViewById(R.id.None).setVisibility(View.VISIBLE);
				}
				else findViewById(R.id.None).setVisibility(View.INVISIBLE);
				super.onChanged();
			}
		});

		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				String Data=(String)(listView.getAdapter()).getItem(i);

				//TODO:Implemet================================

				switch (query.getQueryTargetType())
				{
					case Event:
						view.getContext().startActivity(new Intent(view.getContext(), Event.class));
						break;
					case Exhibition:
						view.getContext().startActivity(new Intent(view.getContext(), Exhibition.class));
						break;
					case GuestTalk:
						view.getContext().startActivity(new Intent(view.getContext(), Exhibition.class));
						break;
				}
			}
		});
	}

	void prepareListData()
	{
		//===================Handle===================================
		listDataChild=new ArrayList<>();
		listDataChild.add("Hello");
		listDataChild.add("World");
	}
}
