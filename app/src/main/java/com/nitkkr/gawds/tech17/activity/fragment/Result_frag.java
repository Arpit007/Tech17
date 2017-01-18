package com.nitkkr.gawds.tech17.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.Event;

/**
 * Created by Dell on 18-Dec-16.
 */
public class Result_frag extends Fragment
{
	private Event event;
	private View MyView;

	public static Result_frag getNewFragment(Event event)
	{
		Result_frag result_frag = new Result_frag();
		result_frag.event = event;
		return result_frag;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		MyView = inflater.inflate(R.layout.fragment_about, container, false);
		return MyView;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		setUpModel();
	}

	public void setUpModel()
	{
		if(this.getView()==null)
			return;

		View view=this.getView();

		WebView webView = (WebView) view.findViewById(R.id.Event_Content);
		TextView textView = (TextView) MyView.findViewById(R.id.NotAvail);

		if (event!=null && !event.getModel().getResult().equals(""))
		{
			String text;

			if (!event.getModel().getResult().contains("<br/>"))
			{
				text = "<html><head>"
						+ "<style type=\"text/css\">body{color: #fff; }"
						+ "</style></head>"
						+ "<body>"
						+ event.getModel().getResult().replaceAll("\n", "<br/>")
						+ "</body></html>";
			}
			else
			{
				text = "<html><head>"
						+ "<style type=\"text/css\">body{color: #fff; }"
						+ "</style></head>"
						+ "<body>"
						+ event.getModel().getResult()
						+ "</body></html>";
			}

			webView.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
			webView.setBackgroundColor(Color.TRANSPARENT);

			textView.setVisibility(View.INVISIBLE);
			webView.setVisibility(View.VISIBLE);
		}
		else
		{
			textView.setText("No Results Yet");
			textView.setVisibility(View.VISIBLE);
			webView.setVisibility(View.INVISIBLE);
		}
	}
}
