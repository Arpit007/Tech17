package com.nitkkr.gawds.tech16.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nitkkr.gawds.tech16.adapter.AvatarAdapter;
import com.nitkkr.gawds.tech16.helper.ActionBarBack;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.R;

public class AvatarPicker extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avatar_picker);

		ActionBarBack barBack=new ActionBarBack(AvatarPicker.this);
		barBack.setLabel("Select an Avatar");

		GridView gridView=(GridView)findViewById(R.id.avatar_list);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				Intent intent=new Intent();
				intent.putExtra("ID",i);
				setResult(RESULT_OK,intent);
				finish();
			}
		});
		gridView.setAdapter(new AvatarAdapter(AvatarPicker.this));
	}

	@Override
	public void onBackPressed()
	{
		if(ActivityHelper.revertToHomeIfLast(AvatarPicker.this))
			return;
		super.onBackPressed();
	}
}
