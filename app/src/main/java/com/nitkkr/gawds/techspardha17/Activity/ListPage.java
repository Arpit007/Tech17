package com.nitkkr.gawds.techspardha17.Activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.techspardha17.API.Query;
import com.nitkkr.gawds.techspardha17.Adapter.EventListAdapter;
import com.nitkkr.gawds.techspardha17.Helper.ActionBarSearch;
import com.nitkkr.gawds.techspardha17.Helper.iActionBar;
import com.nitkkr.gawds.techspardha17.Model.BaseEventModel;
import com.nitkkr.gawds.techspardha17.R;

import java.util.ArrayList;

public class ListPage extends AppCompatActivity
{
	private Query query;
	private ListView listView;
	private ArrayList<BaseEventModel> listDataChild;

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

		String Label=getIntent().getExtras().getString("Label");
		this.query=(Query) getIntent().getExtras().getSerializable("Query");

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
				Intent intent;
				Bundle bundle=new Bundle();

				switch (query.getQueryTargetType())
				{
					case Event:
						bundle.putSerializable("Event",(BaseEventModel)listView.getAdapter().getItem(i));
						intent=new Intent(view.getContext(), Event.class);
						intent.putExtras(bundle);
						view.getContext().startActivity(intent);
						break;
					case Exhibition:
					case GuestTalk:
						bundle.putSerializable("Exhibition",(BaseEventModel)listView.getAdapter().getItem(i));
						intent=new Intent(view.getContext(), Exhibition.class);
						intent.putExtras(bundle);
						view.getContext().startActivity(intent);
						break;
				}
			}
		});
	}

	void prepareListData()
	{
		//===================Handle===================================

		listDataChild=new ArrayList<>();
		listDataChild.add(new BaseEventModel("Hello"));
		listDataChild.add(new BaseEventModel("World"));
	}
}
