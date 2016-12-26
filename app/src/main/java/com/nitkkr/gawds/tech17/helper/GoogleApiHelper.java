package com.nitkkr.gawds.tech17.helper;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Dell on 16-Dec-16.
 */
public class GoogleApiHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
	private static final String TAG = GoogleApiHelper.class.getSimpleName();
	Context context;
	GoogleApiClient mGoogleApiClient;
	private String client_server_id = "726783559264-o574f9bvum7qdnlusrdmh0rnshqfnr8h.apps.googleusercontent.com";

	GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestEmail()
			.requestIdToken(client_server_id)
			.build();


	public GoogleApiHelper(Context context)
	{
		buildGoogleApiClient();
		connect();
		this.context = context;
	}

	public GoogleApiClient getGoogleApiClient()
	{
		return this.mGoogleApiClient;
	}

	public void connect()
	{
		if (mGoogleApiClient != null)
		{
			mGoogleApiClient.connect();
		}
	}

	public void disconnect()
	{
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
		{
			mGoogleApiClient.disconnect();
		}
	}

	public boolean isConnected()
	{
		return mGoogleApiClient != null && mGoogleApiClient.isConnected();
	}

	private void buildGoogleApiClient()
	{
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

	}

	@Override
	public void onConnected(Bundle bundle)
	{
		//You are connected do what ever you want
		//Like i get last known location
		// Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
	}

	@Override
	public void onConnectionSuspended(int i)
	{
		Log.d(TAG, "onConnectionSuspended: googleApiClient.connect()");
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult)
	{
		Log.d(TAG, "onConnectionFailed: connectionResult.toString() = " + connectionResult.toString());
	}


}
