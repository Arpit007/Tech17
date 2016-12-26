package com.nitkkr.gawds.tech16.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nitkkr.gawds.tech16.R;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.database.DbConstants;
import com.nitkkr.gawds.tech16.helper.ResponseStatus;
import com.nitkkr.gawds.tech16.model.AppUserModel;
import com.nitkkr.gawds.tech16.model.CoordinatorModel;
import com.nitkkr.gawds.tech16.model.EventKey;
import com.nitkkr.gawds.tech16.model.EventModel;
import com.nitkkr.gawds.tech16.model.EventStatus;
import com.nitkkr.gawds.tech16.model.ExhibitionModel;
import com.nitkkr.gawds.tech16.model.InterestModel;
import com.nitkkr.gawds.tech16.model.NotificationModel;
import com.nitkkr.gawds.tech16.model.SocietyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dell on 17-Dec-16.
 */

public class FetchData
{
    private FetchData(){}

    private static FetchData f=new FetchData();

    public static FetchData getInstance(){return f;}

    public  void fetchUserInterests(final Context context)
    {
        FetchResponseHelper.getInstance().incrementRequestCount();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.get_interests_list)+"?token="+AppUserModel.MAIN_USER.getToken(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        JSONObject response;
                        JSONArray data;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            data=response.getJSONArray("data");
                            code=response.getJSONObject("status").getInt("code");

                            if(code==200)
                            {
                                Database.getInstance().getInterestDB().resetTable();

                                ArrayList<InterestModel> list=new ArrayList<>();
                                for(int i=0;i<data.length();i++)
                                {
                                    InterestModel interestModel=new InterestModel();
                                    interestModel.setID(data.getInt(i));
                                    interestModel.setInterest(Database.getInstance().getInterestDB().getInterest(interestModel));
                                    interestModel.setSelected(true);
                                    list.add(interestModel);
                                }

                                Database.getInstance().getInterestDB().addOrUpdateInterest(list);

                                FetchResponseHelper.getInstance().incrementResponseCount(null);

                                Log.v("DEBUG",data.toString());
                            }
                            else
                            {
                                Log.d("Fetch:\t","Failed Fetching User Interests");
                                FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        FetchResponseHelper.getInstance().incrementResponseCount(error);
                        error.printStackTrace();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void getUserDetails(final Context context)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.get_user_details_url) +"?token="+ AppUserModel.MAIN_USER.getToken(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        JSONObject response,profile, data;

                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            data=response.getJSONObject("data");
                            code=response.getJSONObject("status").getInt("code");

                            if(code==200)
                            {
                                AppUserModel appUserModel=(AppUserModel)AppUserModel.MAIN_USER.clone();
                                appUserModel.setRoll(data.getInt("RollNo") + "");
                                appUserModel.setMobile(data.getString("PhoneNumber"));
                                appUserModel.setBranch(data.getString("Branch"));
                                appUserModel.setYear(data.getString("Year"));
                                appUserModel.setCollege(data.getString("College"));
                                appUserModel.setGender(data.getString("Gender"));

                                profile=data.getJSONObject("Profile");
                                appUserModel.setName(profile.getString("Name"));
                                appUserModel.setEmail(profile.getString("Email"));

                                appUserModel.saveAppUser(context);
                                AppUserModel.MAIN_USER = appUserModel;

                                FetchResponseHelper.getInstance().incrementResponseCount(null);
                                Log.v("DEBUG",data.toString());
                            }
                            else
                            {
                                FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                            }
                        }
                        catch (JSONException e)
                        {
                            FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        FetchResponseHelper.getInstance().incrementResponseCount(error);
                        error.printStackTrace();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void updateUserDetails(final Context context, final AppUserModel main_user, final iResponseCallback callback)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.get_user_details_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            code=response.getJSONObject("status").getInt("code");

                            if(code==200)
                            {
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.SUCCESS);
                            }
                            else
                            {
                                if (callback!=null)
                                    callback.onResponse(ResponseStatus.FAILED);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if (callback!=null)
                                callback.onResponse(ResponseStatus.FAILED);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        if (callback!=null)
                            if(error instanceof TimeoutError || error instanceof NetworkError)
                                callback.onResponse(ResponseStatus.NONE);
                            else callback.onResponse(ResponseStatus.FAILED);
                    }
                })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<>();
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

    public void sendInterests(final Context context, final ArrayList<InterestModel> models, final AppUserModel model, final iResponseCallback callback)
    {
        AppUserModel temp=new AppUserModel();
        temp.setInterests(models);
        final String Query=temp.selectedInterestsToString();
        if(Query.equals(""))
        {
            if(callback!=null)
                callback.onResponse(ResponseStatus.SUCCESS);
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.get_user_interests_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        try
                        {
                            response = new JSONObject(res);

                            if(response.getJSONObject("status").getInt("code")==200)
                            {
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.SUCCESS);
                            }
                            else
                            {
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.FAILED);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if(callback!=null)
                                callback.onResponse(ResponseStatus.FAILED);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        Log.v("DEBUG",error.toString());
                        if(callback!=null)
                            if(error instanceof TimeoutError || error instanceof NetworkError)
                                callback.onResponse(ResponseStatus.NONE);
                            else callback.onResponse(ResponseStatus.FAILED);
                    }
                })
        {
            @Override
            protected Map<String,String> getParams()
            {

                Map<String,String> params = new HashMap<>();
                params.put("token",model.getToken());
                params.put("interests",Query);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void deleteInterests(final Context context, final ArrayList<InterestModel> models, final AppUserModel model, final iResponseCallback callback)
    {
        AppUserModel temp=new AppUserModel();
        temp.setInterests(models);
        final String Query=temp.unSelectedInterestsToString();
        if(Query.equals(""))
        {
            if(callback!=null)
                callback.onResponse(ResponseStatus.SUCCESS);
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.get_user_interests_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        try
                        {
                            response = new JSONObject(res);

                            if(response.getJSONObject("status").getInt("code")==200)
                            {
                                Log.v("DEBUG",response.toString());
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.SUCCESS);
                            }
                            else
                            {
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.FAILED);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if(callback!=null)
                                callback.onResponse(ResponseStatus.FAILED);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        Log.v("DEBUG",error.toString());
                        if (callback!=null)
                            if(error instanceof TimeoutError || error instanceof NetworkError)
                                callback.onResponse(ResponseStatus.NONE);
                            else callback.onResponse(ResponseStatus.FAILED);
                    }
                })
        {
            @Override
            protected Map<String,String> getParams()
            {
                AppUserModel temp=new AppUserModel();
                temp.setInterests(models);

                Map<String,String> params = new HashMap<>();
                params.put("token",model.getToken());
                params.put("interests",Query);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void getEvent(final Context context, int eventId, final iResponseCallback callback)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.getEvent)+eventId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        JSONObject response;
                        JSONObject data;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            data=response.getJSONObject("data");
                            code=response.getJSONObject("status").getInt("code");

                            if(code==200)
                            {
                                EventModel eventModel=Database.getInstance().getEventsDB().getEvent(data.getInt("Id"));
                                eventModel.setEventID(data.getInt("Id"));
                                eventModel.setEventName(data.getString("Name"));
                                eventModel.setDescription(data.getString("Description"));
                                eventModel.setVenue(data.getString("Venue"));
                                eventModel.setEventDate(EventModel.parseDate(data.getString("Start")));
                                eventModel.setEventEndDate(EventModel.parseDate(data.getString("End")));
                                try
                                {
                                    eventModel.setCurrentRound(Integer.valueOf(data.getString("CurrentRound")));
                                }
                                catch (Exception e)
                                {
                                    eventModel.setCurrentRound(0);
                                }
                                eventModel.setMaxUsers(data.getInt("MaxContestants"));
                                eventModel.setStatus(EventStatus.Parse(data.getString("Status").toLowerCase()));
                                eventModel.setPdfLink(data.getString("Pdf"));
                                eventModel.setCategory(data.getInt("CategoryId"));
                                eventModel. setSociety(data.getInt("SocietyId"));

                                Log.v("DEBUG",data.toString());
                                if (callback!=null)
                                    callback.onResponse(ResponseStatus.SUCCESS,eventModel);
                            }
                            else
                            {
                                if (callback!=null)
                                    callback.onResponse(ResponseStatus.FAILED,new EventModel());
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if (callback!=null)
                                callback.onResponse(ResponseStatus.FAILED,new EventModel());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback!=null)
                            callback.onResponse(ResponseStatus.FAILED,new EventModel());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void searchFor(final Context context, String search_this, final ArrayList<EventModel> result)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.search_event) +"?query="+ search_this,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        JSONObject response;
                        JSONArray data;
                        int code;
                        try {
                            response = new JSONObject(res);
                            data=response.getJSONArray("data");
                            code=response.getJSONObject("status").getInt("code");

                            if(code==200){

                                for(int i=0;i<data.length();i++){
                                    EventModel eventModel=new EventModel();

                                    eventModel.setEventID(data.getJSONObject(i).getInt("Id"));
                                    eventModel.setEventName(data.getJSONObject(i).getString("Name"));
                                    eventModel.setDescription(data.getJSONObject(i).getString("Description"));
                                    eventModel.setVenue(data.getJSONObject(i).getString("Venue"));
                                    eventModel.setEventDate(EventModel.parseDate(data.getJSONObject(i).getString("Start")));
                                    eventModel.setEventEndDate(EventModel.parseDate(data.getJSONObject(i).getString("End")));
                                    try
                                    {
                                        eventModel.setCurrentRound(Integer.valueOf(data.getJSONObject(i).getString("CurrentRound")));
                                    }
                                    catch (Exception e)
                                    {
                                        eventModel.setCurrentRound(0);
                                    }
                                    eventModel.setMaxUsers(data.getJSONObject(i).getInt("MaxContestants"));
                                    eventModel.setStatus(EventStatus.Parse(data.getJSONObject(i).getString("Status").toLowerCase()));
                                    eventModel.setPdfLink(data.getJSONObject(i).getString("Pdf"));
                                    eventModel.setCategory(data.getJSONObject(i).getInt("CategoryId"));
                                    eventModel. setSociety(data.getJSONObject(i).getInt("SocietyId"));
                                    result.add(eventModel);

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

    public void registerSingleEvent(final Context context, String eventId, final iResponseCallback callback)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url)+
                "/api/event/"+eventId+"/register" ,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            code=response.getJSONObject("status").getInt("code");

                            if(code==200)
                            {
                                if (callback!=null)
                                    callback.onResponse(ResponseStatus.SUCCESS);
                            }
                            else
                            {
                                if (callback!=null)
                                    callback.onResponse(ResponseStatus.FAILED);
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if (callback!=null)
                                callback.onResponse(ResponseStatus.FAILED);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        if (callback!=null)
                            if(error instanceof TimeoutError || error instanceof NetworkError)
                                callback.onResponse(ResponseStatus.NONE);
                            else callback.onResponse(ResponseStatus.FAILED);
                    }
                })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<>();
                params.put("token",AppUserModel.MAIN_USER.getToken());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void getGTalk(final Context context, final EventKey key, final iResponseCallback callback)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+context.getResources().getString(R.string.guestLectures)
                +"/"+key.getEventID(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        JSONObject data;
                        int code;
                        try {
                            response = new JSONObject(res);
                            code = response.getJSONObject("status").getInt("code");
                            data = response.getJSONObject("data");
                            if (code == 200)
                            {
                                ExhibitionModel model=Database.getInstance().getExhibitionDB().getExhibition(key);

                                model.setEventID(data.getInt("Id"));
                                model.setEventName(data.getString("GuestName"));
                                model.setEventDate(EventModel.parseDate(data.getString("Start")));
                                model.setEventEndDate(EventModel.parseDate(data.getString("End")));
                                model.setImage_URL(data.getString("Photo"));
                                model.setDescription(data.getString("Description"));
                                model.setAuthor(data.getString("GuestName"));
                                model.setVenue(data.getString("Venue"));
                                model.setGTalk(true);

                                Database.getInstance().getExhibitionDB().addOrUpdateExhibition(model);
                                if (callback!=null)
                                    callback.onResponse(ResponseStatus.SUCCESS);

                            }
                            else
                            {
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.FAILED);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if(callback!=null)
                                callback.onResponse(ResponseStatus.FAILED);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        if (callback!=null)
                            if(error instanceof TimeoutError || error instanceof NetworkError)
                                callback.onResponse(ResponseStatus.NONE);
                            else callback.onResponse(ResponseStatus.FAILED);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void fetchUserWishlist(final Context context)
    {
        FetchResponseHelper.getInstance().incrementRequestCount();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+context.getResources().getString(R.string.userWishlist)
                +"?token="+AppUserModel.MAIN_USER.getToken(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        JSONArray data;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            code=response.getJSONObject("status").getInt("code");
                            if(code==200)
                            {
                                Database.getInstance().getExhibitionDB().resetTable();
                                data=response.getJSONArray("data");
                                for(int i=0;i<data.length();i++)
                                {
                                    ExhibitionModel key=Database.getInstance().getExhibitionDB().getExhibition(data.getInt(i));
                                    key.setNotify(true);
                                    Database.getInstance().getExhibitionDB().addOrUpdateExhibition(key);
                                }
                                Database.getInstance().getNotificationDB().UpdateTable();
                                FetchResponseHelper.getInstance().incrementResponseCount(null);
                            }
                            else
                            {
                                FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        FetchResponseHelper.getInstance().incrementResponseCount(error);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void addToWishlist(final Context context, final EventKey key, final iResponseCallback callback)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url)+context.getResources().getString(R.string.userWishlist)
                +"/"+key.getEventID(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            code=response.getJSONObject("status").getInt("code");
                            if(code==200)
                            {
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.SUCCESS);
                            }
                            else
                            {
                                if (callback!=null)
                                    callback.onResponse(ResponseStatus.FAILED);
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if(callback!=null)
                                callback.onResponse(ResponseStatus.FAILED);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        if(callback!=null)
                        {
                            if (error instanceof TimeoutError || error instanceof NetworkError)
                                callback.onResponse(ResponseStatus.NONE);
                            else callback.onResponse(ResponseStatus.FAILED);
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("token",AppUserModel.MAIN_USER.getToken());
                params.put("lectureId", String.valueOf(key.getEventID()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void removeFromWishlist(final Context context, final EventKey key, final iResponseCallback callback)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, context.getResources().getString(R.string.server_url)+context.getResources().getString(R.string.userWishlist)
                +"/"+key.getEventID()+"?token="+AppUserModel.MAIN_USER.getToken(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            code=response.getJSONObject("status").getInt("code");
                            if(code==200)
                            {
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.SUCCESS);
                            }
                            else
                            {
                                if (callback!=null)
                                    callback.onResponse(ResponseStatus.FAILED);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if (callback!=null)
                                callback.onResponse(ResponseStatus.FAILED);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();

                        if(callback!=null)
                        {
                            if (error instanceof TimeoutError || error instanceof NetworkError)
                                callback.onResponse(ResponseStatus.NONE);
                            else callback.onResponse(ResponseStatus.FAILED);
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void getSocieties(final Context context)
    {
        FetchResponseHelper.getInstance().incrementRequestCount();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+context.getResources().getString(R.string.getSocieties),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        JSONArray data;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            code=response.getJSONObject("status").getInt("code");
                            data=response.getJSONArray("data");
                            if(code==200)
                            {
                                ArrayList<SocietyModel> models=new ArrayList<>();
                                for(int i=0;i<data.length();i++)
                                {
                                    JSONObject object=data.getJSONObject(i);
                                    SocietyModel model=new SocietyModel();
                                    model.setName(object.getString("Name"));
                                    model.setID(object.getInt("Id"));
                                    model.setDescription(object.getString("Description"));
                                    models.add(model);
                                }
                                Database.getInstance().getSocietyDB().addOrUpdateSocities(models);
                                FetchResponseHelper.getInstance().incrementResponseCount(null);
                            }
                            else
                            {
                                FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        FetchResponseHelper.getInstance().incrementResponseCount(error);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    //get all notifications
    public void getNotifications(final Context context)
    {
        FetchResponseHelper.getInstance().incrementRequestCount();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.GetNotification)+"?token="+ AppUserModel.MAIN_USER.getToken(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        JSONArray data;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            code=response.getJSONObject("status").getInt("code");
                            data=response.getJSONArray("data");
                            if(code==200)
                            {
                                ArrayList<NotificationModel> models=new ArrayList<>();
                                for(int i=0;i<data.length();i++)
                                {
                                    //Status 0 for read and 1 for unread

                                    JSONObject object=data.getJSONObject(i);
                                    int Id=object.getInt("Id");
                                    int status=object.getInt("status");

                                    JSONObject NotificationObject=object.getJSONObject("Notification");
                                    String Message=NotificationObject.getString("Message");
                                    int EventId=NotificationObject.getInt("EventId");
                                }
                            }
                            else
                            {
                                FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        FetchResponseHelper.getInstance().incrementResponseCount(error);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    //change Notification Status
    public void changeNotificationStatus(final Context context, final int NotificationId,final int status, final iResponseCallback callback)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.GetNotification),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            code=response.getJSONObject("status").getInt("code");
                            //Status 0 for read and 1 for unread
                            if(code==200)
                            {
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.SUCCESS);
                            }
                            else
                            {
                                if (callback!=null)
                                    callback.onResponse(ResponseStatus.FAILED);
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if(callback!=null)
                                callback.onResponse(ResponseStatus.FAILED);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        if(callback!=null)
                        {
                            if (error instanceof TimeoutError || error instanceof NetworkError)
                                callback.onResponse(ResponseStatus.NONE);
                            else callback.onResponse(ResponseStatus.FAILED);
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("token",AppUserModel.MAIN_USER.getToken());
                params.put("notificationId", String.valueOf(NotificationId));
                params.put("status", String.valueOf(status));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void getLiveEvents(final Context context, final iResponseCallback callback)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.liveEvents),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        JSONArray data;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            data = response.getJSONArray("data");
                            code = response.getJSONObject("status").getInt("code");

                            if (code == 200)
                            {
                                ArrayList<EventModel> eventModels = Database.getInstance().getEventsDB().getAllEvents();
                                int Size=eventModels.size();

                                for (int i = 0; i < data.length(); i++)
                                {
                                    EventModel eventModel = new EventModel();
                                    JSONObject jEvent = data.getJSONObject(i);

                                    int ID=jEvent.getInt("Id"), index=-1;

                                    for(int x=0;x<Size;x++)
                                    {
                                        if(eventModels.get(x).getEventID()==ID)
                                        {
                                            eventModel=eventModels.get(x);
                                            index=x;
                                            break;
                                        }
                                    }
                                    eventModel.setEventID(ID);
                                    eventModel.setEventName(jEvent.getString("Name"));
                                    eventModel.setDescription(jEvent.getString("Description"));
                                    eventModel.setVenue(jEvent.getString("Venue"));
                                    eventModel.setEventDate(EventModel.parseDate(jEvent.getString("Start")));
                                    eventModel.setEventEndDate(EventModel.parseDate(jEvent.getString("End")));
                                    try
                                    {
                                        eventModel.setCurrentRound(Integer.valueOf(jEvent.getString("CurrentRound")));
                                    }
                                    catch (Exception e)
                                    {
                                        eventModel.setCurrentRound(0);
                                    }
                                    eventModel.setMaxUsers(jEvent.getInt("MaxContestants"));
                                    eventModel.setStatus(EventStatus.Parse(jEvent.getString("Status").toLowerCase()));
                                    eventModel.setPdfLink(jEvent.getString("Pdf"));
                                    eventModel.setRules(jEvent.getString("Rules"));
                                    eventModel.setCategory(jEvent.getInt("CategoryId"));
                                    eventModel.setSociety(jEvent.getInt("SocietyId"));

                                    if(index==-1)
                                        eventModels.add(eventModel);
                                }

                                Database.getInstance().getEventsDB().addOrUpdateEvent(eventModels);
                                Log.v("DEBUG", data.toString());
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.SUCCESS,eventModels);
                            }
                            else
                            {
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.FAILED,null);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if(callback!=null)
                                callback.onResponse(ResponseStatus.FAILED,null);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        if(callback!=null)
                            callback.onResponse(ResponseStatus.FAILED,null);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void fetchInterestedEvents(final Context context, final iResponseCallback callback)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+
                context.getResources().getString(R.string.interestedEvents)+"?token="+AppUserModel.MAIN_USER.getToken(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        JSONArray data;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            data = response.getJSONArray("data");
                            code = response.getJSONObject("status").getInt("code");

                            if (code == 200)
                            {
                                ArrayList<EventKey> keys=new ArrayList<>();
                                for(int i=0;i<data.length();i++)
                                {
                                    EventKey key=new EventKey();
                                    JSONObject object=data.getJSONObject(i);
                                    key.setEventName(object.getString("Name"));
                                    key.setEventID(object.getInt("Id"));
                                    //object.getInt("CategoryId");
                                    keys.add(key);
                                }
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.SUCCESS,keys);
                            }
                            else
                            {
                                if(callback!=null)
                                    callback.onResponse(ResponseStatus.FAILED,null);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if(callback!=null)
                                callback.onResponse(ResponseStatus.FAILED,null);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        if(callback!=null)
                            callback.onResponse(ResponseStatus.FAILED,null);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public  void fetchAll(final Context context)
    {
        FetchResponseHelper.getInstance().incrementRequestCount();

        String Url = context.getResources().getString(R.string.server_url) + context.getResources().getString(R.string.getCategories);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String res)
            {
                JSONObject response;
                JSONArray data;
                int code;
                try
                {
                    response = new JSONObject(res);
                    data = response.getJSONArray("data");
                    code = response.getJSONObject("status").getInt("code");
                    ArrayList<InterestModel> list = Database.getInstance().getInterestDB().getAllInterests();
                    if (code == 200)
                    {
                        for (int i = 0; i < data.length(); i++)
                        {
                            JSONObject object=data.getJSONObject(i);
                            InterestModel interestModel = Database.getInstance().getInterestDB().getInterestModel(object.getInt("Id"));
                            interestModel.setID(object.getInt("Id"));
                            interestModel.setInterest(object.getString("Name"));
                            list.add(interestModel);
                        }
                        Database.getInstance().getInterestDB().deleteTable();
                        Database.getInstance().getInterestDB().onCreate(Database.getInstance().getDatabase());
                        Database.getInstance().getInterestDB().addOrUpdateInterest(list);
                        Log.v("DEBUG", data.toString());
                        FetchData.getInstance().fetchData(context);
                    }
                    else
                    {
                        Log.d("Fetch:\t","Failed fetching All Interests");
                        FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                    }
                }
                catch (JSONException e)
                {
                    Log.d("Fetch:\t", "Failed Fetching Interests");
                    FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        FetchResponseHelper.getInstance().incrementResponseCount(error);
                        error.printStackTrace();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void fetchData(final Context context)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) +
                context.getResources().getString(R.string.get_events_list),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {

                        JSONObject response;
                        JSONArray data, coordinators;
                        int code;

                        try
                        {
                            response = new JSONObject(res);
                            data = response.getJSONArray("data");
                            code = response.getJSONObject("status").getInt("code");

                            Log.v("DEBUG","FULL EVENT DATA"+response.toString());

                            if (code == 200)
                            {
                                ArrayList<EventModel> eventModels = Database.getInstance().getEventsDB().getEvents("");
                                ArrayList<EventModel> finalEvents=new ArrayList<>();

                                ArrayList<ExhibitionModel> exhibitionModels=Database.getInstance().getExhibitionDB().getExhibitions(DbConstants.ExhibitionNames.GTalk.Name() + " = 0");
                                ArrayList<ExhibitionModel> finalExhibition=new ArrayList<>();

                                ArrayList<CoordinatorModel> coordinatorModels = new ArrayList<>();

                                int evSize=eventModels.size(), exSize=exhibitionModels.size();
                                int ID;
                                boolean isInformal;

                                for (int i = 0; i < data.length(); i++)
                                {
                                    JSONObject jEvent = data.getJSONObject(i);
                                    String Category=Database.getInstance().getInterestDB().getInterest(jEvent.getInt("CategoryId")).toLowerCase();

                                    if(Category.equals("exhibition"))
                                    {
                                        ExhibitionModel model = new ExhibitionModel();
                                        JSONObject object = data.getJSONObject(i);

                                        ID=jEvent.getInt("Id");

                                        for(int x=0;x<exSize;x++)
                                        {
                                            if(exhibitionModels.get(x).getEventID()==ID)
                                            {
                                                model=exhibitionModels.get(x);
                                                break;
                                            }
                                        }

                                        model.setEventName(object.getString("Name"));
                                        model.setEventID(object.getInt("Id"));
                                        model.setEventDate(EventModel.parseDate(object.getString("Start")));
                                        model.setEventEndDate(EventModel.parseDate(object.getString("End")));
                                        model.setImage_URL(object.getString("Image"));
                                        model.setDescription(object.getString("Description"));
                                        model.setAuthor("");
                                        model.setVenue(object.getString("Venue"));
                                        model.setGTalk(false);

                                        finalExhibition.add(model);
                                    }
                                    else
                                    {
                                        Category = Database.getInstance().getSocietyDB().getSocietyName(jEvent.getInt("SocietyId")).toLowerCase();
                                        String Temp1=Database.getInstance().getInterestDB().getInterest(jEvent.getInt("CategoryId")).toLowerCase();
                                        isInformal = ( Category.equals("informals") || Category.equals("informalz") || Temp1.equals("informals") || Temp1.equals("informalz"));

                                        ID = jEvent.getInt("Id");

                                        EventModel eventModel = new EventModel();

                                        for (int x = 0; x < evSize; x++)
                                        {
                                            if (eventModels.get(x).getEventID() == ID)
                                            {
                                                eventModel = eventModels.get(x);
                                                break;
                                            }
                                        }

                                        eventModel.setInformal(isInformal);
                                        eventModel.setEventID(ID);
                                        eventModel.setEventName(jEvent.getString("Name"));
                                        eventModel.setDescription(jEvent.getString("Description"));
                                        eventModel.setVenue(jEvent.getString("Venue"));
                                        eventModel.setEventDate(EventModel.parseDate(jEvent.getString("Start")));
                                        eventModel.setEventEndDate(EventModel.parseDate(jEvent.getString("End")));
                                        try
                                        {
                                            eventModel.setCurrentRound(Integer.valueOf(jEvent.getString("CurrentRound")));
                                        }
                                        catch (Exception e)
                                        {
                                            eventModel.setCurrentRound(0);
                                        }
                                        eventModel.setMaxUsers(jEvent.getInt("MaxContestants"));
                                        eventModel.setStatus(EventStatus.Parse(jEvent.getString("Status").toLowerCase()));
                                        eventModel.setPdfLink(jEvent.getString("Pdf"));
                                        eventModel.setRules(jEvent.getString("Rules"));
                                        eventModel.setCategory(jEvent.getInt("CategoryId"));
                                        eventModel.setSociety(jEvent.getInt("SocietyId"));

                                        finalEvents.add(eventModel);

                                        coordinators = jEvent.getJSONArray("Coordinators");

                                        for (int j = 0; j < coordinators.length(); j++)
                                        {
                                            JSONObject jCoordinator = coordinators.getJSONObject(j);
                                            CoordinatorModel coordinatorModel = new CoordinatorModel();
                                            coordinatorModel.setEventID(eventModel.getEventID());
                                            coordinatorModel.setName(jCoordinator.getString("Name"));
                                            coordinatorModel.setMobile(String.valueOf(jCoordinator.getLong("PhoneNo")));
                                            coordinatorModel.setEmail(jCoordinator.getString("Email"));
                                            coordinatorModel.setDesignation("Coordinator");
                                            coordinatorModels.add(coordinatorModel);
                                        }
                                    }
                                }

                                fetchAllGTalks(context,exhibitionModels,finalExhibition);

                                Database.getInstance().getEventsDB().deleteTable();
                                Database.getInstance().getEventsDB().onCreate(Database.getInstance().getDatabase());
                                Database.getInstance().getEventsDB().addOrUpdateEvent(finalEvents);

                                Database.getInstance().getCoordinatorDB().deleteTable();
                                Database.getInstance().getCoordinatorDB().onCreate(Database.getInstance().getDatabase());
                                Database.getInstance().getCoordinatorDB().addOrUpdateCoordinator(coordinatorModels);
                            }
                            else
                            {
                                FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                            }
                        }
                        catch (JSONException e)
                        {
                            FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        FetchResponseHelper.getInstance().incrementResponseCount(error);
                        error.printStackTrace();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void fetchAllGTalks(final Context context, final ArrayList<ExhibitionModel> initialList, final ArrayList<ExhibitionModel> finalList)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)+context.getResources().getString(R.string.guestLectures),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res)
                    {
                        JSONObject response;
                        JSONArray data;
                        int code;
                        try
                        {
                            response = new JSONObject(res);
                            code=response.getJSONObject("status").getInt("code");
                            data=response.getJSONArray("data");
                            if(code==200)
                            {
                                ArrayList<ExhibitionModel> models=initialList;
                                int Size=models.size();
                                for(int i=0;i<data.length();i++)
                                {
                                    ExhibitionModel model = new ExhibitionModel();
                                    JSONObject object = data.getJSONObject(i);

                                    int ID=object.getInt("Id");

                                    for(int x=0;x<Size;x++)
                                    {
                                        if(models.get(x).getEventID()==ID)
                                        {
                                            model=models.get(x);
                                            break;
                                        }
                                    }

                                    model.setEventName(object.getString("GuestName"));
                                    model.setEventID(object.getInt("Id"));
                                    model.setEventDate(EventModel.parseDate(object.getString("Start")));
                                    model.setEventEndDate(EventModel.parseDate(object.getString("End")));
                                    model.setImage_URL(object.getString("Photo"));
                                    model.setDescription(object.getString("Description"));
                                    model.setAuthor(object.getString("Designation"));
                                    model.setVenue(object.getString("Venue"));
                                    model.setGTalk(true);

                                    finalList.add(model);
                                }
                                Log.v("DEBUG","GUSTO TALK"+data.toString());
                                Database.getInstance().getExhibitionDB().deleteTable();
                                Database.getInstance().getExhibitionDB().onCreate(Database.getInstance().getDatabase());
                                Database.getInstance().getExhibitionDB().addOrUpdateExhibition(finalList);
                                FetchResponseHelper.getInstance().incrementResponseCount(null);
                            }
                            else
                            {
                                FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            FetchResponseHelper.getInstance().incrementResponseCount(new VolleyError());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        FetchResponseHelper.getInstance().incrementResponseCount(error);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}


