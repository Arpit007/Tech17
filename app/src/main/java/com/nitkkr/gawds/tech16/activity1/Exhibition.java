package com.nitkkr.gawds.tech16.activity1;

import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nitkkr.gawds.tech16.database1.Database;
import com.nitkkr.gawds.tech16.helper1.ActionBarBack;
import com.nitkkr.gawds.tech16.helper1.ActivityHelper;
import com.nitkkr.gawds.tech16.model1.EventKey;
import com.nitkkr.gawds.tech16.model1.ExhibitionModel;
import com.nitkkr.gawds.tech16.R;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class Exhibition extends AppCompatActivity
{
	ExhibitionModel model;
	ActionBarBack actionBarBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exhibition);

		actionBarBack = new ActionBarBack(Exhibition.this);

		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(getIntent().getExtras().getInt("NotificationID"));

		EventKey key = (EventKey) getIntent().getExtras().getSerializable("Event");
		LoadExhibition(key);

		final Button fab = (Button) findViewById(R.id.exhibition_notify);

		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(model.isNotify())
				{
					fab.setText("Add to Wishlist");
					model.setNotify(false);
					Database.database.getExhibitionDB().addOrUpdateExhibition(model);
					Database.database.getNotificationDB().UpdateTable();
				}
				else
				{
					fab.setText("Wishlisted");
					model.setNotify(true);
					Database.database.getExhibitionDB().addOrUpdateExhibition(model);
					Database.database.getNotificationDB().UpdateTable();
				}
			}
		});
	}

	private void LoadExhibition(EventKey key)
	{
		model = Database.database.getExhibitionDB().getExhibition(key);

		(( TextView)findViewById(R.id.exhibition_Title)).setText(model.getEventName());
		(( TextView)findViewById(R.id.exhibition_Author)).setText(model.getAuthor());

		Glide.with(Exhibition.this).load(model.getImage_URL()).thumbnail(0.5f).centerCrop().into(( ImageView)findViewById(R.id.exhibition_Image));

		String date=new SimpleDateFormat("h:mm a, d MMM", Locale.getDefault()).format(model.getDateObject()).replace("AM", "am").replace("PM","pm");
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
		if(ActivityHelper.revertToHomeIfLast(Exhibition.this))
			return;
		super.onBackPressed();
	}
}
