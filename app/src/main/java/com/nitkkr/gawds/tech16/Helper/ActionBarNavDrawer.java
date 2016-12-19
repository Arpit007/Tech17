package com.nitkkr.gawds.tech16.Helper;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nitkkr.gawds.tech16.API.Query;
import com.nitkkr.gawds.tech16.Activity.About;
import com.nitkkr.gawds.tech16.Activity.EventListPage;
import com.nitkkr.gawds.tech16.Activity.Home;
import com.nitkkr.gawds.tech16.Activity.Login;
import com.nitkkr.gawds.tech16.Activity.SearchPage;
import com.nitkkr.gawds.tech16.Activity.ViewUser;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.Src.CircularTextView;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nitkkr.gawds.tech16.Helper.ActivityHelper.startListActivity;

/**
 * Created by Home Laptop on 03-Nov-16.
 */

public class ActionBarNavDrawer
{

	private iActionBar barNavDrawer;
	private AppCompatActivity activity;
	private int pageNavID;
	private boolean openNewSearchPage=false;

	private boolean NavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		Intent intent;

		if (id == R.id.nav_home)
		{
			if(activity instanceof Home)
			{
				DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
				drawer.closeDrawer(GravityCompat.START);
				return true;
			}

			ActivityHelper.revertToHome(activity);
		}
		else if (id == R.id.nav_events)
		{
			if(activity instanceof EventListPage)
			{
				DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
				drawer.closeDrawer(GravityCompat.START);
				return true;
			}

			intent=new Intent(activity,EventListPage.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activity.startActivity(intent);
			activity.finish();
		}
		else if (id == R.id.nav_gusto_talks)
		{
			//TODO:Implement
			Query query=new Query("============Implement===========", Query.QueryType.SQl, Query.QueryTargetType.GuestTalk);
			startListActivity(activity,activity.getString(R.string.Guest_Talks),query);
		}
		else if (id == R.id.nav_informals)
		{
			Query query=new Query("============Implement===========", Query.QueryType.SQl, Query.QueryTargetType.Informals);
			startListActivity(activity,activity.getString(R.string.Informals),query);
		}
		else if (id == R.id.nav_exhibitions)
		{
			Query query=new Query("============Implement===========", Query.QueryType.SQl, Query.QueryTargetType.Exhibition);
			startListActivity(activity,activity.getString(R.string.Exhibition),query);
		}
		else if (id == R.id.nav_About)
		{
			intent=new Intent(activity, About.class);
			activity.startActivity(intent);
		}
		else if (id == R.id.nav_logout)
		{
			AppUserModel.MAIN_USER.logoutUser(activity);
			AppUserModel.MAIN_USER.setLoggedIn(false,activity.getBaseContext());
			Intent intent1=new Intent(activity,Login.class);
			intent1.putExtra("Start_Home",false);
			activity.startActivity(intent1);
			activity.finish();

		}
		else if(id==R.id.nav_login)
		{
			Intent intent1=new Intent(activity,Login.class);
			intent1.putExtra("Start_Home",false);
			activity.startActivity(intent1);
		}

		DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);

