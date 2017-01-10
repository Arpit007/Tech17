package com.nitkkr.gawds.tech17.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.model.EventKey;
import com.nitkkr.gawds.tech17.model.NotificationModel;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 02-Jan-17.
 */

public class NotificationAdapter extends BaseAdapter
{

	private Context context;

	private ArrayList<NotificationModel> models;

	public NotificationAdapter(Context context, ArrayList<NotificationModel> models)
	{
		this.models=models;
		this.context = context;
	}

	public void setModels(ArrayList<NotificationModel> models)
	{
		this.models=models;
	}

	@Override
	public int getCount()
	{
		return models.size();
	}

	@Override
	public Object getItem(int i)
	{
		return null;
	}

	@Override
	public long getItemId(int i)
	{
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup)
	{
		if (view == null)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.layout_notification_event, viewGroup, false);
		}

		TextView Title=(( TextView)view.findViewById(R.id.notification_title));

		NotificationModel model=models.get(i);

		if(!model.getTitle().equals(""))
			Title.setText(model.getTitle());
		else
		{
			EventKey key= Database.getInstance().getEventsDB().getEventKey(model.getEventID());
			if(key.getEventID()==model.getEventID())
				model.setTitle(key.getEventName());
			else
			{
				key=Database.getInstance().getExhibitionDB().getExhibitionKey(model.getEventID());
				model.setTitle(key.getEventName());
			}
			if(model.getTitle().equals(""))
				model.setTitle("Notification");
			Title.setText(model.getTitle());
		}


		if(model.isSeen())
		{
			Title.setTypeface(null, Typeface.NORMAL);
			view.findViewById(R.id.unread).setVisibility(View.INVISIBLE);
		}
		else
		{
			Title.setTypeface(null,Typeface.BOLD_ITALIC);
			view.findViewById(R.id.unread).setVisibility(View.VISIBLE);
		}

		(( TextView)view.findViewById(R.id.notification_message)).setText(model.getMessage());

		return view;
	}

	public ArrayList<NotificationModel> getModels(){return models;}

}
