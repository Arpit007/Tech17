package com.nitkkr.gawds.tech16.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nitkkr.gawds.tech16.Model.CoordinatorModel;
import com.nitkkr.gawds.tech16.Model.Coordinator_adapter;
import com.nitkkr.gawds.tech16.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 18-Dec-16.
 */
public class Contact_frag extends Fragment {

    private List<CoordinatorModel> coordinatorModelList=new ArrayList<>();
    private Coordinator_adapter mCoordinator_adapter;
    private RecyclerView mreRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView= inflater.inflate(R.layout.contact_frag,container,false);
        mreRecyclerView=(RecyclerView) rootView.findViewById(R.id.myrecyclerview);

        mCoordinator_adapter=new Coordinator_adapter(coordinatorModelList);


        LinearLayoutManager layoutManager=new LinearLayoutManager(rootView.getContext());
        layoutManager.canScrollVertically();
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mreRecyclerView.setLayoutManager(layoutManager);
        mreRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mreRecyclerView.setAdapter(mCoordinator_adapter);

        prepare_list();
        return rootView;
    }

    public void prepare_list(){

        CoordinatorModel c1=new CoordinatorModel();
        c1.setName("Coordinator 1");
        c1.setEmail("xyz@gmail.com");
        c1.setMobile("9999999999");
        coordinatorModelList.add(c1);

        CoordinatorModel c2=new CoordinatorModel();
        c2.setName("Coordinator 2");
        c2.setEmail("xyz@gmail.com");
        c2.setMobile("9999999999");
        coordinatorModelList.add(c2);

        CoordinatorModel c3=new CoordinatorModel();
        c3.setName("Coordinator 3");
        c3.setEmail("xyz@gmail.com");
        c3.setMobile("9999999999");
        coordinatorModelList.add(c3);

        mCoordinator_adapter.notifyDataSetChanged();

    }
}
