package com.nitkkr.gawds.tech16.src;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.nitkkr.gawds.tech16.helper.ActivityHelper;
import com.nitkkr.gawds.tech16.helper.ResponseStatus;
import com.nitkkr.gawds.tech16.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import static com.nitkkr.gawds.tech16.helper.ActivityHelper.getApplicationContext;

/**
 * Created by Home Laptop on 20-Dec-16.
 */

public class PdfHelper
{
	class Holder
	{
		public int ID;
		public iCallback callback;
	}

	/* TODO:Generate Notifications */
	HashMap<String, iCallback> Downloading;

	private static PdfHelper pdfHelper=new PdfHelper();

	public static PdfHelper getInstance(){return pdfHelper;}

	private PdfHelper(){
		Downloading = new HashMap<>();
	}

	private File getDataFolder()
	{
		File dataDir = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			dataDir = new File(Environment.getExternalStorageDirectory(), getApplicationContext().getString(R.string.FolderName));
			if(!dataDir.isDirectory())
			{
				dataDir.mkdirs();
			}
		}

		if(!dataDir.isDirectory())
		{
			dataDir = getApplicationContext().getDir(getApplicationContext().getString(R.string.FolderName),Context.MODE_PRIVATE);
		}

		return dataDir;
	}

	public interface iCallback
	{
		void DownloadComplete(String url, ResponseStatus status);
	}

	public String getFileName(String url)
	{
		return URLUtil.guessFileName(url,null,null);
	}

	public boolean isPdfExisting(String url)
	{
		return new File(getDataFolder(),getFileName(url)).exists();
	}

	public boolean isPdfDownloading(String url)
	{
		return Downloading.keySet().contains(getFileName(url));
	}

	public boolean DownloadPdf(final String url, final iCallback callback, Context context)
	{
		if(isPdfExisting(url))
			return false;

		if(!ActivityHelper.isInternetConnected())
		{
			Toast.makeText(context, "No Internet Connection",Toast.LENGTH_SHORT).show();
			return false;
		}

		Downloading.put(getFileName(url),callback);

		InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, url.toString(),
				new Response.Listener<byte[]>()
				{
					@Override
					public void onResponse(byte[] response)
					{
						try
						{
							if (response!=null)
							{
								File Path=new File(getDataFolder(),getFileName(url));
								FileOutputStream outputStream=new FileOutputStream(Path);
								outputStream.write(response);
								outputStream.close();

								Toast.makeText(getApplicationContext(), getFileName(url)+" Downloaded", Toast.LENGTH_LONG).show();

								try
								{
									iCallback call = Downloading.get(getFileName(url));
									Downloading.remove(getFileName(url));
									if (call != null)
										call.DownloadComplete(url, ResponseStatus.SUCCESS);
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
							else throw new Exception("No Response");
						}
						catch (Exception e)
						{
							Toast.makeText(getApplicationContext(), getFileName(url)+" Download Failed", Toast.LENGTH_LONG).show();

							try
							{
								iCallback call = Downloading.get(getFileName(url));
								Downloading.remove(getFileName(url));
								if (call != null)
									call.DownloadComplete(url, ResponseStatus.FAILED);
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}

							Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
							e.printStackTrace();
						}
					}
				} ,
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						Toast.makeText(getApplicationContext(), getFileName(url)+" Download Failed", Toast.LENGTH_LONG).show();

						try
						{
							iCallback call = Downloading.get(getFileName(url));
							Downloading.remove(getFileName(url));
							if (call != null)
								call.DownloadComplete(url, ResponseStatus.FAILED);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
						error.printStackTrace();
					}
				}, null);

		RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
		mRequestQueue.add(request);
		return true;
	}

	public void viewPdfIfExists(String url, Context context)
	{
		if(!isPdfExisting(url))
			return;

		File file = new File(getDataFolder(),getFileName(url));
		Intent target = new Intent(Intent.ACTION_VIEW);
		target.setDataAndType(Uri.fromFile(file),"application/pdf");
		target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

		Intent intent = Intent.createChooser(target, "Open File");
		try
		{
			context.startActivity(intent);
		} catch (Exception e)
		{
			Toast.makeText(context,"Install a Pdf Viewer",Toast.LENGTH_LONG).show();
		}
	}

	public void removeListener(String url)
	{
		if(isPdfDownloading(url))
			Downloading.put(getFileName(url),null);
	}

}
