package com.nitkkr.gawds.tech16.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.api.EventTargetType;
import com.nitkkr.gawds.tech16.api.Query;
import com.nitkkr.gawds.tech16.adapter.EventListAdapter;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.database.DbConstants;
import com.nitkkr.gawds.tech16.helper.ActionBarSearch;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.iActionBar;
import com.nitkkr.gawds.tech16.model.EventKey;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

public class ListPage extends AppCompatActivity
{
	private Query query;
	private ListView listView;
	private ArrayList<EventKey> Data =new ArrayList<>();
	private EventListAdapter listAdapter;
	ActionBarSearch actionBarSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_page);
		ActivityHelper.setCreateAnimation(this);
		ActivityHelper.setStatusBarColor(this);

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

		listView = (ListView) this.findViewById(R.id.event_list);


		if(query.getQueryTargetType()== EventTargetType.Informals)
			listAdapter = new EventListAdapter(ListPage.this, Data,false);
		else listAdapter = new EventListAdapter(ListPage.this, Data,true);
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
				listAdapter.onClick(((EventKey)listAdapter.getItem(i)).getEventID());

				Intent intent;
				Bundle bundle=new Bundle();

				//adding animation
				Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
				animation1.setDuration(1000);
				view.startAnimation(animation1);

				switch (query.getQueryTargetType())
				{
					case Event:
					case Informals:
						bundle.putSerializable("Event",(EventKey)listView.getAdapter().getItem(i));
						intent=new Intent(view.getContext(), Event.class);
						intent.putExtras(bundle);
						view.getContext().startActivity(intent);
						break;
					case Exhibition:
					case GuestTalk:
						bundle.putSerializable("Event",(EventKey)listView.getAdapter().getItem(i));
						intent=new Intent(view.getContext(), Exhibition.class);
						intent.putExtras(bundle);
						ListPage.this.startActivity(intent);
						break;
				}

			}
		});

		prepareListData();
	}

	void prepareListData()
	{
		Data = new ArrayList<>();
		if (query.getQueryTargetType() == EventTargetType.Informals)
		{
			Data = Database.getInstance().getEventsDB().getAllInformalKeys();
		}
		else if (query.getQueryTargetType() == EventTargetType.Exhibition)
		{
			Data= Database.getInstance().getExhibitionDB().getExhibitionKeys(DbConstants.ExhibitionNames.GTalk.Name() + " = 0");
		}
		else if (query.getQueryTargetType() == EventTargetType.GuestTalk)
		{
			Data= Database.getInstance().getExhibitionDB().getExhibitionKeys(DbConstants.ExhibitionNames.GTalk.Name() + " = 1");
		}
		Log.v("DEBUG",Data.toString());
		listAdapter.setEvents(Data);
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public void onBackPressed()
	{
		if(!actionBarSearch.backPressed())
		{
			ActivityHelper.revertToHomeIfLast(ListPage.this);
			ActivityHelper.setExitAnimation(this);
		}
		else
		{
			super.onBackPressed();
			ActivityHelper.setExitAnimation(this);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if(listAdapter.EventID!=-1)
			listAdapter.notifyDataSetChanged();
	}
}
