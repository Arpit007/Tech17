package com.nitkkr.gawds.tech16.Model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class CoordinatorModel extends UserModel implements Serializable
{
	private String Designation;

	public String getDesignation(){return Designation;}
	public void setDesignation(String designation){Designation=designation;}

	public void CallCoordinator(Activity activity){
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getMobile()));
		activity.startActivity(intent);
	}

}
