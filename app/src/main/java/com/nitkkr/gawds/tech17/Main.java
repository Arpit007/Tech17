package com.nitkkr.gawds.tech17;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by Home Laptop on 17-Jan-17.
 */

public class Main extends Application
{
	private static WeakReference<Context> contextWeakReference;

	@Override
	public void onCreate()
	{
		super.onCreate();

		contextWeakReference=new WeakReference<>(getApplicationContext());
		Log.i("Application","=========Application Started====================");
	}

	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
		Toast.makeText(this,"Device Low On Memory\nApp may close Unexpectedly",Toast.LENGTH_LONG).show();
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		Log.i("Application","=========Application Ended====================");
	}

	public static Context getContext()
	{
		return contextWeakReference.get();
	}
}
