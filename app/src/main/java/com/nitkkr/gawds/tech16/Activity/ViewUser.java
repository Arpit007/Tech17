package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nitkkr.gawds.tech16.Helper.ActionBarBack;
import com.nitkkr.gawds.tech16.Helper.ActionBarDoneButton;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.Model.UserModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.Src.CircularTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewUser extends AppCompatActivity
{

	UserModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_user);

		model=(UserModel)getIntent().getExtras().getSerializable("User");


		ActionBarDoneButton bar=new ActionBarDoneButton(ViewUser.this, new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent=new Intent(ViewUser.this,EditUser.class);
				startActivity(intent);
			}
		});
		bar.setLabel(model.getName());

		if(model.getEmail().equals(AppUserModel.MAIN_USER.getEmail()))
		{
			bar.setButtonLabel("Edit");
			bar.setButtonDrawable(R.drawable.ic_edit);
			bar.setButtonVisibility(View.VISIBLE);
		}
		else
			bar.setButtonVisibility(View.GONE);

		setUpContent();
	}

	public void setUpContent()
	{

		(( TextView)findViewById(R.id.user_Name)).setText(model.getName());
		(( TextView)findViewById(R.id.user_Email)).setText(model.getEmail());
		(( TextView)findViewById(R.id.user_College)).setText(model.getCollege());
		(( TextView)findViewById(R.id.user_Roll)).setText(model.getRoll());
		(( TextView)findViewById(R.id.user_Branch)).setText(model.getBranch());
		(( TextView)findViewById(R.id.user_Number)).setText(model.getMobile());

		if(model.getImageResource()!=null && model.isUseGoogleImage())
		{
			CircleImageView view=(CircleImageView)findViewById(R.id.view_user_Image);
			view.setVisibility(View.VISIBLE);

			Glide.with(ViewUser.this).load(model.getImageResource()).thumbnail(0.5f).centerCrop().into(view);

			findViewById(R.id.view_user_Image_Letter).setVisibility(View.INVISIBLE);
		}
		else if(model.getImageId()!=-1)
		{
			CircleImageView view=(CircleImageView)findViewById(R.id.view_user_Image);
			view.setVisibility(View.VISIBLE);

			TypedArray array=getResources().obtainTypedArray(R.array.Avatar);
			view.setImageResource(array.getResourceId(model.getImageId(),0));
			array.recycle();

			CircularTextView circularTextView=(CircularTextView)findViewById(R.id.view_user_Image_Letter);
			circularTextView.setVisibility(View.VISIBLE);
			circularTextView.setText("");
			circularTextView.setFillColor(ContextCompat.getColor(this,R.color.User_Image_Fill_Color));
		}
		else
		{
			CircularTextView view=(CircularTextView)findViewById(R.id.view_user_Image_Letter);

			view.setText(model.getName().toUpperCase().charAt(0));
			view.setVisibility(View.VISIBLE);

			TypedArray array=getResources().obtainTypedArray(R.array.Flat_Colors);

			int colorPos=(model.getName().toLowerCase().charAt(0)-'a')%array.length();

			view.setFillColor(array.getColor(colorPos,0));
			view.setBorderColor(ContextCompat.getColor(this,R.color.User_Image_Border_Color));

			array.recycle();

			findViewById(R.id.view_user_Image).setVisibility(View.GONE);
		}
	}

	@Override
	public void onBackPressed()
	{
		if(ActivityHelper.revertToHomeIfLast(ViewUser.this))
			return;
		super.onBackPressed();
	}
}
