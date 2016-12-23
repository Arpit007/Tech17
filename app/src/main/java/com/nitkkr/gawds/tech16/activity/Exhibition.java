package com.nitkkr.gawds.tech16.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nitkkr.gawds.tech16.api.FetchData;
import com.nitkkr.gawds.tech16.api.iResponseCallback;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.helper.ActionBarBack;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.ResponseStatus;
import com.nitkkr.gawds.tech16.model.AppUserModel;
import com.nitkkr.gawds.tech16.model.EventKey;
import com.nitkkr.gawds.tech16.model.ExhibitionModel;
import com.nitkkr.gawds.tech16.R;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class Exhibition extends AppCompatActivity
{
	ExhibitionModel model;
	ActionBarBack actionBarBack;
	ProgressDialog progressDialog=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exhibition);

		ActivityHelper.setStatusBarColor(this);

		actionBarBack = new ActionBarBack(Exhibition.this);

		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(getIntent().getExtras().getInt("NotificationID"));

		final EventKey key = (EventKey) getIntent().getExtras().getSerializable("Event");
		model=Database.getInstance().getExhibitionDB().getExhibition(key);
		LoadExhibition();

		final Button fab = (Button) findViewById(R.id.exhibition_notify);

		if(model.isGTalk())
		{
			FetchData.getInstance().getGTalk(getApplicationContext(), key, new iResponseCallback()
			{
				@Override
				public void onResponse(ResponseStatus status)
				{
				}

				@Override
				public void onResponse(ResponseStatus status, Object object)
				{
					if (status == ResponseStatus.SUCCESS)
					{
						model = (ExhibitionModel) object;
						Database.getInstance().getExhibitionDB().addOrUpdateExhibition(model);
						LoadExhibition();
					}
				}
			});
		}
		else
		{
			//TODO:Get Exhibition
		}

		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(!ActivityHelper.isInternetConnected())
				{
					Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
					return;
				}

				if (!AppUserModel.MAIN_USER.isUserLoggedIn(Exhibition.this))
				{
					Snackbar.make(findViewById(android.R.id.content), "Login Required", Snackbar.LENGTH_SHORT)
							.setAction("Login", new View.OnClickListener()
							{
								@Override
								public void onClick(View view)
								{
									AppUserModel.MAIN_USER.LoginUserNoHome(Exhibition.this,false);
								}
							})
							.setActionTextColor(ContextCompat.getColor(Exhibition.this,R.color.neon_green))
							.show();
					return;
				}

				progressDialog=new ProgressDialog(Exhibition.this);
				progressDialog.setMessage("Updating Changes");
				progressDialog.setIndeterminate(true);
				progressDialog.show();
				if(model.isNotify())
				{
					FetchData.getInstance().removeFromWishlist(getApplicationContext(), key, new iResponseCallback()
					{
						@Override
						public void onResponse(ResponseStatus status)
						{
							if(status==ResponseStatus.SUCCESS)
							{
								Toast.makeText(getApplicationContext(),"Removed Wishlist Successfully",Toast.LENGTH_LONG).show();
								fab.setText("Add to Wishlist");
								model.setNotify(false);
								Database.getInstance().getExhibitionDB().addOrUpdateExhibition(model);
								Database.getInstance().getNotificationDB().UpdateTable();
							}
							else if(status==ResponseStatus.FAILED)
							{
								Toast.makeText(getApplicationContext(),"Failed to Remove from Wishlist",Toast.LENGTH_LONG).show();
							}
							else Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
							if(progressDialog!=null && progressDialog.isShowing())
								progressDialog.dismiss();
						}

						@Override
						public void onResponse(ResponseStatus status, Object object)
						{
							this.onResponse(status);
						}
					});
				}
				else
				{
					FetchData.getInstance().addToWishlist(getApplicationContext(), key, new iResponseCallback()
					{
						@Override
						public void onResponse(ResponseStatus status)
						{
							if(status==ResponseStatus.SUCCESS)
							{
								Toast.makeText(getApplicationContext(),"Added to Wishlist Successfully",Toast.LENGTH_LONG).show();
								fab.setText("Wishlisted");
								model.setNotify(true);
								Database.getInstance().getExhibitionDB().addOrUpdateExhibition(model);
								Database.getInstance().getNotificationDB().UpdateTable();
							}
							else if(status==ResponseStatus.FAILED)
							{
								Toast.makeText(getApplicationContext(),"Failed to Add to Wishlist",Toast.LENGTH_LONG).show();
							}
							else Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
							if(progressDialog!=null && progressDialog.isShowing())
								progressDialog.dismiss();
						}

						@Override
						public void onResponse(ResponseStatus status, Object object)
						{
							this.onResponse(status);
						}
					});
				}
			}
		});
	}

	private void LoadExhibition()
	{
		(( TextView)findViewById(R.id.exhibition_Title)).setText(model.getEventName());
		(( TextView)findViewById(R.id.exhibition_Author)).setText(model.getAuthor());

		Glide.with(Exhibition.this).load(model.getImage_URL()).thumbnail(0.5f).centerCrop().into(( ImageView)findViewById(R.id.exhibition_Image));

		String date=new SimpleDateFormat("h:mm a, d MMM", Locale.getDefault()).format(model.getDateObject()).replace("AM", "Am").replace("PM","Pm");
		(( TextView)findViewById(R.id.exhibition_Date)).setText(date);

		(( TextView)findViewById(R.id.exhibition_Venue)).setText(model.getVenue());

		(( TextView)findViewById(R.id.exhibition_Description)).setText(model.getDescription());

		if(model.isNotify())
			((Button) findViewById(R.id.exhibition_notify)).setText("Wishlisted");
		else ((Button) findViewById(R.id.exhibition_notify)).setText("Add to Wishlist");

		actionBarBack.setLabel(model.getEventName());
	}

	@Override
	public void onBackPressed()
	{
		if(progressDialog!=null && progressDialog.isShowing())
			return;

		if(ActivityHelper.revertToHomeIfLast(Exhibition.this))
			return;
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode==AppUserModel.LOGIN_REQUEST_CODE)
		{
			if(resultCode==RESULT_OK)
			{
				AppUserModel.MAIN_USER.loadAppUser(Exhibition.this);
				findViewById(R.id.exhibition_notify).performClick();
			}
		}
		else
		super.onActivityResult(requestCode, resultCode, data);
	}
}
