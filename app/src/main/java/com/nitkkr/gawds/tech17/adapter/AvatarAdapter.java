package com.nitkkr.gawds.tech17.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.src.CircularTextView;
import com.nitkkr.gawds.tech17.src.CompatCircleImageView;

/**
 * Created by Home Laptop on 16-Dec-16.
 */

public class AvatarAdapter extends BaseAdapter
{
	private TypedArray array;
	private Context context;

	public AvatarAdapter(Context context)
	{
		array = context.getResources().obtainTypedArray(R.array.Avatar);
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return array.length();
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
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.layout_avatar, null);
		}
		CompatCircleImageView CircleImageView = (CompatCircleImageView) view.findViewById(R.id.avatar_image);
		CircleImageView.setImageResource(array.getResourceId(i, 0));

		CircularTextView circularTextView = (CircularTextView) view.findViewById(R.id.circ_view);
		circularTextView.setFillColor(ContextCompat.getColor(context, R.color.User_Image_Fill_Color));

		return view;
	}

	@Override
	protected void finalize() throws Throwable
	{
		array.recycle();
		super.finalize();
	}
}
