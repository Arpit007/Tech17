package com.nitkkr.gawds.tech16.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.adapter.EventListAdapter;
import com.nitkkr.gawds.tech16.api.FetchData;
import com.nitkkr.gawds.tech16.api.iResponseCallback;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.helper.ActionBarSearch;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.ResponseStatus;
import com.nitkkr.gawds.tech16.helper.iActionBar;
import com.nitkkr.gawds.tech16.model.EventKey;
import com.nitkkr.gawds.tech16.model.EventModel;

import java.util.ArrayList;

public class DashboardPage extends AppCompatActivity
{
	private Page page;
	private EventListAdapter eventAdapter;
	private ListView listView;
	private ActionBarSearch barBack;

	enum Page
	{
		Live(1),
		Wishlist(2),
		Notification(3),
		Interest(4);

		int value;

		Page(int Value)
		{
			value = Value;
		}

		static public Page Parse(int value)
		{
			switch (value)
			{
				case 2:
					return Wishlist;
				case 3:
					return Notification;
				case 4:
					return Interest;
				default:
					return Live;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		ActivityHelper.setCreateAnimation(this);
		ActivityHelper.setStatusBarColor(DashboardPage.this);

		barBack = new ActionBarSearch(DashboardPage.this, new iActionBar()
		{
			@Override
			public void NavButtonClicked()
			{
			}

			@Override
			public void SearchQuery(String Query)
			{
				if (page != Page.Notification)
				{
					eventAdapter.getFilter().filter(Query);
				}
			}
		});

		int ID = getIntent().getIntExtra("Navigation", -1);
		if (ID == -1)
		{
			finish();
			ActivityHelper.setExitAnimation(this);
			return;
		}

		page = Page.Parse(ID);

		if (page != Page.Notification)
		{
			findViewById(R.id.notification_list).setVisibility(View.GONE);
			eventAdapter = new EventListAdapter(DashboardPage.this, new ArrayList<EventKey>(), page == Page.Wishlist);
			listView = (ListView) findViewById(R.id.event_list);
			listView.setVisibility(View.VISIBLE);
			listView.setAdapter(eventAdapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
				{
					eventAdapter.onClick(( (EventKey) eventAdapter.getItem(i) ).getEventID());
					if (page == Page.Wishlist)
					{
						Bundle bundle = new Bundle();
						bundle.putSerializable("Event", (EventKey) eventAdapter.getItem(i));
						Intent intent = new Intent(view.getContext(), Exhibition.class);
						intent.putExtras(bundle);
						DashboardPage.this.startActivity(intent);
					}
					else
					{
						Bundle bundle = new Bundle();
						bundle.putSerializable("Event", (EventKey) eventAdapter.getItem(i));
						Intent intent = new Intent(view.getContext(), Event.class);
						intent.putExtras(bundle);
						DashboardPage.this.startActivity(intent);
					}
				}
			});
		}
		else
		{
			findViewById(R.id.event_list).setVisibility(View.GONE);
			listView = (ListView) findViewById(R.id.notification_list);
			listView.setVisibility(View.VISIBLE);
		}

		listView.getAdapter().registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				if (listView.getAdapter().getCount() == 0)
				{
					findViewById(R.id.None).setVisibility(View.VISIBLE);
				}
				else
				{
					findViewById(R.id.None).setVisibility(View.GONE);
				}
			}
		});

		switch (page)
		{
			case Live:
				barBack.setLabel("Live Events");
				loadLiveEvents();
				break;
			case Notification:
				barBack.setLabel("Notifications");
				loadNotificationEvents();
				barBack.setSearchButtonVisibility(View.GONE);
				break;
			case Interest:
				barBack.setLabel("Interested Events");
				loadInterestedEvents();
				break;
			case Wishlist:
				loadWishlistEvents();
				barBack.setLabel("Wishlist Events");
				break;
		}
	}

	private void loadLiveEvents()
	{
		final ProgressDialog dialog = new ProgressDialog(DashboardPage.this);
		dialog.setIndeterminate(true);
		dialog.setMessage("Fetching Data");
		dialog.show();

		FetchData.getInstance().getLiveEvents(getBaseContext(), new iResponseCallback()
		{
			@Override
			public void onResponse(ResponseStatus status)
			{
				this.onResponse(status, null);
			}

			@Override
			public void onResponse(ResponseStatus status, Object object)
			{
				if (status == ResponseStatus.SUCCESS && object != null)
				{
					ArrayList<EventModel> models = (ArrayList<EventModel>) object;
					ArrayList<EventKey> keys = new ArrayList<>(models.size());

					for (EventModel model : models)
					{
						keys.add(model);
					}
					eventAdapter.setEvents(keys);
				}
				else if (status == ResponseStatus.NONE)
				{
					Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Failed to Fetch Live Events", Toast.LENGTH_LONG).show();
				}
				eventAdapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});

		eventAdapter.notifyDataSetChanged();
	}

	private void loadNotificationEvents()
	{

	}

	private void loadInterestedEvents()
	{
		final ProgressDialog dialog = new ProgressDialog(DashboardPage.this);
		dialog.setIndeterminate(true);
		dialog.setMessage("Fetching Data");
		dialog.show();

		FetchData.getInstance().fetchInterestedEvents(getBaseContext(), new iResponseCallback()
		{
			@Override
			public void onResponse(ResponseStatus status)
			{
				this.onResponse(status, null);
			}

			@Override
			public void onResponse(ResponseStatus status, Object object)
			{
				if (status == ResponseStatus.SUCCESS && object != null)
				{
					ArrayList<EventKey> keys = (ArrayList<EventKey>) object;
					eventAdapter.setEvents(keys);
				}
				else if (status == ResponseStatus.NONE)
				{
					Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Failed to Interested Events", Toast.LENGTH_LONG).show();
				}
				eventAdapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});

		eventAdapter.notifyDataSetChanged();
	}

	private void loadWishlistEvents()
	{
		eventAdapter.setEvents(Database.getInstance().getExhibitionDB().getRegisteredExhibitionKeys());
		eventAdapter.notifyDataSetChanged();
	}

	@Override
	public void onBackPressed()
	{
		if (barBack.backPressed())
		{
			super.onBackPressed();
			ActivityHelper.setExitAnimation(this);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (eventAdapter != null && eventAdapter.EventID != -1)
		{
			eventAdapter.notifyDataSetChanged();
		}
	}
}
