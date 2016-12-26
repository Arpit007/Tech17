package com.nitkkr.gawds.tech16.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.model.EventModel;
import com.nitkkr.gawds.tech16.src.UpdateCheck;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dell on 18-Dec-16.
 */
public class Result_frag extends Fragment {

    private EventModel model;
    private View MyView;

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
        MyView=inflater.inflate(R.layout.fragment_about,container,false);
        fetchResult();
        return MyView;
    }

    void fetchResult()
    {
        TextView textView=(TextView)MyView.findViewById(R.id.NotAvail);
        textView.setVisibility(View.VISIBLE);
        if(!UpdateCheck.getInstance().isUpdateAvailable())
            textView.setText("Feature Coming Soon");
        else
        {
            textView.setText("Update Now to Access This Feature");
            textView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Answers.getInstance().logCustom(new CustomEvent("Updated App"));
                    SharedPreferences preferences=getContext().getSharedPreferences(getContext().getString(R.string.Misc_Prefs), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=getContext().getSharedPreferences(getContext().getString(R.string.Misc_Prefs),Context.MODE_PRIVATE).edit();

                    Date date=new Date(preferences.getLong("Update_Date",new Date().getTime()));

                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.HOUR,getContext().getResources().getInteger(R.integer.AfterUpdateHours));

                    editor.putBoolean("Update",false);

                    editor.putLong("Update_Date",calendar.getTime().getTime());
                    editor.apply();

                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id="+getContext().getPackageName()));
                    getContext().startActivity(intent);
                }
            });
        }
    }
}
