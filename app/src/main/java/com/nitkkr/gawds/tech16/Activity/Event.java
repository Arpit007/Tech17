package com.nitkkr.gawds.tech16.Activity;

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

import com.nitkkr.gawds.tech16.Helper.ActionBarBack;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.SignInStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.Model.EventKey;
import com.nitkkr.gawds.tech16.Model.EventModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.List;

public class Event extends AppCompatActivity implements EventModel.EventStatusListener
{
	private final int REGISTER=100;
	private EventModel model;
	private ActionBarBack actionBar;
	private AlertDialog alertDialog;

	private void setRegister()
	{
		Button Register = (Button) findViewById(R.id.event_Register);
		if (model.isRegistered())
		{
			if (model.isSingleEvent())
			{
				Register.setText("Registered");
				Register.setEnabled(false);
			}
			if (model.isGroupEvent() || model.isVariableGroupEvent())
			{
				Register.setText("View Team");
				Register.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						//TODO: Pass Team Model
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
			Register.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if(AppUserModel.MAIN_USER.isUserLoaded())
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
									SignInStatus status = SignInStatus.NONE;
									//TODO: Register Single Event
									switch (status)
									{
										case FAILED:
											Toast.makeText(Event.this, "Failed, Please Try Again", Toast.LENGTH_LONG).show();
											break;
										case SUCCESS:
											Toast.makeText(Event.this, "Registered Successfully", Toast.LENGTH_LONG).show();
											LoadEvent();
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
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		actionBar = new ActionBarBack(this);

		TabLayout tabLayout=(TabLayout)findViewById(R.id.event_tab_layout);
		ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager);
		setupViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);
				LoadEvent();

	}

	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new About_Fragment(), "About");
		adapter.addFragment(new Rules_frag(), "Rules");
		adapter.addFragment(new Contact_frag(), "Contact");
		adapter.addFragment(new Result_frag(), "Result");
		viewPager.setAdapter(adapter);
	}
	class ViewPagerAdapter extends FragmentPagerAdapter {
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
			if(ActivityHelper.revertToHomeIfLast(Event.this))
				return;
			super.onBackPressed();
		}
	}

	private void LoadEvent()
	{
		EventKey key = (EventKey) getIntent().getExtras().getSerializable("Event");

		//Load Event
		model=new EventModel();
		model.setEventID(key.getEventID());
		model.setEventName(key.getEventName());
		model.setNotify(model.isNotify());

		try
		{
			model.setStatusListener(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//TODO: Implement------------ Load event from net
		StringBuilder text = new StringBuilder("");
		text.append("\nID: ").append(model.getEventID());
		text.append("\nName: ").append(model.getEventName());
		text.append("\nVenue: ").append(model.getVenue());
		text.append("\nDescription: ").append(model.getDescription());
		text.append("\nRules: ").append(model.getRules());
		text.append("\nStatus: ").append(model.getEventStatus());
		text.append("\nNotify: ").append(model.isNotify());

		//----------------Date, Result, Cood, Participants------------------------

		actionBar.setLabel(model.getEventName());

		if (model.isGroupEvent())
		{
			text.append("\nMember Count: ").append(model.getMinUsers());
		}
		if (model.isVariableGroupEvent())
		{
			text.append("\nTeam Size:").append(model.getMinUsers()).append("-").append(model.getMaxUsers());
		}

		//( (TextView) findViewById(R.id.event_detail) ).setText(text.toString());

		setRegister();
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
					LoadEvent();
				}
			}
		}
		else if(requestCode==AppUserModel.LOGIN_REQUEST_CODE)
			{
				if(requestCode==RESULT_OK)
				{
					AppUserModel.MAIN_USER.loadAppUser(Event.this);
					findViewById(R.id.event_Register).performClick();
				}
			}
		else
			super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void EventStatusChanged(EventModel.EventStatus status)
	{
		//TODO: Event Status
	}
}
