package com.nitkkr.gawds.tech16.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nitkkr.gawds.tech16.Activity.Splash;
import com.nitkkr.gawds.tech16.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dell on 17-Dec-16.
 */

public class fetch_data {

    public static fetch_data f=new fetch_data();

    private void fetch_data(){

    }

    public static fetch_data getInstance(){
        return f;
    }

    public  void fetch_interests(final Context context){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.get_interests_list),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        JSONObject response= null,status=null;
                        String message;
                        JSONArray data=null;
                        int code;
                        try {
                            response = new JSONObject(res);
                            status=response.getJSONObject("status");
                            data=response.getJSONArray("data");
                            code=status.getInt("code");
                            //message=status.getString("message");

                            if(code==200){
                                //success
                                Log.v("DEBUG",data.toString());
                            }else{
                                //internal error
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public void fetch_events(final Context context){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.get_events_list),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        JSONObject response= null,status=null;
                        String message;
                        JSONArray data=null;
                        int code;
                        boolean isNew;
                        try {
                            response = new JSONObject(res);
                            status=response.getJSONObject("status");
                            data=response.getJSONArray("data");
                            code=status.getInt("code");
                            //message=status.getString("message");

                            if(code==200){
                                //success
                                Log.v("DEBUG",data.toString());
                            }else{
                                //internal error
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
}
