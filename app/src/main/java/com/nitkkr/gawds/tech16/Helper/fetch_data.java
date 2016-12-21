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
import com.nitkkr.gawds.tech16.Activity.Interests;
import com.nitkkr.gawds.tech16.Activity.Login;
import com.nitkkr.gawds.tech16.Activity.Splash;
import com.nitkkr.gawds.tech16.Model.AppUserModel;
import com.nitkkr.gawds.tech16.Model.EventModel;
import com.nitkkr.gawds.tech16.Model.UserModel;
import com.nitkkr.gawds.tech16.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                                for(int i=0;i<data.length();i++){

                                }
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


    //get full user info
    public void get_userInfo(final Context context){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.get_user_details_url) +"?token="+ AppUserModel.MAIN_USER.getToken(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        JSONObject response= null,status=null;
                        JSONObject data=null;

                        String RollNo,PhoneNumber,Branch,Year,College,Gender,Id;
                        int code;
                        boolean isNew;
                        try {
                            response = new JSONObject(res);
                            status=response.getJSONObject("status");
                            data=response.getJSONObject("data");
                            code=status.getInt("code");

                            if(code==200){

                                RollNo=String.valueOf(data.getInt("RollNo"));
                                PhoneNumber=data.getString("PhoneNumber");
                                Branch=data.getString("Branch");
                                Year=data.getString("Year");
                                College=data.getString("College");
                                Gender=data.getString("Gender");
                                Id=String.valueOf(data.getInt("Id"));

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

    //Update user data
    public void update_user_details(final Context context, final UserModel main_user){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.get_user_details_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        //check response if good send to handler
                        JSONObject response= null,status=null,data=null;
                        int code;

                        try {
                            response = new JSONObject(res);
                            status=response.getJSONObject("status");


                            code=status.getInt("code");

                            //save this token for further use
                            if(code==200){
                                //successfully executed

                                }else{
                                //something went wrong on server
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
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token",AppUserModel.MAIN_USER.getToken());
                params.put("name",main_user.getName());
                params.put("rollNo",main_user.getRoll());
                params.put("phoneNumber",main_user.getMobile());
                params.put("branch",main_user.getBranch());
                params.put("college",main_user.getCollege());
                params.put("year",main_user.getYear());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void send_interests(final Context context, final ArrayList<String> s){
        ///showProgressDialog("Optimising your feed...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.get_user_interests_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        //check response if good send to handler
                        JSONObject response= null,status=null;
                        JSONArray data;
                        String message,RollNo,PhoneNumber,Branch,Year,College,Gender;
                        ArrayList<String> interests=new ArrayList<String>();
                        int code;
                        try {
                            response = new JSONObject(res);
                            status=response.getJSONObject("status");

                            code=status.getInt("code");

                            if(code==200){

                            }else{
                                //failure
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(context,res,Toast.LENGTH_LONG).show();
                     }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token",AppUserModel.MAIN_USER.getToken());
                String interests_post_data="[";

                for(int i=0;i<s.size()-1;i++){
                    interests_post_data+='"'+s.get(i)+'"'+",";
                }
                interests_post_data+='"'+s.get(s.size()-1)+'"'+"]";
                params.put("interests",interests_post_data);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    //delete interests
    public void delete_interests(final Context context, final ArrayList<String> s){
        ///showProgressDialog("Optimising your feed...");
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.get_user_interests_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        //check response if good send to handler
                        JSONObject response= null,status=null;
                        int code;
                        try {
                            response = new JSONObject(res);
                            status=response.getJSONObject("status");

                            code=status.getInt("code");

                            if(code==200){

                            }else{
                                //failure
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                       // Toast.makeText(context,res,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token",AppUserModel.MAIN_USER.getToken());
                String interests_post_data="[";
                for(int i=0;i<s.size()-1;i++){
                    interests_post_data+='"'+s.get(i)+'"'+",";
                }
                interests_post_data+='"'+s.get(s.size()-1)+'"'+"]";
                params.put("interests",interests_post_data);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    //fetch all events
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
                                for(int i=0;i<data.length();i++){
                                    EventModel eventModel=new EventModel();

                                    eventModel.setId(data.getJSONObject(i).getInt("Id"));
                                    eventModel.setName(data.getJSONObject(i).getString("Name"));
                                    eventModel.setDescription(data.getJSONObject(i).getString("Description"));
                                    eventModel.setVenue(data.getJSONObject(i).getString("Venue"));
                                    eventModel.setStart(data.getJSONObject(i).getString("Start"));
                                    eventModel.setEnd(data.getJSONObject(i).getString("End"));
                                    eventModel.setCurrentRound(Integer.valueOf(data.getJSONObject(i).getString("CurrentRound")));
                                    eventModel.setMaxContestants(data.getJSONObject(i).getInt("MaxContestants"));
                                    eventModel.setStatus(data.getJSONObject(i).getString("Status"));
                                    eventModel.setPdf(data.getJSONObject(i).getString("Pdf"));
                                    eventModel.setCategoryId(data.getJSONObject(i).getInt("CategoryId"));
                                    eventModel. setSocietyId(data.getJSONObject(i).getInt("SocietyId"));

                                }
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

    //fetch single event description
    public void getEvent(final Context context,int Event_id,final EventModel eventModel){
       StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.getEvent)+Event_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        JSONObject response= null,status=null;
                        String message;
                        JSONObject data=null;
                        int code;
                        try {
                            response = new JSONObject(res);
                            status=response.getJSONObject("status");
                            data=response.getJSONObject("data");
                            code=status.getInt("code");
                            //message=status.getString("message");

                            if(code==200){
                                //success

                                eventModel.setId(data.getInt("Id"));
                                eventModel.setName(data.getString("Name"));
                                eventModel.setDescription(data.getString("Description"));
                                eventModel.setVenue(data.getString("Venue"));
                                eventModel.setStart(data.getString("Start"));
                                eventModel.setEnd(data.getString("End"));
                                eventModel.setCurrentRound(Integer.valueOf(data.getString("CurrentRound")));
                                eventModel.setMaxContestants(data.getInt("MaxContestants"));
                                eventModel.setStatus(data.getString("Status"));
                                eventModel.setPdf(data.getString("Pdf"));
                                eventModel.setCategoryId(data.getInt("CategoryId"));
                                eventModel. setSocietyId(data.getInt("SocietyId"));


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

    //search events
    public void searchFor(final Context context, String search_this, final ArrayList<EventModel> result){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.search_event) +"?query="+ search_this,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        JSONObject response= null,status=null;
                        JSONArray data=null;

                        String RollNo,PhoneNumber,Branch,Year,College,Gender,Id;
                        int code;
                        boolean isNew;
                        try {
                            response = new JSONObject(res);
                            status=response.getJSONObject("status");
                            data=response.getJSONArray("data");
                            code=status.getInt("code");

                            if(code==200){

                                for(int i=0;i<data.length();i++){
                                    EventModel eventModel=new EventModel();

                                    eventModel.setId(data.getJSONObject(i).getInt("Id"));
                                    eventModel.setName(data.getJSONObject(i).getString("Name"));
                                    eventModel.setDescription(data.getJSONObject(i).getString("Description"));
                                    eventModel.setVenue(data.getJSONObject(i).getString("Venue"));
                                    eventModel.setStart(data.getJSONObject(i).getString("Start"));
                                    eventModel.setEnd(data.getJSONObject(i).getString("End"));
                                    eventModel.setCurrentRound(Integer.valueOf(data.getJSONObject(i).getString("CurrentRound")));
                                    eventModel.setMaxContestants(data.getJSONObject(i).getInt("MaxContestants"));
                                    eventModel.setStatus(data.getJSONObject(i).getString("Status"));
                                    eventModel.setPdf(data.getJSONObject(i).getString("Pdf"));
                                    eventModel.setCategoryId(data.getJSONObject(i).getInt("CategoryId"));
                                    eventModel. setSocietyId(data.getJSONObject(i).getInt("SocietyId"));
                                    result.add(eventModel);

                                }
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

    //Register for an event
    public void RegisterEvent(final Context context, String Event_id){
        ///showProgressDialog("Optimising your feed...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url)+
              "api/event/"+Event_id+"/register" ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        //check response if good send to handler
                        JSONObject response= null,status=null;
                        int code;
                        try {
                            response = new JSONObject(res);
                            status=response.getJSONObject("status");

                            code=status.getInt("code");

                            if(code==200){
                                //success

                            }else{
                                //failure
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(context,res,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token",AppUserModel.MAIN_USER.getToken());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}

