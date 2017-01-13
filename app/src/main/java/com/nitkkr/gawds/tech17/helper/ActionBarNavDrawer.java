package com.nitkkr.gawds.tech17.helper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.About;
import com.nitkkr.gawds.tech17.activity.EventListPage;
import com.nitkkr.gawds.tech17.activity.Home;
import com.nitkkr.gawds.tech17.activity.Login;
import com.nitkkr.gawds.tech17.activity.SearchPage;
import com.nitkkr.gawds.tech17.activity.TeamPage;
import com.nitkkr.gawds.tech17.activity.ViewUser;
import com.nitkkr.gawds.tech17.api.EventTargetType;
import com.nitkkr.gawds.tech17.api.Query;
import com.nitkkr.gawds.tech17.model.AppUserModel;
import com.nitkkr.gawds.tech17.src.CircularTextView;
import com.nitkkr.gawds.tech17.src.CompatCircleImageView;

import static com.nitkkr.gawds.tech17.activity.Login.mGoogleApiClient;
import static com.nitkkr.gawds.tech17.helper.ActivityHelper.isDebugMode;
import static com.nitkkr.gawds.tech17.helper.ActivityHelper.startListActivity;

/**
 * Created by Home Laptop on 03-Nov-16.
 */

public class ActionBarNavDrawer
{
	private NavigationView navigationView = null;
	private iActionBar barNavDrawer;
	private AppCompatActivity activity;
	private boolean openNewSearchPage = false;
	private int ID = -1;

	private void NavigationItemSelected()
	{
		int id = ID;
		ID = -1;
		Intent intent;

		if (id == R.id.nav_home)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			if (activity instanceof Home)
			{
				DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
				drawer.closeDrawer(GravityCompat.START);
				return;
			}

			ActivityHelper.revertToHome(activity);
		}
		else if (id == R.id.nav_events)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			if (activity instanceof EventListPage)
			{
				DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
				drawer.closeDrawer(GravityCompat.START);
				return;
			}

