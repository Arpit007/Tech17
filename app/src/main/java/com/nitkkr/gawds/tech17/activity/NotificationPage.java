package com.nitkkr.gawds.tech17.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.adapter.NotificationAdapter;
import com.nitkkr.gawds.tech17.api.FetchData;
import com.nitkkr.gawds.tech17.api.iResponseCallback;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.helper.ActionBarBack;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.model.EventKey;
import com.nitkkr.gawds.tech17.model.NotificationModel;

public class NotificationPage extends AppCompatActivity
{
	ListView listView = null;
	NotificationAdapter adapter=null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_page);
		ActivityHelper.setCreateAnimation(this);
		ActivityHelper.setStatusBarColor(this);

		ActionBarBack barBack=new ActionBarBack(NotificationPage.this);
		barBack.setLabel("Notifications");

		if(getIntent().getBooleanExtra("IsNotification",false) && isTaskRoot())
		{
			new Database(getApplicationContext());
			ActivityHelper.setUpHelper(getApplicationContext());
		}

		listView=(ListView)findViewById(R.id.notification_list);

		adapter=new NotificationAdapter(this, Database.getInstance().getNotificationDB().getAllNotifications());
		listView.setAdapter(adapter);

		((TextView)NotificationPage.this.findViewById(R.id.None)).setText("No Notifications");

		adapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				super.onChanged();
				if(adapter.getCount()==0)
					NotificationPage.this.findViewById(R.id.None).setVisibility(View.VISIBLE);
				else NotificationPage.this.findViewById(R.id.None).setVisibility(View.INVISIBLE);
			}
		});
		adapter.notifyDataSetChanged();

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				final NotificationModel model=adapter.getModels().get(i);
				if(!model.isSeen())
				{
					model.setSeen(true);
					model.setUpdated(false);
					Database.getInstance().getNotificationDB().addOrUpdateNotification(model);

					adapter.notifyDataSetChanged();
					FetchData.getInstance().changeNotificationStatus(getApplicationContext(), model.getNotificationID(), 0, new iResponseCallback()
					{
						@Override
						public void onResponse(ResponseStatus status)
						{
							if(status == ResponseStatus.SUCCESS)
							{
								model.setUpdated(true);
								Database.getInstance().getNotificationDB().addOrUpdateNotification(model);
							}
						}

						@Override
						public void onResponse(ResponseStatus status, Object object)
						{
							this.onResponse(status);
						}
					});
				}

				final Dialog dialog=new Dialog(NotificationPage.this);

				dialog.setContentView(R.layout.layout_notification_dialog);
				dialog.setTitle(model.getTitle());
				dialog.setCancelable(true);

				(( TextView)dialog.findViewById(R.id.notification_message)).setText(model.getMessage());

				if(model.getEventID()<=0)
					dialog.findViewById(R.id.notification_View).setVisibility(View.GONE);
				else
				{
					dialog.findViewById(R.id.notification_View).setVisibility(View.VISIBLE);
					dialog.findViewById(R.id.notification_View).setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View view)
						{
							EventKey key= Database.getInstance().getEventsDB().getEventKey(model.getEventID());
							if(key.getEventID()==model.getEventID())
							{
								Bundle bundle = new Bundle();
								bundle.putSerializable("Event", key);
								Intent intent = new Intent(NotificationPage.this, Event.class);
								intent.putExtras(bundle);
								dialog.dismiss();
								view.getContext().startActivity(intent);
							}
							else
							{
								key=Database.getInstance().getExhibitionDB().getExhibitionKey(model.getEventID());
								Bundle bundle = new Bundle();
								bundle.putSerializable("Event", key);
								Intent intent = new Intent(NotificationPage.this, Exhibition.class);
								intent.putExtras(bundle);
								dialog.dismiss();
								view.getContext().startActivity(intent);
							}
						}
					});
				}
				dialog.findViewById(R.id.notification_Dismiss).setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});

	}

	@Override
	public void onBackPressed()
	{
		if(getIntent().getBooleanExtra("IsNotification",false) && isTaskRoot())
		{
			startActivity(new Intent(this,Splash.class));
		}
		else if(!ActivityHelper.revertToHomeIfLast(this))
			super.onBackPressed();
	}
}
