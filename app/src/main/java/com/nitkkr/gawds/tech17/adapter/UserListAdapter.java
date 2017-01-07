package com.nitkkr.gawds.tech17.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.model.AppUserModel;
import com.nitkkr.gawds.tech17.model.UserKey;
import com.nitkkr.gawds.tech17.src.CircularTextView;
import com.nitkkr.gawds.tech17.src.CompatCircleImageView;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 06-Jan-17.
 */

public class UserListAdapter extends BaseAdapter
{
	private ArrayList<UserKey> users;
	private Context context;
	private boolean cross;
	private int ResourceID;

	public UserListAdapter(ArrayList<UserKey> users, Context context, boolean showCross, int resourceID)
	{
		cross = showCross;

		this.ResourceID = resourceID;

		this.context = context;
		this.users=users;
	}

	@Override
	public int getCount()
	{
		return users.size();
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
	public View getView(final int i, View view, ViewGroup viewGroup)
	{
		if (view == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(ResourceID, null);
		}

		String[] strArray = users.get(i).getName().split(" ");
		StringBuilder builder = new StringBuilder();
		for (String s : strArray)
		{
			String cap = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
			builder.append(cap + " ");
		}

		(( TextView)view.findViewById(R.id.user_name)).setText(builder.toString());
		setImage(view,users.get(i));

		if(!cross || i==0)
			view.findViewById(R.id.Cross).setVisibility(View.INVISIBLE);
		else
		{
			view.findViewById(R.id.Cross).setVisibility(View.VISIBLE);
			view.findViewById(R.id.Cross).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					users.remove(i);
					notifyDataSetChanged();
				}
			});
		}

		return view;
	}

	public ArrayList<UserKey> getUsers() {return users;}

	public void setUsers(ArrayList<UserKey> users) {this.users = users; notifyDataSetChanged();}

	private void setImage(View view1, UserKey key)
	{
		if(AppUserModel.MAIN_USER.getName().equals(key.getName()))
		{
			if (AppUserModel.MAIN_USER.getImageResource() != null && AppUserModel.MAIN_USER.isUseGoogleImage())
			{
				CompatCircleImageView view = (CompatCircleImageView) view1.findViewById(R.id.User_Image);
				view.setVisibility(View.VISIBLE);

				Glide.with(context).load(AppUserModel.MAIN_USER.getImageResource()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f).centerCrop().into(view);

				view1.findViewById(R.id.User_Image_Letter).setVisibility(View.INVISIBLE);
				view1.findViewById(R.id.temp_user_Image_Letter).setVisibility(View.INVISIBLE);
			}
			else if (AppUserModel.MAIN_USER.getImageId() != -1)
			{
				CompatCircleImageView view = (CompatCircleImageView) view1.findViewById(R.id.User_Image);
				view.setVisibility(View.VISIBLE);

				TypedArray array = context.getResources().obtainTypedArray(R.array.Avatar);
				view.setImageResource(array.getResourceId(AppUserModel.MAIN_USER.getImageId(), 0));
				array.recycle();

				CircularTextView circularTextView = (CircularTextView) view1.findViewById(R.id.User_Image_Letter);
				circularTextView.setVisibility(View.INVISIBLE);
				circularTextView = (CircularTextView) view1.findViewById(R.id.temp_user_Image_Letter);
				circularTextView.setVisibility(View.VISIBLE);
				circularTextView.setFillColor(ContextCompat.getColor(context, R.color.User_Image_Fill_Color));
			}
		}
		else
		{
			CircularTextView view = (CircularTextView) view1.findViewById(R.id.User_Image_Letter);

			if (key.getName().isEmpty())
			{
				view.setText("#");
			}
			else
			{
				view.setText(String.valueOf(key.getName().trim().toUpperCase().charAt(0)));
			}

			view.setVisibility(View.VISIBLE);

			TypedArray array = view1.getResources().obtainTypedArray(R.array.Flat_Colors);

			int colorPos;
			if (key.getName().isEmpty())
			{
				colorPos = Math.abs(( '#' - 'a' )) % array.length();
			}
			else
			{
				colorPos = Math.abs(key.getName().trim().toLowerCase().charAt(0) - 'a') % array.length();
			}

			view.setFillColor(array.getColor(colorPos, 0));
			view.setBorderWidth(2);
			view.setBorderColor(ContextCompat.getColor(context, R.color.User_Image_Border_Color));

			array.recycle();

			view1.findViewById(R.id.User_Image).setVisibility(View.GONE);
			view1.findViewById(R.id.temp_user_Image_Letter).setVisibility(View.INVISIBLE);
		}
	}
}
