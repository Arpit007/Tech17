package com.nitkkr.gawds.tech17.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.fragment.AboutEvent;
import com.nitkkr.gawds.tech17.activity.fragment.ContactEvent;
import com.nitkkr.gawds.tech17.activity.fragment.Result_frag;
import com.nitkkr.gawds.tech17.activity.fragment.RulesEvent;
import com.nitkkr.gawds.tech17.adapter.ViewPagerAdapter;
import com.nitkkr.gawds.tech17.api.FetchData;
import com.nitkkr.gawds.tech17.api.iResponseCallback;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.helper.ActionBarBack;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.model.AppUserModel;
import com.nitkkr.gawds.tech17.model.EventKey;
import com.nitkkr.gawds.tech17.model.EventModel;
import com.nitkkr.gawds.tech17.model.EventStatus;
import com.nitkkr.gawds.tech17.src.CircularTextView;
import com.nitkkr.gawds.tech17.src.CustomTabLayout;
import com.nitkkr.gawds.tech17.src.PdfDownloader;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Event extends AppCompatActivity implements EventModel.EventStatusListener
{
	private final int REGISTER = 100;
	private EventKey key;
	private EventModel model;
	private ActionBarBack actionBar;
	private AlertDialog alertDialog;
	private ProgressDialog progressDialog = null;
	private SwipeRefreshLayout swipe;
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
			Register.setEnabled(true);
			Register.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if (AppUserModel.MAIN_USER.isUserLoggedIn(Event.this))
					{

						if (model.getEventStatus() == EventStatus.Started || model.getEventStatus() == EventStatus.Finished)
						{
							Snackbar.make(Event.this.findViewById(android.R.id.content), "Registrations Are Closed", Snackbar.LENGTH_LONG).show();
						}
						else if (model.getEventStatus() == EventStatus.None)
						{
							Snackbar.make(Event.this.findViewById(android.R.id.content), "Fetching Live Data/Server Error", Snackbar.LENGTH_LONG).show();
						}
						else if (model.getEventStatus() == EventStatus.NotStarted)
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
										progressDialog = new ProgressDialog(Event.this,R.style.StyledDialog);
										progressDialog.setMessage("Registering, Please Wait");
										progressDialog.setIndeterminate(true);
										progressDialog.setCancelable(false);
										progressDialog.show();

										FetchData.getInstance().registerSingleEvent(getApplicationContext(), String.valueOf(model.getEventID()), new iResponseCallback()
										{
											@Override
											public void onResponse(ResponseStatus status)
											{
												if (progressDialog != null)
												{
													progressDialog.dismiss();
												}

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

												alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(Event.this, R.color.button_color));
												alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(Event.this, R.color.button_color));
												alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(Event.this, R.color.button_color));
											}
										});
								alertDialog.show();
							}
							else
							{
								/*TODO:Remove on Update*/
								ActivityHelper.comingSoonSnackBar("Team Registration Coming Soon", Event.this);

								/*Intent intent = new Intent(Event.this, CreateTeam.class);
								Bundle bundle=new Bundle();
								bundle.putSerializable("Event",key);
								intent.putExtras(bundle);
								startActivityForResult(intent, REGISTER);*/
							}
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
										AppUserModel.MAIN_USER.LoginUserNoHome(Event.this, false);
									}
								})
								.setActionTextColor(ContextCompat.getColor(Event.this, R.color.neon_green))
								.show();
					}
				}
			});
		}

		final Button PdfButton = (Button) findViewById(R.id.Event_Pdf);
		if(model.getPdfLink().equals(""))
			PdfButton.setVisibility(View.GONE);
		else
		{
			PdfButton.setVisibility(View.VISIBLE);
			PdfButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if (PdfDownloader.getInstance().isPdfExisting(model.getEventName()))
					{
						PdfDownloader.getInstance().viewPdfIfExists(model.getEventName(), Event.this);
					}
					else
					{
						if (!ActivityHelper.isInternetConnected())
						{
							PdfButton.setText("Download PDF");
							Toast.makeText(Event.this, "No Network Connection", Toast.LENGTH_SHORT).show();
							return;
						}
						PdfButton.setText("Downloading");
						PdfButton.setEnabled(false);

						PdfDownloader.getInstance().DownloadPdf(model.getPdfLink(), model.getEventName(), new PdfDownloader.iCallback()
						{
							@Override
							public void DownloadComplete(String url, ResponseStatus status)
							{
								if (status == ResponseStatus.SUCCESS)
								{
									PdfButton.setText("View Pdf");
								}
								else
								{
									PdfButton.setText("Download PDF");
								}
								PdfButton.setEnabled(true);
							}
						}, Event.this);
					}
				}
			});
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		ActivityHelper.setCreateAnimation(this);
		ActivityHelper.setStatusBarColor(this);

		actionBar = new ActionBarBack(this);

		( (NotificationManager) getSystemService(NOTIFICATION_SERVICE) ).cancel(getIntent().getExtras().getInt("NotificationID"));

		CustomTabLayout tabLayout = (CustomTabLayout) findViewById(R.id.event_tab_layout);
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

		key = (EventKey) getIntent().getExtras().getSerializable("Event");
		model = Database.getInstance().getEventsDB().getEvent(key);

		swipe=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
		swipe.setDistanceToTriggerSync(20);
		swipe.setSize(SwipeRefreshLayout.DEFAULT);
		swipe.setColorSchemeColors(ContextCompat.getColor(this,R.color.RingColor));
		swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				swipe.setRefreshing(true);
				swipe.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
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
								else
								{
									Toast.makeText(Event.this,"Live Status Unavailable\nPull down to Refresh",Toast.LENGTH_SHORT).show();
									TextView Status=(TextView) findViewById(R.id.Event_Status);
									Status.setText("Go Online");
								}
								swipe.setRefreshing(false);
							}
						});
					}
				},1500);
			}
		});

		setupViewPager(viewPager, model);
		tabLayout.setupWithViewPager(viewPager);
		LoadEvent();

		int PageID = getIntent().getExtras().getInt("PageID", 0);
		if (PageID < tabLayout.getTabCount())
		{
			viewPager.setCurrentItem(PageID);
		}

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
				else
				{
					Toast.makeText(Event.this,"Live Status Unavailable\nPull down to Refresh",Toast.LENGTH_SHORT).show();
					TextView Status=(TextView) findViewById(R.id.Event_Status);
					Status.setText("Go Online");
				}
			}
		});

	}

	private void setupViewPager(ViewPager viewPager, EventModel model)
	{
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(AboutEvent.getNewFragment(model), "About");
		adapter.addFragment(RulesEvent.getNewFragment(model), "Rules");
		adapter.addFragment(ContactEvent.getNewFragment(model), "Contact");
		adapter.addFragment(Result_frag.getNewFragment(model), "Result");
		viewPager.setAdapter(adapter);
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
			if (ActivityHelper.revertToHomeIfLast(Event.this))
			{
				;
			}
			else
			{
				super.onBackPressed();
			}
			ActivityHelper.setExitAnimation(this);
		}
	}

	private void LoadEvent()
	{
		model.setListener(Event.this);

		( (TextView) findViewById(R.id.Event_Name) ).setText(model.getEventName());
		( (TextView) findViewById(R.id.Event_Category) ).setText(Database.getInstance().getInterestDB().getInterest(model.getCategory()));

		String date = new SimpleDateFormat("h:mm a, d MMM", Locale.getDefault()).format(model.getDateObject()).replace("AM", "Am").replace("PM", "Pm");
		( (TextView) findViewById(R.id.Event_Date) ).setText(date);

		( (TextView) findViewById(R.id.Event_Venue) ).setText(model.getVenue());

		if (model.isSingleEvent())
		{
			( (TextView) findViewById(R.id.Event_Members) ).setText("Individual");
		}
		else
		{
			String Text = "Team (" + model.getMinUsers() + "-" + model.getMaxUsers() + " Members)";
			( (TextView) findViewById(R.id.Event_Members) ).setText(Text);
		}

		Button PdfButton = (Button) findViewById(R.id.Event_Pdf);

		if (PdfDownloader.getInstance().isPdfExisting(model.getEventName()))
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
			PdfButton.setText("Download PDF");
			PdfButton.setEnabled(true);
		}

		CircularTextView Round=(CircularTextView) findViewById(R.id.Event_Round);
		Round.setBorderWidth(5);
		Round.setTextColor(ContextCompat.getColor(Event.this,R.color.text_color_primary));
		TextView Status=(TextView) findViewById(R.id.Event_Status);

		if (model.getEventStatus() == EventStatus.None)
		{
			Round.setText("0");
			Round.setBackgroundResource(R.drawable.none);
			Status.setText("Updating");
			Status.setTextColor(ContextCompat.getColor(Event.this,R.color.None));
		}
		else
		{
			Status.setText(model.getEventStatus().getValue());
			if(model.getEventStatus()==EventStatus.NotStarted)
			{
				Status.setTextColor(ContextCompat.getColor(Event.this,R.color.Upcoming));
				Round.setBackgroundResource(R.drawable.notstart);
			}
			else if(model.getEventStatus()==EventStatus.Started)
			{
				Status.setTextColor(ContextCompat.getColor(Event.this,R.color.Started));
				Round.setBackgroundResource(R.drawable.start);
			}
			else if(model.getEventStatus()==EventStatus.Finished)
			{
				Status.setTextColor(ContextCompat.getColor(Event.this,R.color.Over));
				Round.setBackgroundResource(R.drawable.finished);
			}
		}
		Round.setText(String.valueOf(model.getCurrentRound()));
		actionBar.setLabel(model.getEventName());

		setCallbacks();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REGISTER)
		{
			if (resultCode == RESULT_OK)
			{
				if (data.getBooleanExtra("Register", false))
				{
					Toast.makeText(Event.this, "Registered Successfully", Toast.LENGTH_LONG).show();
					EventStatusChanged(EventStatus.None);
					LoadEvent();
				}
			}
		}
		else if (requestCode == AppUserModel.LOGIN_REQUEST_CODE)
		{
			if (resultCode == RESULT_OK)
			{
				AppUserModel.MAIN_USER.loadAppUser(Event.this);
				findViewById(R.id.Event_Register).performClick();
			}
		}
		else
		{
			super.onActivityResult(requestCode, resultCode, data);
		}

	}

	@Override
	public void EventStatusChanged(EventStatus status)
	{
		LoadEvent();
	}
}
