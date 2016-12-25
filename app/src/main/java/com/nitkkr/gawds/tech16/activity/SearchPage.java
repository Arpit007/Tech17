package com.nitkkr.gawds.tech16.activity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.adapter.SearchPageAdapter;
import com.nitkkr.gawds.tech16.helper.ActionBarSearch;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.iActionBar;

public class SearchPage extends AppCompatActivity
{
	private ActionBarSearch actionBarSearch;
	SearchPageAdapter adapter;
	ListView listView;
	String Query="";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_page);

		ActivityHelper.setCreateAnimation(this);

		ActivityHelper.setStatusBarColor(this);

		listView=(ListView)findViewById(R.id.event_list);
		adapter = new SearchPageAdapter(SearchPage.this);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				adapter.onClick(i);
			}
		});

		adapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if (adapter.getCount()==0  && !Query.isEmpty())
					findViewById(R.id.None).setVisibility(View.VISIBLE);
				else findViewById(R.id.None).setVisibility(View.INVISIBLE);
			}
		});

		actionBarSearch=new ActionBarSearch(SearchPage.this, new iActionBar()
		{
			@Override
			public void NavButtonClicked()
			{
			}

			@Override
			public void SearchQuery(String Query)
			{
				SearchPage.this.Query = Query;
				findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
				adapter.getFilter().filter(Query);
				findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
			}
		});
		actionBarSearch.setLabel(getResources().getString(R.string.FestName));
		actionBarSearch.setSearchHint("Quick Search");

		findViewById(R.id.actionbar_search).performClick();
	}

	@Override
	public void onBackPressed()
	{
		if(actionBarSearch.backPressed())
		{
			if(ActivityHelper.revertToHomeIfLast(SearchPage.this));
			else super.onBackPressed();
			ActivityHelper.setExitAnimation(this);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if(adapter.ID!=-1)
			adapter.notifyDataSetChanged();
	}
}