		return true;
	}

	public ActionBarNavDrawer(final AppCompatActivity activity, iActionBar drawer, int pageNavID)
	{
		this.activity = activity;
		barNavDrawer=drawer;
		this.pageNavID=pageNavID;

		NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
		{
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item)
			{
				return NavigationItemSelected(item);
			}
		});

		navigationView.setCheckedItem(pageNavID);

		activity.findViewById(R.id.actionbar_navButton).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				DrawerLayout drawerx = (DrawerLayout) activity.findViewById(R.id.drawer_layout);

				NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
				navigationView.setCheckedItem(ActionBarNavDrawer.this.pageNavID);

				if(AppUserModel.MAIN_USER.isUserLoaded())
				{
					navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
					navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
				}
				else
				{
					navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
					navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
				}
				drawerx.openDrawer(GravityCompat.START);

				barNavDrawer.NavButtonClicked();
			}
		});
		activity.findViewById(R.id.actionbar_search).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (openNewSearchPage)
					activity.startActivity(new Intent(activity, SearchPage.class));
				else
				{
					activity.findViewById(R.id.main_bar).setVisibility(View.GONE);
					activity.findViewById(R.id.search_bar).setVisibility(View.VISIBLE);

					SearchView searchView=(SearchView)activity.findViewById(R.id.search);
					searchView.onActionViewExpanded();
				}
			}
		});

		SearchView searchView=(SearchView)activity.findViewById(R.id.search);
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
				barNavDrawer.SearchQuery(newText);
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

		if(AppUserModel.MAIN_USER.isUserLoaded())
		{
			if(AppUserModel.MAIN_USER.getImageResource()!=null && AppUserModel.MAIN_USER.isUseGoogleImage())
			{
				CircleImageView view=(CircleImageView)navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image);
				view.setVisibility(View.VISIBLE);

                Glide.with(activity).load(AppUserModel.MAIN_USER.getImageResource()).thumbnail(0.5f).centerCrop().into(view);

				navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image_Letter).setVisibility(View.INVISIBLE);
			}
			else if(AppUserModel.MAIN_USER.getImageId()!=-1)
			{
				CircleImageView view=(CircleImageView)navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image);
				view.setVisibility(View.VISIBLE);

				TypedArray array=activity.getResources().obtainTypedArray(R.array.Avatar);
				view.setImageResource(array.getResourceId(AppUserModel.MAIN_USER.getImageId(),0));
				array.recycle();

				CircularTextView circularTextView=(CircularTextView)navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image_Letter);
				circularTextView.setVisibility(View.VISIBLE);
				circularTextView.setText("");
				circularTextView.setFillColor(ContextCompat.getColor(activity,R.color.User_Image_Fill_Color));
			}
			else
			{
				CircularTextView view=(CircularTextView)navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image_Letter);

				view.setText(AppUserModel.MAIN_USER.getName().toUpperCase().charAt(0));
				view.setVisibility(View.VISIBLE);

				TypedArray array=activity.getResources().obtainTypedArray(R.array.Flat_Colors);

				int colorPos=(AppUserModel.MAIN_USER.getName().toLowerCase().charAt(0)-'a')%array.length();

				view.setFillColor(array.getColor(colorPos,0));
				view.setBorderColor(ContextCompat.getColor(activity,R.color.User_Image_Border_Color));

				array.recycle();

				navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image).setVisibility(View.GONE);
			}

			TextView textView=(TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_User_Name);
			textView.setVisibility(View.VISIBLE);
			textView.setText(AppUserModel.MAIN_USER.getName());
		}
		else
		{
			CircleImageView view=(CircleImageView)navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image);
			view.setVisibility(View.VISIBLE);

			TypedArray array=activity.getResources().obtainTypedArray(R.array.Avatar);
			view.setImageResource(array.getResourceId(0,0));
			array.recycle();

			CircularTextView circularTextView=(CircularTextView)navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image_Letter);
			circularTextView.setText("");
			circularTextView.setVisibility(View.VISIBLE);
			circularTextView.setFillColor(ContextCompat.getColor(activity,R.color.User_Image_Fill_Color));

			navigationView.getHeaderView(0).findViewById(R.id.nav_User_Name).setVisibility(View.GONE);
		}

		navigationView.getHeaderView(0).findViewById(R.id.nav_User_Image).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(AppUserModel.MAIN_USER.isUserLoaded())
				{
					Intent intent=new Intent(activity, ViewUser.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("User",AppUserModel.MAIN_USER);
					intent.putExtras(bundle);
					activity.startActivity(intent);
				}
				else
				{
					AppUserModel.MAIN_USER.LoginUserNoHome(activity,false);
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
		if(!openNewSearchPage && !backPressed())
			return true;
		return false;
	}

	public void setLabel(String label)
	{
		(( TextView)activity.findViewById(R.id.actionbar_title)).setText(label);
	}

	public void setOpenNewSearchPage(boolean openNewSearchPage)
	{
		this.openNewSearchPage = openNewSearchPage;
	}

	public void setSearchHint(String hint)
	{
		SearchView searchView=(SearchView)activity.findViewById(R.id.search);
		searchView.setQueryHint(hint);
	}

	private boolean backPressed()
	{
		if(activity.findViewById(R.id.search_bar).getVisibility()==View.VISIBLE)
		{
			activity.findViewById(R.id.search_bar).setVisibility(View.GONE);
			activity.findViewById(R.id.main_bar).setVisibility(View.VISIBLE);
			SearchView searchView=(SearchView)activity.findViewById(R.id.search);
			searchView.setQuery("",false);
			return false;
		}
		else
			return true;
	}
}
