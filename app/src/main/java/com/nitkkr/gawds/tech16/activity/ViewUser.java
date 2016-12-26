package com.nitkkr.gawds.tech16.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.helper.ActionBarDoneButton;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.model.AppUserModel;
import com.nitkkr.gawds.tech16.model.UserModel;
import com.nitkkr.gawds.tech16.src.CircularTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewUser extends AppCompatActivity
{
	private final int EDIT = 10;
	UserModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_user);
		ActivityHelper.setCreateAnimation(this);
		ActivityHelper.setStatusBarColor(this);

		model = (UserModel) getIntent().getExtras().getSerializable("User");

		ActionBarDoneButton bar = new ActionBarDoneButton(ViewUser.this, new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(ViewUser.this, EditUser.class);
				startActivityForResult(intent, EDIT);
			}
		});
		bar.setLabel(model.getName());

		if (model.getEmail().equals(AppUserModel.MAIN_USER.getEmail()))
		{
			bar.setButtonLabel("Edit");
			bar.setButtonDrawable(R.drawable.ic_edit_white);
			bar.setButtonVisibility(View.VISIBLE);
		}
		else
		{
			bar.setButtonVisibility(View.GONE);
		}

		setUpContent();
	}

	public void setUpContent()
	{

		( (TextView) findViewById(R.id.user_Name) ).setText(model.getName());
		( (TextView) findViewById(R.id.user_Email) ).setText(model.getEmail());
		( (TextView) findViewById(R.id.user_College) ).setText(model.getCollege());
		( (TextView) findViewById(R.id.user_Roll) ).setText(model.getRoll());
		( (TextView) findViewById(R.id.user_Branch) ).setText(model.getBranch());
		( (TextView) findViewById(R.id.user_Year) ).setText(model.getYear());
		( (TextView) findViewById(R.id.user_Number) ).setText(model.getMobile());

		if (model.getImageResource() != null && model.isUseGoogleImage())
		{
			CircleImageView view = (CircleImageView) findViewById(R.id.view_user_Image);
			view.setVisibility(View.VISIBLE);

			Glide.with(ViewUser.this).load(model.getImageResource()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f).centerCrop().into(view);

			findViewById(R.id.view_user_Image_Letter).setVisibility(View.INVISIBLE);
			findViewById(R.id.temp_user_Image_Letter).setVisibility(View.INVISIBLE);
		}
		else if (model.getImageId() != -1)
		{
			CircleImageView view = (CircleImageView) findViewById(R.id.view_user_Image);
			view.setVisibility(View.VISIBLE);

			TypedArray array = getResources().obtainTypedArray(R.array.Avatar);
			view.setImageResource(array.getResourceId(model.getImageId(), 0));
			array.recycle();

			CircularTextView circularTextView = (CircularTextView) findViewById(R.id.view_user_Image_Letter);
			circularTextView.setVisibility(View.INVISIBLE);
			circularTextView = (CircularTextView) findViewById(R.id.temp_user_Image_Letter);
			circularTextView.setVisibility(View.VISIBLE);
			circularTextView.setFillColor(ContextCompat.getColor(this, R.color.User_Image_Fill_Color));
		}
		else
		{
			CircularTextView view = (CircularTextView) findViewById(R.id.view_user_Image_Letter);

			if (model.getName().isEmpty())
			{
				view.setText("#");
			}
			else
			{
				view.setText(String.valueOf(model.getName().trim().toUpperCase().charAt(0)));
			}

			view.setVisibility(View.VISIBLE);

			TypedArray array = getResources().obtainTypedArray(R.array.Flat_Colors);

			int colorPos;
			if (model.getName().isEmpty())
			{
				colorPos = Math.abs(( '#' - 'a' )) % array.length();
			}
			else
			{
				colorPos = Math.abs(model.getName().trim().toLowerCase().charAt(0) - 'a') % array.length();
			}

			view.setFillColor(array.getColor(colorPos, 0));
			view.setBorderWidth(2);
			view.setBorderColor(ContextCompat.getColor(this, R.color.User_Image_Border_Color));

			array.recycle();

			findViewById(R.id.view_user_Image).setVisibility(View.GONE);
			findViewById(R.id.temp_user_Image_Letter).setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onBackPressed()
	{

		if (ActivityHelper.revertToHomeIfLast(ViewUser.this))
		{
			;
		}
		else
		{
			super.onBackPressed();
		}
		ActivityHelper.setExitAnimation(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == EDIT)
		{
			model = AppUserModel.MAIN_USER;
			setUpContent();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
