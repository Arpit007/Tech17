package com.nitkkr.gawds.tech16.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.R;

/**
 * Created by Home Laptop on 03-Nov-16.
 */

public class InterestAdapter extends BaseAdapter
{
	private String list[];
	private boolean Selected[];
	private Context context;

	public InterestAdapter(Context context)
	{
		list=context.getResources().getStringArray(R.array.Interests);
		Selected=new boolean[list.length];

		this.context=context;
	}

	@Override
	public int getCount()
	{
		return list.length;
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
			view=inflater.inflate(R.layout.layout_interest_list_item,viewGroup,false);
		}

		(( TextView)view.findViewById(R.id.interest_item_label)).setText(list[i]);

		if(Selected[i])
			(( ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_tick);
		else ((ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_untick);

		return view;
	}

	public void onItemClick(View view, int Position)
	{
		if(Selected[Position])
			(( ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_tick);
		else ((ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_untick);
	}

	public boolean isDone()
	{
		for(int x=0;x<Selected.length;x++)
			if(Selected[x])
				return true;
		return false;
	}

	public String getInterestsString()
	{
		StringBuilder stringBuilder=new StringBuilder("");

		for(int x=0;x<list.length;x++)
			if(Selected[x])
			{
				if(stringBuilder.toString().equals(""))
					stringBuilder.append(list[x]);
				else (stringBuilder.append(",")).append(list[x]);
			}
		return stringBuilder.toString();
	}
}
