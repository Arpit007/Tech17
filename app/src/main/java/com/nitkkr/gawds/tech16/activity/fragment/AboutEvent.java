package com.nitkkr.gawds.tech16.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.model.EventModel;

/**
 * Created by Dell on 18-Dec-16.
 */
public class AboutEvent extends Fragment
{
    private EventModel model;

    public static AboutEvent getNewFragment(EventModel model)
    {
        AboutEvent aboutFragment=new AboutEvent();
        aboutFragment.model=model;
        return aboutFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.fragment_about,container,false);

        WebView webView=(WebView)view.findViewById(R.id.Event_Content);
        String text = "<html><head>"
                + "<style type=\"text/css\">body{color: #fff; }"
                + "</style></head>"
                + "<body>"
                + model.getDescription()
                + "</body></html>";
        webView.loadDataWithBaseURL(null,text,"text/html","utf-8",null);
        webView.setBackgroundColor(Color.TRANSPARENT);
        return view;
    }
}
