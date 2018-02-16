package com.example.apple.whattodo.EventController;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.apple.whattodo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EventFeed extends Activity {
    // Log tag
    private static final String TAG = EventFeed.class.getSimpleName();

    // Movies json url
    private static final String url = "https://www.eventbriteapi.com/v3/events/search/?location.address=Dublin&token=IULJK3QH2256C6ARBMQR";
    private ProgressDialog pDialog;
    private List<EventModel> eventModelList = new ArrayList<EventModel>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_feed);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, eventModelList);
        listView.setAdapter((ListAdapter) adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


        // Creating volley request obj
        JsonObjectRequest eventReq =
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,null,       //this is where the application is currently crashing
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                hidePDialog();

                                try {
                                    JSONArray jsonArray = response.getJSONArray("events");

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject event = jsonArray.getJSONObject(i); //something goes wrong here, i think its possibly with the size of the reading
                                        EventModel eventModel = new EventModel();
                                        eventModel.setTitle(event.getJSONObject("name").getString("text"));
                                        // eventModel.setLocation(employee.getJSONObject("description").getString("text"));
                                        eventModel.setDate(event.getJSONObject("start").getString("timezone"));
                                        eventModel.setTime(event.getJSONObject("start").getString("local"));
                                        eventModel.setThumbnailUrl(event.getJSONObject("logo").getString("url"));

                                        // adding event to events array
                                        eventModelList.add(eventModel);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();

                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(eventReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
