package com.nitkkr.gawds.tech16.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nitkkr.gawds.tech16.Model.EventModel;
import com.nitkkr.gawds.tech16.R;

/**
 * Created by Dell on 18-Dec-16.
 */
public class Result_frag extends Fragment {

    private EventModel model;

    public static Result_frag getNewFragment(EventModel model)
    {
        Result_frag result_frag=new Result_frag();
        result_frag.model=model;
        return result_frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_about,container,false);
    }

    void fetchResult()
    {
        //TODO:Implement
    }
}
