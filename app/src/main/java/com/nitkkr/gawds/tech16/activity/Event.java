package com.nitkkr.gawds.tech16.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.activity.fragment.AboutEvent;
import com.nitkkr.gawds.tech16.activity.fragment.ContactEvent;
import com.nitkkr.gawds.tech16.activity.fragment.Result_frag;
import com.nitkkr.gawds.tech16.activity.fragment.RulesEvent;
import com.nitkkr.gawds.tech16.api.FetchData;
import com.nitkkr.gawds.tech16.api.iResponseCallback;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.helper.ActionBarBack;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.ResponseStatus;
import com.nitkkr.gawds.tech16.model.AppUserModel;
import com.nitkkr.gawds.tech16.model.EventKey;
import com.nitkkr.gawds.tech16.model.EventModel;
import com.nitkkr.gawds.tech16.model.EventStatus;
import com.nitkkr.gawds.tech16.src.CustomTabLayout;
import com.nitkkr.gawds.tech16.src.PdfDownloader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Event extends AppCompatActivity implements EventModel.EventStatusListener
{
	private final int REGISTER=100;
	private EventKey key;
	private EventModel model;
	private ActionBarBack actionBar;
	private AlertDialog alertDialog;
	private ProgressDialog progressDialog=null;

	private void setCallbacks()
	{
		Button Register = (Button) findViewById(R.id.Event_Register);
		if (model.isRegistered())
		{
			Register.setText("Registered");
			Register.setEnabled(false);

			//========TODO:Remove on Update=====================
			/*
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
						bundle.putSerializable("Event",key);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
			}*/
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
									dialogInterface.dismiss();
									progressDialog=new ProgressDialog(Event.this);
									progressDialog.setMessage("Registering, Please Wait");
									progressDialog.setIndeterminate(true);
									progressDialog.setCancelable(false);
									progressDialog.show();

									FetchData.getInstance().registerSingleEvent(getApplicationContext(), String.valueOf(model.getEventID()), new iResponseCallback()
									{
										@Override
										public void onResponse(ResponseStatus status)
										{
											if(progressDialog!=null)
												progressDialog.dismiss();

											switch (status)
											{
												case FAILED:
													Toast.makeText(Event.this, "Failed, Please Try Again", Toast.LENGTH_LONG).show();
													break;
												case SUCCESS:
													Toast.makeText(Event.this, "Registered Successfully", Toast.LENGTH_LONG).show();

													model.setRegistered(true);
													model.setNotify(true);
													Database.getInstance().getEventsDB().addOrUpdateEvent(model);
													Database.getInstance().getNotificationDB().UpdateTable();
													model.callStatusListener();
													LoadEvent();
													break;
												default:
													Toast.makeText(Event.this, "Network Error", Toast.LENGTH_LONG).show();
													break;
											}
										}

										@Override
										public void onResponse(ResponseStatus status, Object object)
										{
											onResponse(status);
										}
									});
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
							alertDialog.setOnShowListener(
									new DialogInterface.OnShowListener()
									{
										@Override
										public void onShow(DialogInterface arg0)
										{

											alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(Event.this,R.color.button_color));
											alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(Event.this,R.color.button_color));
											alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(Event.this,R.color.button_color));
										}
									});
							alertDialog.show();
						}
						else
						{
							/*TODO:Remove on Update*/
							ActivityHelper.comingSoonSnackBar("Team Registration Coming Soon",Event.this);

							/*Intent intent = new Intent(Event.this, CreateTeam.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable("Event",key);
							intent.putExtras(bundle);
							startActivityForResult(intent, REGISTER);*/
						}
					}
					else
					{
						Snackbar.make(findViewById(android.R.id.content), "Login Required", Snackbar.LENGTH_SHORT)
								.setAction("Login", new View.OnClickListener()
								{
									@Override
									public void onClick(View view)
									{
										AppUserModel.MAIN_USER.LoginUserNoHome(Event.this,false);
									}
								})
								.setActionTextColor(ContextCompat.getColor(Event.this,R.color.neon_green))
								.show();
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
				if(PdfDownloader.getInstance().isPdfExisting(model.getPdfLink()))
				{
					PdfDownloader.getInstance().viewPdfIfExists(model.getPdfLink(),Event.this);
				}
				else
				{
					PdfButton.setText("Downloading");
					PdfButton.setEnabled(false);

					PdfDownloader.getInstance().DownloadPdf(model.getPdfLink(), new PdfDownloader.iCallback()
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
		ActivityHelper.setCreateAnimation(this);
		ActivityHelper.setStatusBarColor(this);

		actionBar = new ActionBarBack(this);

		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(getIntent().getExtras().getInt("NotificationID"));

		CustomTabLayout tabLayout=(CustomTabLayout) findViewById(R.id.event_tab_layout);
		ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager);

		key = (EventKey) getIntent().getExtras().getSerializable("Event");
		model= Database.getInstance().getEventsDB().getEvent(key);

		setupViewPager(viewPager,model);
		tabLayout.setupWithViewPager(viewPager);
		LoadEvent();

		int PageID=getIntent().getExtras().getInt("PageID",0);
		if(PageID<tabLayout.getTabCount())
			viewPager.setCurrentItem(PageID);

		FetchData.getInstance().getEvent(getApplicationContext(), model.getEventID(), new iResponseCallback()
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
					model = (EventModel) object;
					Database.getInstance().getEventsDB().addOrUpdateEvent(model);
					LoadEvent();
				}
			}
		});

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
			PdfDownloader.getInstance().removeListener(model.getPdfLink());
			if(ActivityHelper.revertToHomeIfLast(Event.this));
			else super.onBackPressed();
			ActivityHelper.setExitAnimation(this);
		}
	}

	private void LoadEvent()
	{
		model.setListener(Event.this);

		(( TextView)findViewById(R.id.Event_Name)).setText(model.getEventName());
		(( TextView)findViewById(R.id.Event_Category)).setText(Database.getInstance().getInterestDB().getInterest(model.getCategory()));

		String date=new SimpleDateFormat("h:mm a, d MMM", Locale.getDefault()).format(model.getDateObject()).replace("AM", "Am").replace("PM","Pm");
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

		if(PdfDownloader.getInstance().isPdfExisting(model.getPdfLink()))
		{
			PdfButton.setText("View Pdf");
			PdfButton.setEnabled(true);
		}
		else if (PdfDownloader.getInstance().isPdfDownloading(model.getPdfLink()))
		{
			PdfButton.setText("Downloading");
			PdfButton.setEnabled(false);
		}
		else
		{
			PdfButton.setText("Download as PDF");
			PdfButton.setEnabled(true);
		}

		if(model.getEventStatus()==EventStatus.None)
			(( TextView)findViewById(R.id.Event_Round)).setText("ROUND");
		else (( TextView)findViewById(R.id.Event_Round)).setText(model.getEventStatus().name());
		(( TextView)findViewById(R.id.Event_Round)).setText(String.valueOf(model.getCurrentRound()));
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
					EventStatusChanged(EventStatus.None);
					LoadEvent();
				}
			}
		}
		else if(requestCode==AppUserModel.LOGIN_REQUEST_CODE)
			{
				if(resultCode==RESULT_OK)
				{
					AppUserModel.MAIN_USER.loadAppUser(Event.this);
					findViewById(R.id.Event_Register).performClick();
				}
			}
		else
			super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void EventStatusChanged(EventStatus status)
	{
		LoadEvent();
	}
}
