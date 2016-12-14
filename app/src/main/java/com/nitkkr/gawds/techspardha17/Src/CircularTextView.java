package com.nitkkr.gawds.techspardha17.Src;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Home Laptop on 04-Dec-16.
 */

public class CircularTextView extends TextView
{
	private float borderWidth;
	int borderColor, fillColor;

	public CircularTextView(Context context) {
		super(context);
	}

	public CircularTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	@Override
	public void draw(Canvas canvas) {

		Paint circlePaint = new Paint();
		circlePaint.setColor(fillColor);
		circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

		Paint strokePaint = new Paint();
		strokePaint.setColor(borderColor);
		strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

		int  h = this.getHeight();
		int  w = this.getWidth();

		int diameter = ((h > w) ? h : w);
		int radius = diameter/2;

		this.setHeight(diameter);
		this.setWidth(diameter);

		canvas.drawCircle(diameter / 2 , diameter / 2, radius, strokePaint);

		canvas.drawCircle(diameter / 2, diameter / 2, radius- borderWidth, circlePaint);

		super.draw(canvas);
	}

	public void setBorderWidth(int dp)
	{
		float scale = getContext().getResources().getDisplayMetrics().density;
		borderWidth = dp*scale;
	}

	public void setBorderColor(int color)
	{
		borderColor = color;
	}

	public void setFillColor(int color)
	{
		fillColor = color;

	}
}