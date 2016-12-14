package com.nitkkr.gawds.techspardha17.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitkkr.gawds.techspardha17.Helper.ActionBarBack;
import com.nitkkr.gawds.techspardha17.Helper.ActivityHelper;
import com.nitkkr.gawds.techspardha17.Model.UserModel;
import com.nitkkr.gawds.techspardha17.R;

public class ViewUser extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_user);

		UserModel model=(UserModel)getIntent().getExtras().getSerializable("User");

		(( TextView)findViewById(R.id.user_Name)).setText(model.getName());
		(( TextView)findViewById(R.id.user_Email)).setText(model.getEmail());
		(( TextView)findViewById(R.id.user_College)).setText(model.getCollege());
		(( TextView)findViewById(R.id.user_Roll)).setText(model.getRoll());
		(( TextView)findViewById(R.id.user_Branch)).setText(model.getBranch());
		(( TextView)findViewById(R.id.user_Number)).setText(model.getMobile());

		//-----------------------------------Set User Image---------------------
		ImageView userImage=( ImageView)findViewById(R.id.user_Image);

		ActionBarBack barBack=new ActionBarBack(ViewUser.this);
		barBack.setLabel(model.getName());
	}

	@Override
	public void onBackPressed()
	{
		if(ActivityHelper.revertToHomeIfLast(ViewUser.this))
			return;
		super.onBackPressed();
	}
}
