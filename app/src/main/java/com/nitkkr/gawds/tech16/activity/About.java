package com.nitkkr.gawds.tech16.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.activity.fragment.ScreenSlidePageFragment;
import com.nitkkr.gawds.tech16.helper.SlideTransformer;

public class About extends FragmentActivity {

	private boolean isLogin=false, LastReached=false, Exit=false;
	private ViewPager mViewPager;
	private PagerAdapter mViewPagerAdapter;
	View view;
	int[] indicators = {R.id.first,R.id.second,R.id.third,R.id.fourth,R.id.fifth};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		ActivityHelper.setCreateAnimation(this);

		isLogin=getIntent().getBooleanExtra("Login",false);

		ActivityHelper.setStatusBarColor(About.this);

		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mViewPager.setOffscreenPageLimit(0);
		view = findViewById(R.id.page_indicator);
		mViewPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setPageTransformer(true,new SlideTransformer());
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				View circle;

				for(int i=0;i<5;i++)
				{
					if(i == (position)) {
						circle = view.findViewById(indicators[position]).findViewById(R.id.indicator_item);
						circle.setBackgroundResource(R.drawable.page_indicator_dot);
					} else {
						circle = view.findViewById(indicators[i]).findViewById(R.id.indicator_item);
						circle.setBackgroundResource(R.drawable.indicator_dot_not_selected);
					}
				}
				if(position==4 && isLogin)
				{
					LastReached=true;
					findViewById(R.id.Login_Button).setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		findViewById(R.id.Login_Button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				startActivity(new Intent(About.this,Login.class));
				finish();
			}
		});
	}
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}


		@Override
		public Fragment getItem(int position) {

			return ScreenSlidePageFragment.init(position);
		}

		@Override
		public int getCount() {
			return ScreenSlidePageFragment.images.length;
		}
	}

	@Override
	public void onBackPressed()
	{
		if(isLogin)
		{
			if(Exit)
			{
				finish();
				System.exit(-1);
			}
			if(LastReached)
			{
				startActivity(new Intent(About.this, Login.class));
				finish();
			}
			else
			{
				Exit=true;
				Toast.makeText(About.this,"Press Back Again to Exit",Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						Exit=false;
					}
				},2000);
			}
			return;
		}

		if(ActivityHelper.revertToHomeIfLast(About.this));
		else super.onBackPressed();

		ActivityHelper.setExitAnimation(this);
	}
}