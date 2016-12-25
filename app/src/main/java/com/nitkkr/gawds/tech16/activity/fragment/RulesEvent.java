package com.nitkkr.gawds.tech16.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.model.EventModel;
import com.nitkkr.gawds.tech16.R;

/**
 * Created by Dell on 18-Dec-16.
 */
public class RulesEvent extends Fragment {

    private EventModel model;

    public static RulesEvent getNewFragment(EventModel model)
    {
        RulesEvent rules_frag=new RulesEvent();
        rules_frag.model=model;
        return rules_frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.fragment_about,container,false);

        if (model!=null)
        {
            WebView webView=(WebView)view.findViewById(R.id.Event_Content);
            String text = "<html><head>"
                    + "<style type=\"text/css\">body{color: #fff; }"
                    + "</style></head>"
                    + "<body>"
                    + model.getRules()
                    + "</body></html>";
            webView.loadDataWithBaseURL(null,text,"text/html","utf-8",null);
            webView.setBackgroundColor(Color.TRANSPARENT);
        }

        return view;
    }
}
