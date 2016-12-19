package com.nitkkr.gawds.tech16.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.Model.EventModel;
import com.nitkkr.gawds.tech16.R;

/**
 * Created by Dell on 18-Dec-16.
 */
public class Rules_frag extends Fragment {

    private EventModel model;

    public static Rules_frag getNewFragment(EventModel model)
    {
        Rules_frag rules_frag=new Rules_frag();
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

        ((TextView)view.findViewById(R.id.Event_Content)).setText(model.getRules());

        return view;
    }

}
