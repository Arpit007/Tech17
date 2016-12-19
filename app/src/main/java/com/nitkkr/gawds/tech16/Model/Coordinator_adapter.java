package com.nitkkr.gawds.tech16.Model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nitkkr.gawds.tech16.R;

import java.util.List;

/**
 * Created by Dell on 19-Dec-16.
 */

public class Coordinator_adapter extends RecyclerView.Adapter<Coordinator_adapter.MyViewHolder> {

    private List<CoordinatorModel> coordinatorModelList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name,email,mobile;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.coordinator_name_txtview);
            email = (TextView) view.findViewById(R.id.coordinator_email_txtview);
            mobile = (TextView) view.findViewById(R.id.coordinator_call_txtview);
        }

    }

    public Coordinator_adapter(List<CoordinatorModel> coordinatorModelList) {
        this.coordinatorModelList = coordinatorModelList;
    }
    @Override
    public Coordinator_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_frag_listview_design, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(Coordinator_adapter.MyViewHolder holder, int position) {

        CoordinatorModel coordinatorModel=coordinatorModelList.get(position);
        holder.email.setText(coordinatorModel.getEmail());
        holder.mobile.setText(coordinatorModel.getMobile());
        holder.name.setText(coordinatorModel.getName());
    }


    @Override
    public int getItemCount() {
        return coordinatorModelList.size();
    }

}
