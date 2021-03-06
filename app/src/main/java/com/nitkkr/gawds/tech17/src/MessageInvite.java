package com.nitkkr.gawds.tech17.src;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.model.MessageModel;
import com.nitkkr.gawds.tech17.model.iMessageAction;

import org.json.JSONObject;

/**
 * Created by Home Laptop on 18-Nov-16.
 */

public class MessageInvite implements iMessageAction
{
	@Override
	public void performAction(MessageModel model, final Context context)
	{
		JSONObject object = getObject(model);

		String Team_Name = "", Event_Name = "", Team_Id = "";

		try
		{
			Team_Name = object.getString("Team_Name");
			Event_Name = object.getString("Team_Name");
			Team_Id = object.getString("Team_Id");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Team Invite");
		builder.setMessage("Do you want to join Team " + Team_Name + " for the Event: " + Event_Name + "?");
		builder.setCancelable(false);
		builder.setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				//Implement
				dialogInterface.dismiss();
			}
		});
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				//Implement
				dialogInterface.dismiss();
			}
		});
		builder.setNeutralButton("View Team", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				//Implement
			}
		});
		final AlertDialog alertDialog = builder.create();
		alertDialog.setOnShowListener(
				new DialogInterface.OnShowListener()
				{
					@Override
					public void onShow(DialogInterface arg0)
					{

						alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context, R.color.button_color));
						alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.button_color));
						alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.button_color));
					}
				});
		alertDialog.getWindow().getAttributes().windowAnimations = R.style.CloseDialogTheme;
	}

	@Override
	public MessageModel formatMessage(String Data)
	{
		//====================Implement============================
		return null;
	}

	@Override
	public JSONObject getObject(MessageModel model)
	{
		return null;
	}
}
