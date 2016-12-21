package com.nitkkr.gawds.tech16.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nitkkr.gawds.tech16.api.iResponseCallback;
import com.nitkkr.gawds.tech16.database.Database;
import com.nitkkr.gawds.tech16.model.AppUserModel;
import com.nitkkr.gawds.tech16.model.CoordinatorModel;
import com.nitkkr.gawds.tech16.model.EventModel;
import com.nitkkr.gawds.tech16.model.InterestModel;
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

public class Fetch_Data1
{
    private Fetch_Data1(){}

    private static Fetch_Data1 f=new Fetch_Data1();

    public static Fetch_Data1 getInstance(){return f;}

    public  void fetchAllInterests(final Context context)
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
                    ArrayList<InterestModel> list = new ArrayList<>();
                    if (code == 200)
                    {
                        for (int i = 0; i < data.length(); i++)
                        {
                            InterestModel interestModel = new InterestModel();
                            interestModel.setID(data.getJSONObject(i).getInt("Id"));
                            interestModel.setInterest(data.getJSONObject(i).getString("Name"));
                            list.add(interestModel);
                        }
                        Database.database.getInterestDB().addOrUpdateInterest(list);
                        Log.v("DEBUG", data.toString());
                        FetchResponseHelper.getInstance().incrementResponseCount(null);
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
                                ArrayList<InterestModel> list=new ArrayList<>();
                                for(int i=0;i<data.length();i++)
                                {
                                    InterestModel interestModel=new InterestModel();
                                    interestModel.setID(data.getJSONObject(i).getInt("Id"));
                                    interestModel.setInterest(Database.database.getInterestDB().getInterest(interestModel));
                                    interestModel.setSelected(true);
                                    list.add(interestModel);
                                }

                                Database.database.getInterestDB().addOrUpdateInterest(list);

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
                            callback.onResponse(ResponseStatus.FAILED);
                    }
                })
        {
            @Override
            protected Map<String,String> getParams()
            {
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

    public void sendInterests(final Context context, final ArrayList<InterestModel> models, final AppUserModel model, final iResponseCallback callback)
    {
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
                AppUserModel temp=new AppUserModel();
                temp.setInterests(models);

                Map<String,String> params = new HashMap<>();
                params.put("token",model.getToken());
                params.put("interests",temp.selectedInterestsToString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void deleteInterests(final Context context, final ArrayList<InterestModel> models, final AppUserModel model, final iResponseCallback callback)
    {
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
                        if(callback!=null)
                            callback.onResponse(ResponseStatus.FAILED);
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
                params.put("interests",temp.unSelectedInterestsToString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void fetchAllEvents(final Context context)
    {
        FetchResponseHelper.getInstance().incrementRequestCount();

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

                            if (code == 200)
                            {
                                ArrayList<EventModel> eventModels = new ArrayList<>();
                                ArrayList<CoordinatorModel> coordinatorModels = new ArrayList<>();

                                for (int i = 0; i < data.length(); i++)
                                {
                                    EventModel eventModel = new EventModel();
                                    JSONObject jEvent = data.getJSONObject(i);

                                    eventModel.setEventID(jEvent.getInt("Id"));
                                    eventModel.setEventName(jEvent.getString("Name"));
                                    eventModel.setDescription(jEvent.getString("Description"));
                                    eventModel.setVenue(jEvent.getString("Venue"));
                                    eventModel.setEventDate(EventModel.parseDate(jEvent.getString("Start")));
                                    eventModel.setEventEndDate(EventModel.parseDate(jEvent.getString("End")));
                                    eventModel.setCurrentRound(Integer.valueOf(jEvent.getString("CurrentRound")));
                                    eventModel.setMaxUsers(jEvent.getInt("MaxContestants"));
                                    //eventModel.setStatus(jEvent.getString("Status"));
                                    eventModel.setPdfLink(jEvent.getString("Pdf"));
                                    eventModel.setRules(jEvent.getString("Rules"));
                                    eventModel.setCategory(jEvent.getInt("CategoryId"));
                                    eventModel.setSociety(jEvent.getInt("SocietyId"));

                                    eventModels.add(eventModel);

                                    coordinators = jEvent.getJSONArray("Coordinators");

                                    for (int j = 0; j < coordinators.length(); j++)
                                    {
                                        JSONObject jCoordinator = coordinators.getJSONObject(j);
                                        CoordinatorModel coordinatorModel = new CoordinatorModel();
                                        coordinatorModel.setEventID(eventModel.getEventID());
                                        coordinatorModel.setName(jCoordinator.getString("Name"));
                                        coordinatorModel.setMobile(jCoordinator.getInt("PhoneNo") + "");
                                        //TODO:set email, designation too here
                                        coordinatorModels.add(coordinatorModel);
                                    }
                                }

                                Database.database.getEventsDB().addOrUpdateEvent(eventModels);
                                Database.database.getCoordinatorDB().addOrUpdateCoordinator(coordinatorModels);

                                FetchResponseHelper.getInstance().incrementResponseCount(null);
                                Log.v("DEBUG", data.toString());
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
                                EventModel eventModel=new EventModel();
                                eventModel.setEventID(data.getInt("Id"));
                                eventModel.setEventName(data.getString("Name"));
                                eventModel.setDescription(data.getString("Description"));
                                eventModel.setVenue(data.getString("Venue"));
                                eventModel.setEventDate(EventModel.parseDate(data.getString("Start")));
                                eventModel.setEventEndDate(EventModel.parseDate(data.getString("End")));
                                eventModel.setCurrentRound(Integer.valueOf(data.getString("CurrentRound")));
                                eventModel.setMaxUsers(data.getInt("MaxContestants"));
                                //TODO:FIX
                                //eventModel.setStatus(data.getString("Status"));
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
                                    callback.onResponse(ResponseStatus.SUCCESS,new EventModel());
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            if (callback!=null)
                                callback.onResponse(ResponseStatus.SUCCESS,new EventModel());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (callback!=null)
                            callback.onResponse(ResponseStatus.SUCCESS,new EventModel());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    //TODO: DEPRECIATE, Global Search Needed
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
                                    eventModel.setCurrentRound(Integer.valueOf(data.getJSONObject(i).getString("CurrentRound")));
                                    eventModel.setMaxUsers(data.getJSONObject(i).getInt("MaxContestants"));
                                    //TODO:Fix
                                    //eventModel.setStatus(data.getJSONObject(i).getString("Status"));
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
              "api/event/"+eventId+"/register" ,
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
                            callback.onResponse(ResponseStatus.FAILED);
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
}
