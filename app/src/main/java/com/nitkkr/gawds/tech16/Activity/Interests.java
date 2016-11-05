package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nitkkr.gawds.tech16.Adapter.InterestAdapter;
import com.nitkkr.gawds.tech16.Helper.ActionBarDone;
import com.nitkkr.gawds.tech16.R;

public class Interests extends AppCompatActivity
{
	private InterestAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interests);

		ListView listView=( ListView)findViewById(R.id.interest_list);
		adapter=new InterestAdapter(getBaseContext());

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				adapter.onItemClick(view,i);
			}
		});

		listView.setAdapter(adapter);

		ActionBarDone barDone=new ActionBarDone(this, new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(adapter.isDone())
				{
					String string=adapter.getInterestsString();
					//TODO:Send Info
					startActivity(new Intent(Interests.this,Home.class));
					finish();
				}
				else
					Toast.makeText(getBaseContext(),"Select minimum 1 Interest",Toast.LENGTH_LONG).show();
			}
		});
		barDone.setLabel("Interests");

	}

	@Override
	public void onBackPressed()
	{
		return;
	}
}
