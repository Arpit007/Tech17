package com.nitkkr.gawds.tech17.api;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.fragment.MyTeams;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.database.DbConstants;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;
import com.nitkkr.gawds.tech17.model.AppUserModel;
import com.nitkkr.gawds.tech17.model.CoordinatorModel;
import com.nitkkr.gawds.tech17.model.EventKey;
import com.nitkkr.gawds.tech17.model.EventModel;
import com.nitkkr.gawds.tech17.model.EventStatus;
import com.nitkkr.gawds.tech17.model.ExhibitionModel;
import com.nitkkr.gawds.tech17.model.InterestModel;
import com.nitkkr.gawds.tech17.model.NotificationModel;
import com.nitkkr.gawds.tech17.model.SocietyModel;
import com.nitkkr.gawds.tech17.model.TeamModel;
import com.nitkkr.gawds.tech17.model.UserKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Dell on 17-Dec-16.
 */

public class FetchData
{
	private FetchData()
	{
	}

	private static FetchData f = new FetchData();

	public static FetchData getInstance()
	{
		return f;
	}

	public static StringRequest fetchUserInterests(final Context context, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.get_interests_list) + "?token=" + context.getSharedPreferences("User_Data", Context.MODE_PRIVATE).getString("Token",""),
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
								Database.getInstance().getInterestDB().resetTable();

								ArrayList<InterestModel> list = new ArrayList<>();
								for (int i = 0; i < data.length(); i++)
								{
									InterestModel interestModel = new InterestModel();
									interestModel.setID(data.getInt(i));
									interestModel.setInterest(Database.getInstance().getInterestDB().getInterest(interestModel));
									interestModel.setSelected(true);
									list.add(interestModel);
								}

								Database.getInstance().getInterestDB().addOrUpdateInterest(list);

								if(callback!=null)
									callback.onResponse(ResponseStatus.SUCCESS);

