package com.nitkkr.gawds.tech17.activity;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.adapter.UserListAdapter;
import com.nitkkr.gawds.tech17.api.FetchData;
import com.nitkkr.gawds.tech17.api.iResponseCallback;
import com.nitkkr.gawds.tech17.helper.ActionBarSearch;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.helper.iActionBar;
import com.nitkkr.gawds.tech17.model.UserKey;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 08-Jan-17.
 */

public class UserSearch extends AppCompatActivity
{
	private ActionBarSearch actionBarSearch;
	UserListAdapter adapter;
	ListView listView;
	String Query = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_page);

		ActivityHelper.setCreateAnimation(this);

		ActivityHelper.setStatusBarColor(this);

		listView = (ListView) findViewById(R.id.event_list);
		adapter = new UserListAdapter(new ArrayList<UserKey>(),this,false,R.layout.layout_create_user_item);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);

				UserKey key=adapter.getUsers().get(i);
				Intent result=new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("User",key);
				result.putExtras(bundle);
				setResult(RESULT_OK,result);
				finish();
				ActivityHelper.setExitAnimation(UserSearch.this);
			}
		});

		findViewById(R.id.None1).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(!ActivityHelper.isDebugMode(UserSearch.this))
					Answers.getInstance().logCustom(new CustomEvent("Invite"));

				Intent waIntent = new Intent(Intent.ACTION_SEND);
				waIntent.setType("text/plain");
				String text = getString(R.string.Invite_Message) + UserSearch.this.getPackageName();

				waIntent.putExtra(Intent.EXTRA_TEXT, text);
				UserSearch.this.startActivity(Intent.createChooser(waIntent, "Share with"));
			}
		});

		adapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if (adapter.getCount() == 0)
				{
					if (!Query.isEmpty())
					{
						( (TextView) findViewById(R.id.None) ).setText("User not Found");
						findViewById(R.id.None).setVisibility(View.VISIBLE);
						findViewById(R.id.None1).setVisibility(View.VISIBLE);
					}
					else
					{
						( (TextView) findViewById(R.id.None) ).setText("Search Users using their\nName/ Roll Number/ Email/\nPhone Number");
						findViewById(R.id.None).setVisibility(View.VISIBLE);
						findViewById(R.id.None1).setVisibility(View.INVISIBLE);
					}
				}
				else
				{
					findViewById(R.id.None).setVisibility(View.INVISIBLE);
					findViewById(R.id.None1).setVisibility(View.INVISIBLE);
				}
			}
		});

		actionBarSearch = new ActionBarSearch(UserSearch.this, new iActionBar()
		{
			@Override
			public void NavButtonClicked()
			{
			}

			@Override
			public void SearchQuery(final String Query)
			{
				UserSearch.this.Query = Query;
				findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
				adapter.setUsers(new ArrayList<UserKey>());
				findViewById(R.id.None).setVisibility(View.INVISIBLE);
				findViewById(R.id.None1).setVisibility(View.INVISIBLE);
				if(Query.equals(""))
				{
					findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
					adapter.notifyDataSetChanged();
					return;
				}

				FetchData.getInstance().searchUsers(UserSearch.this, Query, new iResponseCallback()
				{
					@Override
					public void onResponse(ResponseStatus status)
					{
						this.onResponse(status,null);
					}

					@Override
					public void onResponse(ResponseStatus status, Object object)
					{
						if(Query != UserSearch.this.Query)
							return;

						if(status==ResponseStatus.SUCCESS && object!=null)
						{
							adapter.setUsers((ArrayList<UserKey>)object);
						}
						else if(status == ResponseStatus.FAILED)
						{
							Toast.makeText(UserSearch.this,"Search Failed",Toast.LENGTH_SHORT).show();
						}
						else if(status == ResponseStatus.NONE)
						{
							Toast.makeText(UserSearch.this,"No Network Connection",Toast.LENGTH_SHORT).show();
						}

						findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
					}
				});
			}
		});
		actionBarSearch.setResetOnBack(false);
		actionBarSearch.setLabel("User Search");
		actionBarSearch.setSearchHint("Name/ Roll/ Email/ Phone");

		findViewById(R.id.actionbar_search).performClick();
	}

	@Override
	public void onBackPressed()
	{
		if (actionBarSearch.backPressed())
		{
			if (!ActivityHelper.revertToHomeIfLast(UserSearch.this))
			{
				super.onBackPressed();
			}
			ActivityHelper.setExitAnimation(this);
		}
	}
}
