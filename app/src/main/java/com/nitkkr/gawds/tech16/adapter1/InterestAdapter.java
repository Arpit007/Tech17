package com.nitkkr.gawds.tech16.adapter1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.database1.Database;
import com.nitkkr.gawds.tech16.model1.InterestModel;
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
		list= Database.database.getInterestDB().getAllInterests();
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
		list.get(Position).setSelected(!list.get(Position).isSelected());
		if(list.get(Position).isSelected())
		{
			((ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_untick);
		}else
		{
			(( ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_tick);
		}
	}

	public boolean isDone()
	{
		int length=list.size();
		for(int x=0;x<length;x++)
			if(list.get(x).isSelected())
				return true;
		return false;
	}

	public String getInterestsString()
	{
		//TODO:Set Token
		String Token=",";
		StringBuilder stringBuilder=new StringBuilder("");
		int length=list.size();
		for(int x=0;x<length;x++)
		{
			if (list.get(x).isSelected())
			{
				if (stringBuilder.toString().equals(""))
					stringBuilder.append(list.get(x).getID());
				else ( stringBuilder.append(Token) ).append(list.get(x).getID());
			}
		}
		return stringBuilder.toString();
	}

	public ArrayList<InterestModel> getFinalList(){return list;}
}
