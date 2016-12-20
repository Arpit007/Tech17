package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.API.Query;
import com.nitkkr.gawds.tech16.Adapter.EventListAdapter;
import com.nitkkr.gawds.tech16.Database.Database;
import com.nitkkr.gawds.tech16.Database.DbConstants;
import com.nitkkr.gawds.tech16.Helper.ActionBarSearch;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.iActionBar;
import com.nitkkr.gawds.tech16.Model.EventKey;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

public class ListPage extends AppCompatActivity
{
	private Query query;
	private ListView listView;
	private ArrayList<EventKey> Data;
	private EventListAdapter listAdapter;
	ActionBarSearch actionBarSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_page);

		actionBarSearch=new ActionBarSearch(ListPage.this, new iActionBar()
		{
			@Override
			public void NavButtonClicked()
			{
			}

			@Override
			public void SearchQuery(String Query)
			{
				listAdapter.getFilter().filter(Query);
			}
		});

		String Label=getIntent().getExtras().getString("Label");
		this.query=(Query) getIntent().getExtras().getSerializable("Query");

		actionBarSearch.setLabel(Label);
		actionBarSearch.setSearchHint(Label);


		listView = (ListView) this.findViewById(R.id.event_list);

		prepareListData();

		listAdapter = new EventListAdapter(ListPage.this, Data);
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

				//adding animation
				Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
				animation1.setDuration(1000);
				view.startAnimation(animation1);

				switch (query.getQueryTargetType())
				{
					case Informals:
						bundle.putSerializable("Event",(EventKey)listView.getAdapter().getItem(i));
						intent=new Intent(view.getContext(), Event.class);
						intent.putExtras(bundle);
						view.getContext().startActivity(intent);
						break;
					case Exhibition:
						bundle.putSerializable("Event",(EventKey)listView.getAdapter().getItem(i));
						intent=new Intent(view.getContext(), Exhibition.class);
						intent.putExtras(bundle);
						ListPage.this.startActivity(intent);
						break;
					case GuestTalk:
						bundle.putSerializable("Event",(EventKey)listView.getAdapter().getItem(i));
						intent=new Intent(view.getContext(), Exhibition.class);
						intent.putExtras(bundle);
						ListPage.this.startActivity(intent);
						break;
				}

			}
		});
	}

	void prepareListData()
	{
		Data = new ArrayList<>();
		if (query.getQueryTargetType() == Query.QueryTargetType.Informals)
		{
			//===================TODO: IMPLEMENT=================
		}
		else if (query.getQueryTargetType() == Query.QueryTargetType.Exhibition)
		{
			Data= Database.database.getExhibitionDB().getExhibitionKeys(DbConstants.ExhibitionNames.GTalk + " = 0");
		}
		else if (query.getQueryTargetType() == Query.QueryTargetType.GuestTalk)
		{
			Data= Database.database.getExhibitionDB().getExhibitionKeys(DbConstants.ExhibitionNames.GTalk + " = 1");
		}
	}

	@Override
	public void onBackPressed()
	{
		if(!actionBarSearch.backPressed())
		{
			ActivityHelper.revertToHomeIfLast(ListPage.this);
		}
		else
		super.onBackPressed();
	}
}
