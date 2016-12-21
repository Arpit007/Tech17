package com.nitkkr.gawds.tech16.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.model.CoordinatorModel;
import com.nitkkr.gawds.tech16.adapter.CoordinatorAdapter;
import com.nitkkr.gawds.tech16.model.EventModel;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;

/**
 * Created by Dell on 18-Dec-16.
 */
public class ContactEvent extends Fragment
{
    private ArrayList<CoordinatorModel> coordinatorModelList=new ArrayList<>();
    private CoordinatorAdapter mCoordinatorAdapter;
    private RecyclerView mreRecyclerView;

    private EventModel model;

    public static ContactEvent getNewFragment(EventModel model)
    {
        ContactEvent contact_frag=new ContactEvent();
        contact_frag.model=model;

        return contact_frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView= inflater.inflate(R.layout.fragment_contact,container,false);
        mreRecyclerView=(RecyclerView) rootView.findViewById(R.id.Event_Contacts);

        mCoordinatorAdapter =new CoordinatorAdapter(coordinatorModelList);


        LinearLayoutManager layoutManager=new LinearLayoutManager(rootView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mreRecyclerView.setLayoutManager(layoutManager);
        mreRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mreRecyclerView.setAdapter(mCoordinatorAdapter);

        prepare_list();
        return rootView;
    }

    public void prepare_list()
    {
        coordinatorModelList= Database.database.getCoordinatorDB().getCoordinators(model);
        mCoordinatorAdapter.notifyDataSetChanged();
    }
}
