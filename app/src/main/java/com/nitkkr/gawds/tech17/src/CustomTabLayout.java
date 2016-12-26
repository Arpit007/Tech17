package com.nitkkr.gawds.tech17.src;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * Created by Home Laptop on 26-Dec-16.
 */

public class CustomTabLayout extends TabLayout
{
	public CustomTabLayout(Context context)
	{
		super(context);
	}

	public CustomTabLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		try
		{
			if (getTabCount() == 0)
			{
				return;
			}
			Field field = TabLayout.class.getDeclaredField("mTabMinWidth");
			field.setAccessible(true);
			field.set(this, (int) ( getMeasuredWidth() / (float) getTabCount() ));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}