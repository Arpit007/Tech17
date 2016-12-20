package com.nitkkr.gawds.tech16.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.Model.CoordinatorModel;
import com.nitkkr.gawds.tech16.R;

import java.util.List;

/**
 * Created by Dell on 19-Dec-16.
 */

public class CoordinatorAdapter extends RecyclerView.Adapter<CoordinatorAdapter.MyViewHolder>
{
    private List<CoordinatorModel> coordinatorModelList;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name,email,mobile;
        public ImageView callImg, emailImg;

        public MyViewHolder(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.Coordinator_Name);
            email = (TextView) view.findViewById(R.id.Coordinator_Email);
            mobile = (TextView) view.findViewById(R.id.Coordinator_Number);
            callImg=(ImageView)view.findViewById(R.id.imageView12);
            emailImg=(ImageView)view.findViewById(R.id.imageView13);
        }

    }

    public CoordinatorAdapter(List<CoordinatorModel> coordinatorModelList) {
        this.coordinatorModelList = coordinatorModelList;
    }

    @Override
    public CoordinatorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_item_contact, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CoordinatorAdapter.MyViewHolder holder, final int position)
    {
        CoordinatorModel coordinatorModel=coordinatorModelList.get(position);
        holder.email.setText(coordinatorModel.getEmail());
        holder.mobile.setText(coordinatorModel.getMobile());
        holder.name.setText(coordinatorModel.getName());

        View.OnClickListener emailListener=new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                coordinatorModelList.get(position).EmailCoordinator(view.getContext());
            }
        };

        View.OnClickListener callListener=new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                coordinatorModelList.get(position).CallCoordinator(view.getContext());
            }
        };

        holder.email.setOnClickListener(emailListener);
        holder.emailImg.setOnClickListener(emailListener);

        holder.callImg.setOnClickListener(callListener);
        holder.mobile.setOnClickListener(callListener);
    }


    @Override
    public int getItemCount() {
        return coordinatorModelList.size();
    }

}
