package com.nitkkr.gawds.tech16.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Home Laptop on 03-Nov-16.
 */

public class InterestAdapter extends BaseAdapter
{
	private ArrayList<String> list;
	private boolean Selected[];
	private Context context;

	public InterestAdapter(Context context)
	{
		list=new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.Interests)));
		Selected=new boolean[list.size()];

		if(AppUserModel.MAIN_USER.isUserLoaded())
		{
			ArrayList<String> array=AppUserModel.MAIN_USER.getInterests();
			for( String string: array)
			{
				int ID=list.indexOf(string);
				if(ID!=-1)
					Selected[ID]=true;
			}
		}
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

		(( TextView)view.findViewById(R.id.interest_item_label)).setText(list.get(i));

		if(Selected[i])
			(( ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_tick);
		else ((ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_untick);

		return view;
	}

	public void onItemClick(View view, int Position)
	{
		boolean x=Selected[Position];
		if(x)
		{
			Selected[Position]=false;
			((ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_untick);
		}else{

			Selected[Position]=true;
			(( ImageView)view.findViewById(R.id.interest_item_tick)).setImageResource(R.drawable.icon_tick);
		}
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
		int length=list.size();
		for(int x=0;x<length;x++)
			if(Selected[x])
			{
				if(stringBuilder.toString().equals(""))
					stringBuilder.append(list.get(x));
				else (stringBuilder.append(",")).append(list.get(x));
			}
		return stringBuilder.toString();
	}
}