			intent = new Intent(activity, EventListPage.class);
			activity.startActivity(intent);
			activity.finish();
		}
		else if (id == R.id.nav_gusto_talks)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			Query query = new Query(null, Query.QueryType.SQl, EventTargetType.GuestTalk);
			startListActivity(activity, activity.getString(R.string.Guest_Talks), query);
		}
		else if (id==R.id.nav_workshops)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			Query query = new Query(null, Query.QueryType.SQl, EventTargetType.Workshop);
			startListActivity(activity, activity.getString(R.string.Workshop), query);
		}
		else if (id == R.id.nav_informals)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			Query query = new Query(null, Query.QueryType.SQl, EventTargetType.Informals);
			startListActivity(activity, activity.getString(R.string.Informals), query);
		}
		else if (id == R.id.nav_exhibitions)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			Query query = new Query(null, Query.QueryType.SQl, EventTargetType.Exhibition);
			startListActivity(activity, activity.getString(R.string.Exhibition), query);
		}
		else if (id==R.id.nav_teams)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			if(AppUserModel.MAIN_USER.isUserLoggedIn(activity))
			{
				intent = new Intent(activity, TeamPage.class);
				activity.startActivity(intent);
			}
			else Snackbar.make(activity.findViewById(android.R.id.content), "Login Required", Snackbar.LENGTH_SHORT)
					.setAction("Login", new View.OnClickListener()
					{
						@Override
						public void onClick(View view)
						{
							AppUserModel.MAIN_USER.LoginUserNoHome(activity, false);
						}
					})
					.setActionTextColor(ContextCompat.getColor(activity, R.color.neon_green))
					.show();
		}
		else if(id== R.id.nav_invite)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			try
			{
				if(!ActivityHelper.isDebugMode(activity))
					Answers.getInstance().logCustom(new CustomEvent("Invite"));
				Intent waIntent = new Intent(Intent.ACTION_SEND);
				waIntent.setType("text/plain");
				String text = activity.getString(R.string.Invite_Message) + activity.getPackageName();

				waIntent.putExtra(Intent.EXTRA_TEXT, text);
				activity.startActivity(Intent.createChooser(waIntent, "Share with"));

			}
			catch (Exception e)
			{
				Toast.makeText(activity, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
			}
		}
		else if(id == R.id.nav_Dev)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			if(!isDebugMode(activity))
				Answers.getInstance().logCustom(new CustomEvent("Developers"));

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("Developers");
			builder.setCancelable(true);
			builder.setMessage("App developed by:\nArpit Bhatnagar\nAbhik Setia\nRishabh Kumar\n\nCooked by Gawds,\nTeam Techspardha\nNIT Kurukshetra");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialogInterface, int i)
				{
					dialogInterface.dismiss();
				}
			});
			AlertDialog alertDialog = builder.create();
			alertDialog.getWindow().getAttributes().windowAnimations = R.style.CloseDialogTheme;
			alertDialog.show();
		}
		else if (id == R.id.nav_About)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			intent = new Intent(activity, About.class);
			activity.startActivity(intent);
		}
		else if (id == R.id.nav_logout)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			logout();
		}
		else if (id == R.id.nav_login)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			intent = new Intent(activity, Login.class);
			intent.putExtra("Start_Home", false);
			activity.startActivity(intent);
		}
		else if(id==R.id.nav_Feedback)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			if(!ActivityHelper.isDebugMode(activity))
				Answers.getInstance().logCustom(new CustomEvent("Feedback"));

			String url = "http://techspardha.org/feedback";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			activity.startActivity(i);
		}
		else if (id == R.id.link)
		{
			if(activity instanceof Home)
				navigationView.getMenu().getItem(0).setChecked(true);
			else navigationView.getMenu().getItem(1).setChecked(true);

			String url = "http://techspardha.org/";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			activity.startActivity(i);
		}
	}

	public void logout()
	{
		mGoogleApiClient.connect();
		mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks()
		{
			@Override
			public void onConnected(Bundle bundle)
			{

				if (mGoogleApiClient.isConnected())
				{
					Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>()
					{
						@Override
						public void onResult(@NonNull Status status)
						{
							if (status.isSuccess())
							{

								Toast.makeText(activity, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
								AppUserModel.MAIN_USER.logoutUser(activity);

								Intent intent = new Intent(activity, Login.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
								activity.startActivity(intent);
								ActivityHelper.setExitAnimation(activity);
								activity.finish();
							}
							else
							{
								Toast.makeText(activity, "LogOut Failed", Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}

			@Override
			public void onConnectionSuspended(int i)
			{
				Log.d("DEBUG", "Google API Client Connection Suspended");
			}
		});
	}


	public ActionBarNavDrawer(final AppCompatActivity activity, iActionBar drawer, final int pageNavID)
	{
		this.activity = activity;
		barNavDrawer = drawer;

		navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
		{
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item)
			{
				ID = item.getItemId();
				DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
				drawer.closeDrawer(GravityCompat.START);
				return true;
			}
		});

		navigationView.setCheckedItem(pageNavID);

		DrawerLayout drawerx = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		drawerx.addDrawerListener(new DrawerLayout.DrawerListener()
		{
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset)
			{
			}

			@Override
			public void onDrawerOpened(View drawerView)
			{
				Refresh();
			}

			@Override
			public void onDrawerClosed(View drawerView)
			{
				if (ID != -1)
				{
					NavigationItemSelected();
				}
			}

			@Override
			public void onDrawerStateChanged(int newState)
			{
			}
		});

		activity.findViewById(R.id.actionbar_navButton).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Refresh();
				DrawerLayout drawerx = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
				navigationView.setCheckedItem(pageNavID);
				drawerx.openDrawer(GravityCompat.START);

				barNavDrawer.NavButtonClicked();
			}
		});

		final SearchView searchView = (SearchView) activity.findViewById(R.id.search);

		activity.findViewById(R.id.actionbar_search).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (openNewSearchPage)
				{
					Intent intent = new Intent(activity, SearchPage.class);
					intent.putExtra("Data_Type", "All");
					activity.startActivity(new Intent(activity, SearchPage.class));
					ActivityHelper.setCreateAnimation(activity);
				}
				else
				{
					activity.findViewById(R.id.main_bar).setVisibility(View.GONE);
					activity.findViewById(R.id.search_bar).setVisibility(View.VISIBLE);

					searchView.onActionViewExpanded();
				}
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
		{
			@Override
			public boolean onQueryTextSubmit(String query)
			{
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText)
			{
				barNavDrawer.SearchQuery(newText.trim());
				return true;
			}
		});

		activity.findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				activity.onBackPressed();
			}
		});

		setImage();

		navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (AppUserModel.MAIN_USER.isUserLoggedIn(activity))
				{
					Intent intent = new Intent(activity, ViewUser.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("User", AppUserModel.MAIN_USER);
					intent.putExtras(bundle);
					DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
					drawer.closeDrawer(GravityCompat.START);
					activity.startActivity(intent);
				}
				else
				{
					AppUserModel.MAIN_USER.LoginUserNoHome(activity, false);
				}
			}
		});
	}

	public boolean onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
			return true;
		}
		return !openNewSearchPage && !backPressed();
	}

	public void setLabel(String label)
	{
		( (TextView) activity.findViewById(R.id.actionbar_title) ).setText(label);
	}

	public void setOpenNewSearchPage(boolean openNewSearchPage)
	{
		this.openNewSearchPage = openNewSearchPage;
	}

	private boolean backPressed()
	{
		if (activity.findViewById(R.id.search_bar).getVisibility() == View.VISIBLE)
		{
			activity.findViewById(R.id.search_bar).setVisibility(View.GONE);
			activity.findViewById(R.id.main_bar).setVisibility(View.VISIBLE);
			SearchView searchView = (SearchView) activity.findViewById(R.id.search);
			searchView.setQuery("", false);
			return false;
		}
		else
		{
			return true;
		}
	}

	private void setImage()
	{
		if (AppUserModel.MAIN_USER.isUserLoggedIn(activity))
		{
			if (!AppUserModel.MAIN_USER.getImageResource().equals("") && AppUserModel.MAIN_USER.isUseGoogleImage())
			{
				CompatCircleImageView view = (CompatCircleImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image);
				view.setVisibility(View.VISIBLE);

				Glide.with(activity).load(AppUserModel.MAIN_USER.getImageResource()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f).centerCrop().into(view);

				navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image_Letter).setVisibility(View.INVISIBLE);
				navigationView.getHeaderView(0).findViewById(R.id.temp_user_Image_Letter).setVisibility(View.INVISIBLE);
			}
			else if (AppUserModel.MAIN_USER.getImageId() != -1)
			{
				CompatCircleImageView view = (CompatCircleImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image);
				view.setVisibility(View.VISIBLE);

				TypedArray array = activity.getResources().obtainTypedArray(R.array.Avatar);
				view.setImageResource(array.getResourceId(AppUserModel.MAIN_USER.getImageId(), 0));
				array.recycle();

				CircularTextView circularTextView = (CircularTextView) navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image_Letter);
				circularTextView.setVisibility(View.INVISIBLE);
				circularTextView = (CircularTextView) navigationView.getHeaderView(0).findViewById(R.id.temp_user_Image_Letter);
				circularTextView.setVisibility(View.VISIBLE);
				circularTextView.setFillColor(ContextCompat.getColor(activity, R.color.User_Image_Fill_Color));
			}
			else
			{
				CircularTextView view = (CircularTextView) navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image_Letter);

				if (AppUserModel.MAIN_USER.getName().isEmpty())
				{
					view.setText("#");
				}
				else
				{
					view.setText(String.valueOf(AppUserModel.MAIN_USER.getName().trim().toUpperCase().charAt(0)));
				}

				view.setVisibility(View.VISIBLE);

				TypedArray array = activity.getResources().obtainTypedArray(R.array.Flat_Colors);

				int colorPos;

				if (AppUserModel.MAIN_USER.getName().isEmpty())
				{
					colorPos = Math.abs(( '#' - 'a' )) % array.length();
				}
				else
				{
					colorPos = Math.abs(AppUserModel.MAIN_USER.getName().trim().toLowerCase().charAt(0) - 'a') % array.length();
				}

				view.setFillColor(array.getColor(colorPos, 0));
				view.setBorderWidth(2);
				view.setBorderColor(ContextCompat.getColor(activity, R.color.User_Image_Border_Color));

				array.recycle();

				navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image).setVisibility(View.INVISIBLE);
				navigationView.getHeaderView(0).findViewById(R.id.temp_user_Image_Letter).setVisibility(View.INVISIBLE);
			}

			TextView textView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_User_Name);
			textView.setVisibility(View.VISIBLE);
			textView.setText(AppUserModel.MAIN_USER.getName());
		}
		else
		{
			CompatCircleImageView view = (CompatCircleImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image);
			view.setVisibility(View.VISIBLE);

			TypedArray array = activity.getResources().obtainTypedArray(R.array.Avatar);
			view.setImageResource(array.getResourceId(0, R.drawable.avatar_1));
			array.recycle();

			CircularTextView circularTextView = (CircularTextView) navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image_Letter);
			circularTextView.setText("");
			circularTextView.setVisibility(View.VISIBLE);
			circularTextView.setFillColor(ContextCompat.getColor(activity, R.color.User_Image_Fill_Color));

			navigationView.getHeaderView(0).findViewById(R.id.nav_User_Name).setVisibility(View.GONE);
		}
	}

	private void Refresh()
	{
		setImage();

		if (AppUserModel.MAIN_USER.isUserLoggedIn(activity))
		{
			navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
			navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
		}
		else
		{
			navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
			navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
		}
	}
}
