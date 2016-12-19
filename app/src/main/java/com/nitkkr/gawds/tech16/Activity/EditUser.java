package com.nitkkr.gawds.tech16.Activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nitkkr.gawds.tech16.Database.Database;
import com.nitkkr.gawds.tech16.Helper.ActionBarDoneButton;
import com.nitkkr.gawds.tech16.Helper.ActivityHelper;
import com.nitkkr.gawds.tech16.Helper.ResponseStatus;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.Model.InterestModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.Src.CircularTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUser extends AppCompatActivity
{
	private static final int INTEREST=900;
	private static final int AVATAR=700;
	private String interest=AppUserModel.MAIN_USER.interestsToString();
	private String Name, Number, Branch;
	private ArrayList<InterestModel> interestModels;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_user);

		Initialise();

		ActionBarDoneButton bar=new ActionBarDoneButton(EditUser.this, new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(Check())
				{
					//TODO: Send info
					ResponseStatus status= ResponseStatus.NONE;
					switch (status)
					{
						case SUCCESS:
							AppUserModel.MAIN_USER.setName(Name);
							AppUserModel.MAIN_USER.setMobile(Number);
							AppUserModel.MAIN_USER.setBranch(Branch);
							AppUserModel.MAIN_USER.setInterests(interest);
							AppUserModel.MAIN_USER.saveAppUser(EditUser.this);
							Database.database.getInterestDB().addOrUpdateInterest(interestModels);
							finish();
							//When finished, check if View User Changes Itself
							break;
						case FAILED:
							Toast.makeText(EditUser.this,"Failed, Please Try Again",Toast.LENGTH_LONG).show();
							break;
						case OTHER:
							Toast.makeText(EditUser.this,"----------------message-------------------",Toast.LENGTH_LONG).show();
							break;
						default:
							break;
					}
				}
			}
		});
		bar.setLabel("Edit User");

		findViewById(R.id.edit_user_Interest).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent=new Intent(EditUser.this,Interests.class);
				intent.putExtra("Return_Interest",true);
				startActivityForResult(intent,INTEREST);
			}
		});

		findViewById(R.id.edit_user_Image).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				PopupMenu popup = new PopupMenu(EditUser.this, view);
				MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.image_menu, popup.getMenu());

				if(AppUserModel.MAIN_USER.getImageResource().equals(""))
					popup.getMenu().findItem(0).setVisible(false);

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem item)
					{
						switch (item.getItemId())
						{
							case R.id.google_Image:
								AppUserModel.MAIN_USER.setUseGoogleImage(true);
								break;

							case R.id.avatar:
								Intent intent=new Intent(EditUser.this,AvatarPicker.class);
								startActivityForResult(intent,AVATAR);
								break;

							case R.id.alphabet:
								AppUserModel.MAIN_USER.setUseGoogleImage(false);
								AppUserModel.MAIN_USER.setImageId(-1);
								break;
						}
						return false;
					}
				});
				popup.show();
			}
		});

	}

	public void Initialise()
	{
		interestModels= Database.database.getInterestDB().getAllInterests();
		((EditText)findViewById(R.id.user_Name)).setText(AppUserModel.MAIN_USER.getName());
		(( TextView)findViewById(R.id.user_Email)).setText(AppUserModel.MAIN_USER.getEmail());
		(( TextView)findViewById(R.id.user_College)).setText(AppUserModel.MAIN_USER.getCollege());
		(( TextView)findViewById(R.id.user_Roll)).setText(AppUserModel.MAIN_USER.getRoll());
		(( EditText)findViewById(R.id.user_Number)).setText(AppUserModel.MAIN_USER.getMobile());

		String Branches[] = getResources().getStringArray(R.array.Branches);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_modified,R.id.branch_selected,Branches);
		Spinner spinner = (Spinner) findViewById(R.id.user_Branch);
		spinner.setAdapter(adapter);

		int ID;
		for(ID=0;ID<Branches.length;ID++)
		{
			if(Branches[ID].equals(AppUserModel.MAIN_USER.getBranch()))
				break;
		}

		spinner.setSelection(ID);
		setImage();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==RESULT_OK)
		{
			if(resultCode==INTEREST)
			{
				interest=data.getStringExtra("InterestString");
				interestModels=(ArrayList<InterestModel>)data.getSerializableExtra("Interests");
			}
		}
		else if(requestCode==AVATAR)
		{
			int ID=data.getIntExtra("ID",-1);
			AppUserModel.MAIN_USER.setImageId(ID);
			if(ID!=-1)
				AppUserModel.MAIN_USER.setUseGoogleImage(false);
			AppUserModel.MAIN_USER.saveAppUser(EditUser.this);
			setImage();
		}
	}

	private void setImage()
	{
		if(AppUserModel.MAIN_USER.getImageResource()!=null && AppUserModel.MAIN_USER.isUseGoogleImage())
		{
			CircleImageView view=(CircleImageView)findViewById(R.id.view_user_Image);
			view.setVisibility(View.VISIBLE);

			Glide.with(EditUser.this).load(AppUserModel.MAIN_USER.getImageResource()).thumbnail(0.5f).centerCrop().into(view);

			findViewById(R.id.view_user_Image_Letter).setVisibility(View.INVISIBLE);
		}
		else if(AppUserModel.MAIN_USER.getImageId()!=-1)
		{
			CircleImageView view=(CircleImageView)findViewById(R.id.view_user_Image);
			view.setVisibility(View.VISIBLE);

			TypedArray array=getResources().obtainTypedArray(R.array.Avatar);
			view.setImageResource(array.getResourceId(AppUserModel.MAIN_USER.getImageId(),0));
			array.recycle();

			CircularTextView circularTextView=(CircularTextView)findViewById(R.id.view_user_Image_Letter);
			circularTextView.setVisibility(View.VISIBLE);
			circularTextView.setText("");
			circularTextView.setFillColor(ContextCompat.getColor(this,R.color.User_Image_Fill_Color));
		}
		else
		{
			CircularTextView view=(CircularTextView)findViewById(R.id.view_user_Image_Letter);

			view.setText(AppUserModel.MAIN_USER.getName().toUpperCase().charAt(0));
			view.setVisibility(View.VISIBLE);

			TypedArray array=getResources().obtainTypedArray(R.array.Flat_Colors);

			int colorPos=(AppUserModel.MAIN_USER.getName().toLowerCase().charAt(0)-'a')%array.length();

			view.setFillColor(array.getColor(colorPos,0));
			view.setBorderColor(ContextCompat.getColor(this,R.color.User_Image_Border_Color));

			array.recycle();

			findViewById(R.id.view_user_Image).setVisibility(View.GONE);
		}
	}

	private boolean Check()
	{
		if(((EditText)findViewById(R.id.user_Name)).getText().equals(""))
		{
			Toast.makeText(EditUser.this,"All Fields Are Mandatory",Toast.LENGTH_SHORT).show();
			return false;
		}
		else Name=((EditText)findViewById(R.id.user_Name)).getText().toString();
		int length=(( EditText)findViewById(R.id.user_Number)).getText().length();
		if(length<10)
		{
			Toast.makeText(EditUser.this,"All Fields Are Mandatory",Toast.LENGTH_SHORT).show();
			return false;
		}
		else Number=((EditText)findViewById(R.id.user_Number)).getText().toString();

		Branch=((Spinner) findViewById(R.id.user_Branch)).getSelectedItem().toString();

		return true;
	}

	@Override
	public void onBackPressed()
	{
		if(ActivityHelper.revertToHomeIfLast(EditUser.this))
			return;
		super.onBackPressed();
	}
}
