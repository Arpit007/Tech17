package com.nitkkr.gawds.tech16.Activity;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.Activity.Fragment.AboutEvent;
import com.nitkkr.gawds.tech16.Activity.Fragment.ContactEvent;
import com.nitkkr.gawds.tech16.Activity.Fragment.Result_frag;
import com.nitkkr.gawds.tech16.Activity.Fragment.RulesEvent;
import com.nitkkr.gawds.tech16.Database.Database;
import com.nitkkr.gawds.tech16.Helper.ActionBarBack;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.ResponseStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.Model.EventKey;
import com.nitkkr.gawds.tech16.Model.EventModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.Src.PdfHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Event extends AppCompatActivity implements EventModel.EventStatusListener
{
	private final int REGISTER=100;
	private EventModel model;
	private ActionBarBack actionBar;
	private AlertDialog alertDialog;

	private void setCallbacks()
	{
		Button Register = (Button) findViewById(R.id.Event_Register);
		if (model.isRegistered())
		{
			if (model.isSingleEvent())
			{
				Register.setText("Registered");
				Register.setEnabled(false);
			}
			if (model.isGroupEvent())
			{
				Register.setText("View Team");
				Register.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						Intent intent=new Intent(Event.this,ViewTeam.class);
						Bundle bundle=new Bundle();
						bundle.putSerializable("Event",model);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
			}
		}
		else
		{
			Register.setText("Register");
			Register.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if(AppUserModel.MAIN_USER.isUserLoggedIn(Event.this))
					{
						if (model.isSingleEvent())
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(Event.this);
							builder.setCancelable(true);
							builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialogInterface, int i)
								{
									ResponseStatus status = ResponseStatus.NONE;
									//TODO: Register Single Event
									switch (status)
									{
										case FAILED:
											Toast.makeText(Event.this, "Failed, Please Try Again", Toast.LENGTH_LONG).show();
											break;
										case SUCCESS:
											Toast.makeText(Event.this, "Registered Successfully", Toast.LENGTH_LONG).show();

											model.setRegistered(true);
											model.setNotify(true);
											Database.database.getEventsDB().addOrUpdateEvent(model);
											Database.database.getNotificationDB().UpdateTable();
											model.callStatusListener();

											break;
										case OTHER:
											Toast.makeText(Event.this, "----------------------Message--------------------", Toast.LENGTH_LONG).show();
											break;
										default:
											break;
									}
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialogInterface, int i)
								{
									dialogInterface.dismiss();
								}
							});
							builder.setMessage("Are you sure, you want to Register for " + model.getEventName() + "?");
							builder.setTitle("Register Event");
							alertDialog = builder.create();
							alertDialog.show();
						}
						else
						{
							Intent intent = new Intent(Event.this, CreateTeam.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable("Event",model);
							intent.putExtras(bundle);
							startActivityForResult(new Intent(Event.this, CreateTeam.class), REGISTER);
						}
					}
					else
					{
						AppUserModel.MAIN_USER.LoginUserNoHome(Event.this,true);
					}
				}
			});
		}

		final Button PdfButton=(Button)findViewById(R.id.Event_Pdf);
		PdfButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(PdfHelper.getInstance().isPdfExisting(model.getPdfLink()))
				{
					PdfHelper.getInstance().viewPdfIfExists(model.getPdfLink(),Event.this);
				}
				else
				{
					PdfButton.setText("Downloading");
					PdfButton.setEnabled(false);

					PdfHelper.getInstance().DownloadPdf(model.getPdfLink(), new PdfHelper.iCallback()
					{
						@Override
						public void DownloadComplete(String url, ResponseStatus status)
						{
							if (status==ResponseStatus.SUCCESS)
								PdfButton.setText("View Pdf");
							else PdfButton.setText("Download as PDF");
							PdfButton.setEnabled(true);
						}
					},Event.this);
				}
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		actionBar = new ActionBarBack(this);

		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(getIntent().getExtras().getInt("NotificationID"));

		TabLayout tabLayout=(TabLayout)findViewById(R.id.event_tab_layout);
		ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager);

		EventKey key = (EventKey) getIntent().getExtras().getSerializable("Event");
		model= Database.database.getEventsDB().getEvent(key);

		setupViewPager(viewPager,model);
		tabLayout.setupWithViewPager(viewPager);
		LoadEvent();

		int PageID=getIntent().getExtras().getInt("PageID",0);
		if(PageID<tabLayout.getTabCount())
			viewPager.setCurrentItem(PageID);
	}

	private void setupViewPager(ViewPager viewPager, EventModel model) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(AboutEvent.getNewFragment(model), "About");
		adapter.addFragment(RulesEvent.getNewFragment(model), "Rules");
		adapter.addFragment(ContactEvent.getNewFragment(model), "Contact");
		adapter.addFragment(Result_frag.getNewFragment(model), "Result");
		viewPager.setAdapter(adapter);
	}

	class ViewPagerAdapter extends FragmentPagerAdapter
	{
		private final List<Fragment> mFragmentList = new ArrayList<>();
		private final List<String> mFragmentTitleList = new ArrayList<>();

		public ViewPagerAdapter(FragmentManager manager) {
			super(manager);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}

		public void addFragment(Fragment fragment, String title) {
			mFragmentList.add(fragment);
			mFragmentTitleList.add(title);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mFragmentTitleList.get(position);
		}
	}

	@Override
	public void onBackPressed()
	{
		if (alertDialog != null && alertDialog.isShowing())
		{
			alertDialog.dismiss();
		}
		else
		{
			PdfHelper.getInstance().removeListener(model.getPdfLink());
			if(ActivityHelper.revertToHomeIfLast(Event.this))
				return;
			super.onBackPressed();
		}
	}

	private void LoadEvent()
	{
		model.setListener(Event.this);

		(( TextView)findViewById(R.id.Event_Name)).setText(model.getEventName());
		(( TextView)findViewById(R.id.Event_Category)).setText(Database.database.getInterestDB().getInterest(model.getSociety()));

		String date=new SimpleDateFormat("h:mm a, d MMM", Locale.getDefault()).format(model.getDateObject()).replace("AM", "am").replace("PM","pm");
		(( TextView)findViewById(R.id.Event_Date)).setText(date);

		(( TextView)findViewById(R.id.Event_Venue)).setText(model.getVenue());

		if(model.isSingleEvent())
			(( TextView)findViewById(R.id.Event_Members)).setText("Individual");
		else
		{
			String Text="Team (" +model.getMinUsers() + "-" + model.getMaxUsers() + " Members)";
			(( TextView)findViewById(R.id.Event_Members)).setText(Text);
		}

		Button PdfButton=(Button)findViewById(R.id.Event_Pdf);

		if(PdfHelper.getInstance().isPdfExisting(model.getPdfLink()))
		{
			PdfButton.setText("View Pdf");
			PdfButton.setEnabled(true);
		}
		else if (PdfHelper.getInstance().isPdfDownloading(model.getPdfLink()))
		{
			PdfButton.setText("Downloading");
			PdfButton.setEnabled(false);
		}
		else
		{
			PdfButton.setText("Download as PDF");
			PdfButton.setEnabled(true);
		}


		actionBar.setLabel(model.getEventName());

		setCallbacks();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode==REGISTER)
		{
			if(resultCode==RESULT_OK)
			{
				if(data.getBooleanExtra("Register",false))
				{
					Toast.makeText(Event.this,"Registered Successfully",Toast.LENGTH_LONG).show();
					EventStatusChanged(EventModel.EventStatus.None);
				}
			}
		}
		else if(requestCode==AppUserModel.LOGIN_REQUEST_CODE)
			{
				if(requestCode==RESULT_OK)
				{
					AppUserModel.MAIN_USER.loadAppUser(Event.this);
					findViewById(R.id.Event_Register).performClick();
				}
			}
		else
			super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void EventStatusChanged(EventModel.EventStatus status)
	{
		LoadEvent();
		//TODO: Event Status, Round Num
	}
}
