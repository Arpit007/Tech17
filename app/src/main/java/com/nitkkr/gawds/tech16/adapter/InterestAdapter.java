package com.nitkkr.gawds.tech16.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.model.InterestModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 03-Nov-16.
 */

public class InterestAdapter extends BaseAdapter
{
	private ArrayList<InterestModel> list;
	private Context context;

	public InterestAdapter(Context context)
	{
		list= Database.getInstance().getInterestDB().getAllInterests();
		this.context=context;
	}

	public InterestAdapter(Context context, ArrayList<InterestModel> keys)
	{
		list= keys;
		this.context=context;
	}

	@Override
	public int getCount()
	{
		return list.size();
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
		if(view==null)
		{
			LayoutInflater inflater=LayoutInflater.from(context);
			view=inflater.inflate(R.layout.layout_list_item_interest,viewGroup,false);
		}

		(( TextView)view.findViewById(R.id.interest_item_label)).setText(list.get(i).getInterest());

		if(list.get(i).isSelected())
			(( ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_tick);
		else ((ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_untick);

		return view;
	}

	public void onItemClick(View view, int Position)
	{
		list.get(Position).setSelected(!(list.get(Position).isSelected()));
		if(list.get(Position).isSelected())
		{
			((ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_tick);
		}
		else
		{
			(( ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_untick);
		}
	}

	public boolean isDone()
	{
		for(InterestModel model: list)
		{
			if (model.isSelected())
				return true;
		}
		return false;
	}

	public ArrayList<InterestModel> getFinalList(){return list;}
}
