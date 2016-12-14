package com.nitkkr.gawds.techspardha17.Src;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nitkkr.gawds.techspardha17.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Home Laptop on 18-Nov-16.
 */

public class CheckUpdate {
    private boolean UpdateAvailable = false;

    public static final CheckUpdate CHECK_UPDATE = new CheckUpdate();

    //No new Instance of Class
    private CheckUpdate() {
    }

    public boolean isUpdateAvailable() {
        return UpdateAvailable;
    }

    public boolean displayUpdate(final Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.Misc_Prefs), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.Misc_Prefs), Context.MODE_PRIVATE).edit();

        if (UpdateAvailable) {
            final Date date = new Date(preferences.getLong("Update_Date", new Date().getTime()));

            if (date.after(new Date()))
                return false;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.HOUR, context.getResources().getInteger(R.integer.AfterUpdateHours));

                    editor.putLong("Update_Date", calendar.getTime().getTime());
                    editor.apply();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.HOUR, context.getResources().getInteger(R.integer.LaterUpdateHours));
                    editor.putLong("Update_Date", calendar.getTime().getTime());
                    editor.apply();
                    dialogInterface.dismiss();
                }
            });
            builder.setTitle("Update");
            builder.setMessage(R.string.Update);
            builder.create().show();
            UpdateAvailable = false;
        }
        return true;
    }

    public void checkForUpdate(final Context context) {
        String url = "http://carreto.pt/tools/android-store-version/?package=";
        try {
            url += context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (Exception e) {
            e.printStackTrace();
            url += "null";
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null && response.has("status") && response.getBoolean("status") && response.has("version")) {
                        String version = response.getString("version");
                        if (context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName.compareTo(version) < 0) {
                            UpdateAvailable = true;
                        }
                    } else {
                        UpdateAvailable = false;
                    }
                } catch (Exception e) {
                    UpdateAvailable = false;
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UpdateAvailable = false;
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
