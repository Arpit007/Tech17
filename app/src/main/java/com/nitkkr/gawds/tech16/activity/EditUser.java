package com.nitkkr.gawds.tech16.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
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
import com.nitkkr.gawds.tech16.api.iResponseCallback;
import com.nitkkr.gawds.tech16.helper.ActionBarDoneButton;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.api.FetchData;
import com.nitkkr.gawds.tech16.helper.ResponseStatus;
import com.nitkkr.gawds.tech16.model.AppUserModel;
import com.nitkkr.gawds.tech16.model.InterestModel;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.src.CircularTextView;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUser extends AppCompatActivity
{
	private int ResponseCount=0;
	private final int PROFILE=200;
	private static final int INTEREST=900;
	private static final int AVATAR=700;
	private AppUserModel model= (AppUserModel)AppUserModel.MAIN_USER.clone();
	private boolean interestChanged=false, interestSuccess=false, profileSuccess=false;
	private ProgressDialog progressDialog=null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_user);

		ActivityHelper.setStatusBarColor(this);

		Initialise();

		ActionBarDoneButton bar=new ActionBarDoneButton(EditUser.this, new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(Check())
				{
					progressDialog=new ProgressDialog(EditUser.this);
					progressDialog.setMessage("Uploading Changes...");
					progressDialog.setCancelable(false);
					progressDialog.setIndeterminate(true);
					progressDialog.show();

					if(interestChanged)
						FetchData.getInstance().sendInterests(getApplicationContext(), model.getInterests(), model, new iResponseCallback()
						{
							@Override
							public void onResponse(ResponseStatus status)
							{
								if(status==ResponseStatus.FAILED)
									Toast.makeText(EditUser.this,"Failed to Update Interests",Toast.LENGTH_LONG).show();
								else if (status==ResponseStatus.NONE)
									Toast.makeText(EditUser.this,"Network Error",Toast.LENGTH_LONG).show();

								EditUser.this.onResponse(PROFILE,status);
							}

							@Override
							public void onResponse(ResponseStatus status, Object object)
							{
								this.onResponse(status);
							}
						});

					FetchData.getInstance().updateUserDetails(getApplicationContext(), model, new iResponseCallback()
					{
						@Override
						public void onResponse(ResponseStatus status)
						{
							if(status==ResponseStatus.FAILED)
								Toast.makeText(EditUser.this,"Failed to Update Profile",Toast.LENGTH_LONG).show();
							else if(status==ResponseStatus.NONE)
								Toast.makeText(EditUser.this,"Network Error",Toast.LENGTH_LONG).show();
							EditUser.this.onResponse(PROFILE,status);
						}

						@Override
						public void onResponse(ResponseStatus status, Object object)
						{
							this.onResponse(status);
						}
					});

					//TODO: Send info
					ResponseStatus status= ResponseStatus.SUCCESS;
					switch (status)
					{
						case SUCCESS:
							model.saveAppUser(EditUser.this);
							AppUserModel.MAIN_USER=model;
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
		((EditText)findViewById(R.id.user_Name)).setText(AppUserModel.MAIN_USER.getName());
		(( TextView)findViewById(R.id.user_Email)).setText(AppUserModel.MAIN_USER.getEmail());
		(( TextView)findViewById(R.id.user_College)).setText(AppUserModel.MAIN_USER.getCollege());
		(( TextView)findViewById(R.id.user_Roll)).setText(AppUserModel.MAIN_USER.getRoll());
		(( EditText)findViewById(R.id.user_Number)).setText(AppUserModel.MAIN_USER.getMobile());

		String Branches[] = getResources().getStringArray(R.array.Branches);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_modified,R.id.branch_selected,Branches);
		AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.user_Branch);
		spinner.setAdapter(adapter);
		spinner.setSelection(Arrays.asList(Branches).indexOf(AppUserModel.MAIN_USER.getBranch()));


		String Year[] = getResources().getStringArray(R.array.Year);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_modified,R.id.branch_selected,Year);
		AppCompatSpinner spinner1 = (AppCompatSpinner) findViewById(R.id.user_Year);
		spinner1.setAdapter(adapter1);
		spinner1.setSelection(Arrays.asList(Year).indexOf(AppUserModel.MAIN_USER.getYear()));


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
				interestChanged=true;
				model.setInterests((ArrayList<InterestModel>)data.getSerializableExtra("Interests"));
			}
		}
		else if(requestCode==AVATAR)
		{
			int ID=data.getIntExtra("ID",-1);
			AppUserModel.MAIN_USER.setImageId(ID);

			AppUserModel.MAIN_USER.setUseGoogleImage((ID==-1));

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

			findViewById(R.id.view_user_Image).setVisibility(View.INVISIBLE);
		}
	}

	private boolean Check()
	{
		if(((EditText)findViewById(R.id.user_Name)).getText().toString().equals("") || (( EditText)findViewById(R.id.user_Number)).getText().toString().equals(""))
		{
			Toast.makeText(EditUser.this,"All Fields Are Mandatory",Toast.LENGTH_SHORT).show();
			return false;
		}
		else model.setName(((EditText)findViewById(R.id.user_Name)).getText().toString());

		int length=(( EditText)findViewById(R.id.user_Number)).getText().length();
		if(length<10)
		{
			Toast.makeText(EditUser.this,"Contact Number Should Be 10 Digits Long",Toast.LENGTH_SHORT).show();
			return false;
		}
		else model.setMobile(((EditText)findViewById(R.id.user_Number)).getText().toString());

		model.setBranch(((Spinner) findViewById(R.id.user_Branch)).getSelectedItem().toString());
		model.setYear(((Spinner) findViewById(R.id.user_Year)).getSelectedItem().toString());

		return true;
	}

	@Override
	public void onBackPressed()
	{
		if(progressDialog!=null && progressDialog.isShowing())
			return;

		if(ActivityHelper.revertToHomeIfLast(EditUser.this))
			return;
		super.onBackPressed();
	}

	private void onResponse(int ID, ResponseStatus status)
	{
		ResponseCount++;

		if(ID==PROFILE)
			profileSuccess = status==ResponseStatus.SUCCESS;
		if(ID==INTEREST)
			interestSuccess = status==ResponseStatus.SUCCESS;

		if(ResponseCount==2)
		{
			if (progressDialog!=null)
				progressDialog.dismiss();

			if(!interestChanged || !interestSuccess)
				model.setInterests(AppUserModel.MAIN_USER.getInterests());
			if(!profileSuccess)
			{
				AppUserModel temp=(AppUserModel)AppUserModel.MAIN_USER.clone();
				temp.setInterests(model.getInterests());
				model=temp;
			}
			model.saveAppUser(getApplicationContext());
			AppUserModel.MAIN_USER=model;

			ResponseCount=0;
			profileSuccess=false;
			interestSuccess=false;
			interestChanged=false;
			Toast.makeText(getApplicationContext(),"Changes Updated Successfuully",Toast.LENGTH_LONG).show();
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					EditUser.this.finish();
				}
			},getResources().getInteger(R.integer.AutoCloseDuration));
		}
	}
}