								Log.v("DEBUG", data.toString());
							}
							else
							{
								Log.d("Fetch:\t", "Failed Fetching User Interests");
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
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				});

		return stringRequest;
	}

	public static StringRequest getUserDetails(final Context context, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.get_user_details_url) + "?token=" + context.getSharedPreferences("User_Data", Context.MODE_PRIVATE).getString("Token",""),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{

						JSONObject response, data;

						int code;
						try
						{
							response = new JSONObject(res);
							data = response.getJSONObject("data");
							code = response.getJSONObject("status").getInt("code");

							if (code == 200)
							{
								AppUserModel appUserModel = (AppUserModel) AppUserModel.MAIN_USER.clone();
								appUserModel.setRoll(data.getInt("RollNo") + "");
								appUserModel.setMobile(data.getString("PhoneNumber"));
								appUserModel.setBranch(data.getString("Branch"));
								appUserModel.setYear(data.getString("Year"));
								appUserModel.setCollege(data.getString("College"));
								appUserModel.setGender(data.getString("Gender"));

								appUserModel.saveAppUser(context);
								AppUserModel.MAIN_USER = appUserModel;

								if(callback!=null)
									callback.onResponse(ResponseStatus.SUCCESS);
								Log.v("DEBUG", data.toString());
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
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				});

		return stringRequest;
	}

	public void updateUserDetails(final Context context, final AppUserModel main_user, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.get_user_details_url),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						Log.v("DEBUG",main_user.getName()+" "+main_user.getMobile()+" "+main_user.getRoll());
						JSONObject response;
						int code;
						Log.v("DEBUG",res);
						try
						{
							response = new JSONObject(res);
							code = response.getJSONObject("status").getInt("code");

							if (code == 200)
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String> params = new HashMap<>();
				params.put("token", AppUserModel.MAIN_USER.getToken());
				params.put("name", main_user.getName());
				params.put("rollNo", main_user.getRoll());
				params.put("phoneNumber", main_user.getMobile());
				params.put("branch", main_user.getBranch());
				params.put("college", main_user.getCollege());
				params.put("year", main_user.getYear());
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public void sendInterests(final Context context, final ArrayList<InterestModel> models, final AppUserModel model, final iResponseCallback callback)
	{
		AppUserModel temp = new AppUserModel();
		temp.setInterests(models);
		final String Query = temp.selectedInterestsToString();
		if (Query.equals(""))
		{
			if (callback != null)
			{
				callback.onResponse(ResponseStatus.SUCCESS);
			}
			return;
		}
		StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.get_user_interests_url),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response;
						Log.v("DEBUG",res);
						try
						{
							response = new JSONObject(res);

							if (response.getJSONObject("status").getInt("code") == 200)
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						Log.v("DEBUG", error.toString());
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams()
			{

				Map<String, String> params = new HashMap<>();
				params.put("token", model.getToken());
				params.put("interests", Query);
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public void deleteInterests(final Context context, final ArrayList<InterestModel> models, final AppUserModel model, final iResponseCallback callback)
	{
		AppUserModel temp = new AppUserModel();
		temp.setInterests(models);
		final String Query = temp.unSelectedInterestsToString();
		if (Query.equals(""))
		{
			if (callback != null)
			{
				callback.onResponse(ResponseStatus.SUCCESS);
			}
			return;
		}
		StringRequest stringRequest = new StringRequest(Request.Method.DELETE, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.get_user_interests_url),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response;
						try
						{
							response = new JSONObject(res);

							if (response.getJSONObject("status").getInt("code") == 200)
							{
								Log.v("DEBUG", response.toString());
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						Log.v("DEBUG", error.toString());
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams()
			{
				AppUserModel temp = new AppUserModel();
				temp.setInterests(models);

				Map<String, String> params = new HashMap<>();
				params.put("token", model.getToken());
				params.put("interests", Query);
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public void getEvent(final Context context, int eventId, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.getEvent) + eventId,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{

						JSONObject response;
						JSONObject data;
						int code;
						try
						{
							response = new JSONObject(res);
							data = response.getJSONObject("data");
							code = response.getJSONObject("status").getInt("code");

							if (code == 200)
							{
								EventModel eventModel = Database.getInstance().getEventsDB().getEvent(data.getInt("Id"));
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
								try
								{
									eventModel.setResult(data.getString("Result"));
								}
								catch (Exception e)
								{
									e.printStackTrace();
									eventModel.setResult("");
								}
								eventModel.setMaxUsers(data.getInt("MaxContestants"));
								eventModel.setStatus(EventStatus.Parse(data.getString("Status").toLowerCase()));
								eventModel.setPdfLink(data.getString("Pdf"));
								eventModel.setCategory(data.getInt("CategoryId"));
								eventModel.setSociety(data.getInt("SocietyId"));

								Log.v("DEBUG", data.toString());
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS, eventModel);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED, new EventModel());
								}
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED, new EventModel());
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							callback.onResponse(ResponseStatus.FAILED, new EventModel());
						}
					}
				});

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	//Unused
	public void searchFor(final Context context, String search_this, final ArrayList<EventModel> result)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.search_event) + "?query=" + search_this,
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

								for (int i = 0; i < data.length(); i++)
								{
									EventModel eventModel = new EventModel();

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
									eventModel.setSociety(data.getJSONObject(i).getInt("SocietyId"));
									result.add(eventModel);

								}
								Log.v("DEBUG", data.toString());
							}
							else
							{
								//internal error
							}

						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}

					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
					}
				});

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public void registerSingleEvent(final Context context, String eventId, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url) +
				"/api/event/" + eventId + "/register",
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
							code = response.getJSONObject("status").getInt("code");

							if (code == 200)
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}

						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String> params = new HashMap<>();
				params.put("token", AppUserModel.MAIN_USER.getToken());
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public void getGTalk(final Context context, final EventKey key, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) + context.getResources().getString(R.string.guestLectures)
				+ "/" + key.getEventID(),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response;
						JSONObject data;
						int code;
						try
						{
							response = new JSONObject(res);
							code = response.getJSONObject("status").getInt("code");
							data = response.getJSONObject("data");
							if (code == 200)
							{
								ExhibitionModel model = Database.getInstance().getExhibitionDB().getExhibition(key);

								model.setEventID(data.getInt("Id"));
								model.setEventName(data.getString("GuestName"));
								model.setEventDate(EventModel.parseDate(data.getString("Start")));
								model.setEventEndDate(EventModel.parseDate(data.getString("End")));
								model.setImage_URL(data.getString("Photo"));
								model.setDescription(data.getString("Description"));
								model.setAuthor(data.getString("GuestName"));
								model.setVenue(data.getString("Venue"));
								model.setGTalk(1);

								Database.getInstance().getExhibitionDB().addOrUpdateExhibition(model);
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}

							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				});

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public static StringRequest fetchUserWishlist(final Context context, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) + context.getResources().getString(R.string.userWishlist)
				+ "?token=" + context.getSharedPreferences("User_Data", Context.MODE_PRIVATE).getString("Token",""),
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
							code = response.getJSONObject("status").getInt("code");
							if (code == 200)
							{
								Database.getInstance().getExhibitionDB().resetTable();
								data = response.getJSONArray("data");
								for (int i = 0; i < data.length(); i++)
								{
									ExhibitionModel key = Database.getInstance().getExhibitionDB().getExhibition(data.getInt(i));
									key.setNotify(true);
									Database.getInstance().getExhibitionDB().addOrUpdateExhibition(key);
								}
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
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				});
		return stringRequest;
	}

	public void addToWishlist(final Context context, final EventKey key, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url) + context.getResources().getString(R.string.userWishlist)
				+ "/" + key.getEventID(),
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
							code = response.getJSONObject("status").getInt("code");
							if (code == 200)
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}

						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<>();
				params.put("token", AppUserModel.MAIN_USER.getToken());
				params.put("lectureId", String.valueOf(key.getEventID()));
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public void removeFromWishlist(final Context context, final EventKey key, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.DELETE, context.getResources().getString(R.string.server_url) + context.getResources().getString(R.string.userWishlist)
				+ "/" + key.getEventID() + "?token=" + AppUserModel.MAIN_USER.getToken(),
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
							code = response.getJSONObject("status").getInt("code");
							if (code == 200)
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();

						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				});

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public static StringRequest getSocieties(final Context context, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) + context.getResources().getString(R.string.getSocieties),
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
							code = response.getJSONObject("status").getInt("code");
							data = response.getJSONArray("data");
							if (code == 200)
							{
								ArrayList<SocietyModel> models = new ArrayList<>();
								for (int i = 0; i < data.length(); i++)
								{
									JSONObject object = data.getJSONObject(i);
									SocietyModel model = new SocietyModel();
									model.setName(object.getString("Name"));
									model.setID(object.getInt("Id"));
									model.setDescription(object.getString("Description"));
									models.add(model);
								}
								Database.getInstance().getSocietyDB().addOrUpdateSocities(models);
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
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				});

		return stringRequest;
	}

	public static StringRequest getNotifications(final Context context, Date date, final iResponseCallback callback)
	{
		String TimeStamp = new SimpleDateFormat("yyyy-MM-dd+hh:mm:ss", Locale.getDefault()).format(date);

		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.GetNotification) + "?token=" + context.getSharedPreferences("User_Data", Context.MODE_PRIVATE).getString("Token","")+"&timeStamp="+TimeStamp,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response;
						JSONArray data,privateArr,globalArr;
						int code;
						try
						{
							Log.v("DEBUG",res.toString());
							response = new JSONObject(res);
							code = response.getJSONObject("status").getInt("code");
							data = response.getJSONArray("data");
							if (code == 200)
							{
								ArrayList<NotificationModel> models = Database.getInstance().getNotificationDB().getAllNotifications();

								JSONObject object = data.getJSONObject(0);
								JSONObject object2 = data.getJSONObject(1);

								privateArr = object.getJSONArray("private");
								globalArr = object2.getJSONArray("global");


								//private notification
								for (int j = 0; j < privateArr.length(); j++)
								{
									NotificationModel model = new NotificationModel();
									JSONObject pvtObject=privateArr.getJSONObject(j);
									JSONObject NotificationObject = pvtObject.getJSONObject("Notification");

									model.setNotificationID(pvtObject.getInt("Id"));
									model.setEventID(NotificationObject.getInt("EventId"));
									model.setMessage(NotificationObject.getString("Message"));
									model.setSeen(pvtObject.getInt("status") == 0);
									models.add(model);
								}

								for (int j = 0; j < globalArr.length(); j++)
								{
									NotificationModel model = new NotificationModel();
									JSONObject glblObject=globalArr.getJSONObject(j);

									model.setNotificationID(glblObject.getInt("Id"));
									model.setEventID(glblObject.getInt("EventId"));
									model.setMessage(glblObject.getString("Message"));
									model.setSeen(false);

									models.add(model);
								}

								Database.getInstance().getNotificationDB().addOrUpdateNotification(models);
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}
						}
						catch (JSONException e)
						{
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				});
		return stringRequest;
	}

	public void changeNotificationStatus(final Context context, final int NotificationId, final int status, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url) +
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
							code = response.getJSONObject("status").getInt("code");
							if (code == 200)
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}

						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<>();
				params.put("token", AppUserModel.MAIN_USER.getToken());
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
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) +
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
								int Size = eventModels.size();

								for (int i = 0; i < data.length(); i++)
								{
									EventModel eventModel = new EventModel();
									JSONObject jEvent = data.getJSONObject(i);

									int ID = jEvent.getInt("Id"), index = -1;

									for (int x = 0; x < Size; x++)
									{
										if (eventModels.get(x).getEventID() == ID)
										{
											eventModel = eventModels.get(x);
											index = x;
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

									if (index == -1)
									{
										eventModels.add(eventModel);
									}
								}

								Database.getInstance().getEventsDB().addOrUpdateEvent(eventModels);
								Log.v("DEBUG", data.toString());
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS, eventModels);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED, null);
								}
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED, null);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							callback.onResponse(ResponseStatus.FAILED, null);
						}
					}
				});

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public void fetchInterestedEvents(final Context context, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.interestedEvents) + "?token=" + AppUserModel.MAIN_USER.getToken(),
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
								ArrayList<EventKey> keys = new ArrayList<>();
								for (int i = 0; i < data.length(); i++)
								{
									EventKey key = new EventKey();
									JSONObject object = data.getJSONObject(i);
									key.setEventName(object.getString("Name"));
									key.setEventID(object.getInt("Id"));
									//object.getInt("CategoryId");
									keys.add(key);
								}
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS, keys);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED, null);
								}
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED, null);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							callback.onResponse(ResponseStatus.FAILED, null);
						}
					}
				});

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public static StringRequest fetchInterests(final Context context, final iResponseCallback callback)
	{
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
							JSONObject object = data.getJSONObject(i);
							InterestModel interestModel = Database.getInstance().getInterestDB().getInterestModel(object.getInt("Id"));
							interestModel.setID(object.getInt("Id"));
							interestModel.setInterest(object.getString("Name"));
							list.add(interestModel);
						}
						Database.getInstance().getInterestDB().deleteTable();
						Database.getInstance().getInterestDB().onCreate(Database.getInstance().getDatabase());
						Database.getInstance().getInterestDB().addOrUpdateInterest(list);
						Log.v("DEBUG", data.toString());
						if(callback!=null)
							callback.onResponse(ResponseStatus.SUCCESS);
					}
					else
					{
						Log.d("Fetch:\t", "Failed fetching All Interests");
						if(callback!=null)
							callback.onResponse(ResponseStatus.FAILED);
					}
				}
				catch (JSONException e)
				{
					Log.d("Fetch:\t", "Failed Fetching Interests");
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
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				});

		return stringRequest;
	}

	public static StringRequest fetchData(final Context context, final iResponseCallback callback)
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

							Log.v("DEBUG", "FULL EVENT DATA" + response.toString());

							if (code == 200)
							{
								ArrayList<EventModel> eventModels = Database.getInstance().getEventsDB().getEvents("");
								ArrayList<EventModel> finalEvents = new ArrayList<>();

								ArrayList<ExhibitionModel> exhibitionModels = Database.getInstance().getExhibitionDB().getExhibitions(DbConstants.ExhibitionNames.GTalk.Name() + " != 1");
								ArrayList<ExhibitionModel> finalExhibition = new ArrayList<>();

								ArrayList<CoordinatorModel> coordinatorModels = new ArrayList<>();

								int evSize = eventModels.size(), exSize = exhibitionModels.size();
								int ID;
								boolean isInformal;

								for (int i = 0; i < data.length(); i++)
								{
									JSONObject jEvent = data.getJSONObject(i);
									String Category = Database.getInstance().getInterestDB().getInterest(jEvent.getInt("CategoryId")).toLowerCase();

									if (Category.equals("exhibitions"))
									{
										ExhibitionModel model = new ExhibitionModel();
										JSONObject object = data.getJSONObject(i);

										ID = jEvent.getInt("Id");

										for (int x = 0; x < exSize; x++)
										{
											if (exhibitionModels.get(x).getEventID() == ID)
											{
												model = exhibitionModels.get(x);
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
										model.setGTalk(0);
										model.setSociety(-1);

										finalExhibition.add(model);

									}
									else if (Category.equals("workshops"))
									{
										ExhibitionModel model = new ExhibitionModel();
										JSONObject object = data.getJSONObject(i);

										ID = jEvent.getInt("Id");

										for (int x = 0; x < exSize; x++)
										{
											if (exhibitionModels.get(x).getEventID() == ID)
											{
												model = exhibitionModels.get(x);
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
										model.setGTalk(-1);

										finalExhibition.add(model);
									}
									else
									{
										Category = Database.getInstance().getSocietyDB().getSocietyName(jEvent.getInt("SocietyId")).toLowerCase();
										String Temp1 = Database.getInstance().getInterestDB().getInterest(jEvent.getInt("CategoryId")).toLowerCase();
										isInformal = ( Category.equals("informals") || Category.equals("informalz") || Temp1.equals("informals") || Temp1.equals("informalz") );

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

								if(callback!=null)
									callback.onResponse(ResponseStatus.SUCCESS);
								fetchAllGTalks(context, exhibitionModels, finalExhibition,Database.getInstance(),callback);

								Database.getInstance().getEventsDB().deleteTable();
								Database.getInstance().getEventsDB().onCreate(Database.getInstance().getDatabase());
								Database.getInstance().getEventsDB().addOrUpdateEvent(finalEvents);

								Database.getInstance().getCoordinatorDB().deleteTable();
								Database.getInstance().getCoordinatorDB().onCreate(Database.getInstance().getDatabase());
								Database.getInstance().getCoordinatorDB().addOrUpdateCoordinator(coordinatorModels);
							}
							else
							{
								if(callback!=null)
								{
									callback.onResponse(ResponseStatus.FAILED);
									callback.onResponse(ResponseStatus.FAILED);
								}
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if(callback!=null)
							{
								callback.onResponse(ResponseStatus.FAILED);
								callback.onResponse(ResponseStatus.FAILED);
							}
						}

					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				});

		return stringRequest;
	}

	private static void fetchAllGTalks(final Context context, final ArrayList<ExhibitionModel> initialList, final ArrayList<ExhibitionModel> finalList, final Database database, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) + context.getResources().getString(R.string.guestLectures),
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
							code = response.getJSONObject("status").getInt("code");
							data = response.getJSONArray("data");
							if (code == 200)
							{
								int Size = initialList.size();
								for (int i = 0; i < data.length(); i++)
								{
									ExhibitionModel model = new ExhibitionModel();
									JSONObject object = data.getJSONObject(i);

									int ID = object.getInt("Id");

									for (int x = 0; x < Size; x++)
									{
										if (initialList.get(x).getEventID() == ID)
										{
											model = initialList.get(x);
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
									model.setGTalk(1);
									finalList.add(model);

								}
								Log.v("DEBUG", "GUSTO TALK" + data.toString());

								database.getExhibitionDB().deleteTable();
								database.getExhibitionDB().onCreate(database.getDatabase());
								database.getExhibitionDB().addOrUpdateExhibition(finalList);

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
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				});

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public void createTeam(final Context context, final String teamName, final int EventId, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.createTeam),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response,data;
						int code,teamId;
						try
						{
							response = new JSONObject(res);
							code = response.getJSONObject("status").getInt("code");
							data=response.getJSONObject("data");

							if (code == 200)
							{
								teamId=data.getInt("Id");
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS, teamId);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.OTHER, 0);
								}
							}

						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED, 0);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE, 0);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED, 0);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<>();
				params.put("token", AppUserModel.MAIN_USER.getToken());
				params.put("teamName", teamName);
				params.put("eventId",String.valueOf(EventId));
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	//Unused
	public void deleteTeam(final Context context, final String teamId, final int status, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.DELETE, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.deleteTeam)+teamId,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response;
						String data;
						int code;
						try
						{
							response = new JSONObject(res);
							code = response.getJSONObject("status").getInt("code");
							data=response.getString("data");
							//Status 0 for read and 1 for unread
							if (code == 200)
							{
								if (callback != null)
								{
									//example
									//"data": "Team deleted"
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}

						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<>();
				params.put("token", AppUserModel.MAIN_USER.getToken());
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public static StringRequest getMyTeams(final Context context, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.createTeam)+"?token="+context.getSharedPreferences("User_Data", Context.MODE_PRIVATE).getString("Token",""),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response, data;
						JSONArray participantIn,teamLeaderOf,pendingInvitations;

						int code;
						try
						{
							Log.v("DEBUG",res.toString());
							response = new JSONObject(res);
							code = response.getJSONObject("status").getInt("code");
							data=response.getJSONObject("data");

							if (code == 200)
							{
								ArrayList<TeamModel> myTeams=new ArrayList<>();
								ArrayList<TeamModel> teams=new ArrayList<>();
								ArrayList<TeamModel> invites=new ArrayList<>();

								if (data != null)
								{
									participantIn = data.getJSONArray("participantIn");
									teamLeaderOf = data.getJSONArray("teamLeaderOf");
									pendingInvitations = data.getJSONArray("pendingInvitations");


									for (int i = 0; i < participantIn.length(); i++)
									{
										TeamModel key=new TeamModel();
										JSONObject object=participantIn.getJSONObject(i);
										key.setTeamID(object.getInt("Id"));
										key.setEventID(object.getInt("EventId"));
										key.setTeamName(object.getString("Name"));
										key.setControl(TeamModel.TeamControl.Participant);
										teams.add(key);
									}

									for (int i = 0; i < teamLeaderOf.length(); i++)
									{
										TeamModel key=new TeamModel();
										JSONObject object=teamLeaderOf.getJSONObject(i);
										key.setTeamID(object.getInt("Id"));
										key.setEventID(object.getInt("EventId"));
										key.setTeamName(object.getString("Name"));
										key.setControl(TeamModel.TeamControl.Leader);
										myTeams.add(key);
									}

									for (int i = 0; i < pendingInvitations.length(); i++)
									{
										TeamModel key=new TeamModel();
										JSONObject object=pendingInvitations.getJSONObject(i);
										key.setTeamID(object.getInt("Id"));
										key.setEventID(object.getInt("EventId"));
										key.setTeamName(object.getString("Name"));
										key.setControl(TeamModel.TeamControl.Pending);
										invites.add(key);
									}
									Database.getInstance().getTeamDB().resetTable();
									Database.getInstance().getTeamDB().addOrUpdateTeamInvite(invites);
									Database.getInstance().getTeamDB().addOrUpdateMyTeam(teams);
									Database.getInstance().getTeamDB().addOrUpdateMyTeam(myTeams);


									RequestQueue teamDetailQueue = Volley.newRequestQueue(context);
									teamDetailQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>()
									{
										@Override
										public void onRequestFinished(Request<Object> request)
										{
											if(callback!=null)
												callback.onResponse(ResponseStatus.SUCCESS);
										}
									});

									for(TeamModel model: teams)
									{
										EventModel eventModel=Database.getInstance().getEventsDB().getEvent(model.getEventID());
										eventModel.setRegistered(true);
										eventModel.setNotify(true);
										Database.getInstance().getEventsDB().addOrUpdateEvent(eventModel);
									}

									for(TeamModel model: myTeams)
									{
										EventModel eventModel=Database.getInstance().getEventsDB().getEvent(model.getEventID());
										eventModel.setRegistered(true);
										eventModel.setNotify(true);
										Database.getInstance().getEventsDB().addOrUpdateEvent(eventModel);
									}

									for(TeamModel model : invites)
										teamDetailQueue.add(getTeamDetail(context, model.getTeamID(),true,false,null));

									for(TeamModel model : teams)
										teamDetailQueue.add(getTeamDetail(context, model.getTeamID(),false,false,null));

									for(TeamModel model : myTeams)
										teamDetailQueue.add(getTeamDetail(context, model.getTeamID(),false,true,null));

									if(invites.size() == 0 && teams.size() == 0 && myTeams.size() == 0)
										if(callback!=null)
											callback.onResponse(ResponseStatus.SUCCESS);

								}

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
							{
								callback.onResponse(ResponseStatus.FAILED);
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				});

		return stringRequest;
	}

	public void searchUsers(final Context context,  final String query, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.server_url)
				+ "/api/user/search?token="+AppUserModel.MAIN_USER.getToken()+"&query="+query.replaceAll(" ","%20"),
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
							code = response.getJSONObject("status").getInt("code");
							data = response.getJSONArray("data");
							if (code == 200)
							{
								ArrayList<UserKey> keys=new ArrayList<>();
								for(int i=0;i<data.length();i++)
								{
									UserKey key = new UserKey();
									JSONObject object=data.getJSONObject(i);
									key.setName(object.getString("Name"));
									key.setUserID(object.getString("Id"));
									key.setTeamControl(TeamModel.TeamControl.Pending);
									key.setRoll(object.getString("RollNo"));
									if(key.getRoll().equals(AppUserModel.MAIN_USER.getRoll()))
										continue;
									keys.add(key);
								}
								if (callback!=null)
									callback.onResponse(ResponseStatus.SUCCESS, keys);
							}
							else
							{
								if (callback!=null)
									callback.onResponse(ResponseStatus.FAILED, null);
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback!=null)
								callback.onResponse(ResponseStatus.FAILED, null);
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE, null);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED, null);
							}
						}
					}
				});

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);

	}

	public void sendInvite(final Context context, final int teamId, final String invites, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.deleteTeam)+teamId+context.getResources().getString(R.string.sendInvite),
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
							code = response.getJSONObject("status").getInt("code");
							if (code == 200)
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}

						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<>();
				params.put("token", AppUserModel.MAIN_USER.getToken());
				params.put("inviteType","new");
				params.put("invites",invites);
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public void acceptTeamInvite(final Context context, final int teamId, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.deleteTeam)+teamId+context.getResources().getString(R.string.sendInvite),
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response;
						int code;

						try
						{
							Log.v("DEBUG",res.toString());
							response = new JSONObject(res);
							code = response.getJSONObject("status").getInt("code");

								if (code == 200)
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}

						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<>();
				params.put("token", AppUserModel.MAIN_USER.getToken());
				params.put("inviteType", "accept");
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public void declineTeamInvite(final Context context, final int teamId, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.deleteTeam)+teamId+context.getResources().getString(R.string.sendInvite),
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
							code = response.getJSONObject("status").getInt("code");

							if (code == 200)
							{

								Log.v("DEBUG",res.toString());
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}

						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<>();
				params.put("token", AppUserModel.MAIN_USER.getToken());
				params.put("inviteType", "decline");
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(context);
		requestQueue.add(stringRequest);
	}

	public static StringRequest getTeamDetail(final Context context, final int teamId, final boolean isInvite, final boolean isLeader, final iResponseCallback callback)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.server_url) +
				context.getResources().getString(R.string.deleteTeam)+teamId,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String res)
					{
						JSONObject response;
						JSONObject data;
						int code;

						try
						{
							response = new JSONObject(res);
							code = response.getJSONObject("status").getInt("code");

							if (code == 200)
							{
								TeamModel model;

								if(isInvite)
									model = Database.getInstance().getTeamDB().getInviteTeam(teamId);
								else model = Database.getInstance().getTeamDB().getMyTeam(teamId);

								ArrayList<UserKey> users=new ArrayList<>();

								data=response.getJSONObject("data");
								model.setTeamName(data.getString("Name"));
								model.setEventID(data.getInt("EventId"));
								model.setTeamID(teamId);

								JSONArray members=data.getJSONArray("TeamInvites");

								for(int i=0;i<members.length();i++)
								{
									JSONObject object=members.getJSONObject(i);
									UserKey key = new UserKey();
									key.setUserID(String.valueOf(object.getJSONObject("Student").getInt("Id")));
									key.setName(object.getJSONObject("Student").getString("Name"));
									key.setTeamControl(TeamModel.TeamControl.Parse(object.getString("Status")));
									users.add(key);
								}

								JSONObject Leader=data.getJSONObject("TeamLeader");
								UserKey leader=new UserKey();
								leader.setName(Leader.getString("Name"));
								leader.setTeamControl(TeamModel.TeamControl.Leader);
								users.add(0,leader);

								if(isLeader)
									model.setControl(TeamModel.TeamControl.Leader);



								model.setMembers(users);

								if(isInvite)
									Database.getInstance().getTeamDB().addOrUpdateTeamInvite(model);
								else Database.getInstance().getTeamDB().addOrUpdateMyTeam(model);

								Log.v("DEBUG",res.toString());
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.SUCCESS);
								}
							}
							else
							{
								if (callback != null)
								{
									callback.onResponse(ResponseStatus.FAILED);
								}
							}

						}
						catch (JSONException e)
						{
							e.printStackTrace();
							if (callback != null)
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						error.printStackTrace();
						if (callback != null)
						{
							if (error instanceof TimeoutError || error instanceof NetworkError)
							{
								callback.onResponse(ResponseStatus.NONE);
							}
							else
							{
								callback.onResponse(ResponseStatus.FAILED);
							}
						}
					}
				})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<>();
				params.put("token", AppUserModel.MAIN_USER.getToken());
				return params;
			}
		};

		return stringRequest;
	}

}


