package com.nitkkr.gawds.tech16.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nitkkr.gawds.tech16.R;

public class Home_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle=getIntent().getExtras();
        String event=bundle.getString("Event");
        switch (event){

            case "Live_Event":
                getSupportActionBar().setTitle("Live Event");
                //load frag
                break;
            case "Notification_Event":
                getSupportActionBar().setTitle("Notifications");
                //load frag
                break;
            case "Interested_Event":
                getSupportActionBar().setTitle("Interested Events");
                //load frag
                break;
            case "Wishlist_Event":
                getSupportActionBar().setTitle("Your Wishlist");
                //load frag
                break;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
