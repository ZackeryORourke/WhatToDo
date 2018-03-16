package com.example.apple.whattodo.EventController;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.apple.whattodo.AccountActivity.LoginActivity;
import com.example.apple.whattodo.AccountActivity.RegisterActivity;
import com.example.apple.whattodo.R;
import com.example.apple.whattodo.UserPreferanceCalculator.EventCard;
import com.example.apple.whattodo.UserPreferanceCalculator.Profile;
import com.example.apple.whattodo.UserPreferanceCalculator.SwipeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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
    public String myUrl;
    private CustomListAdapter adapter;
    private Profile eventId;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("UserPreferance");

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
        // This will get my user preferances


        List<String> userPreferances = EventCard.getPreferanceId();

        String preferenceId = String.valueOf(EventCard.getPreferanceId());
        //deletes the first two and the end two characters
        preferenceId = preferenceId.substring(1, preferenceId.length() - 1);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.eventbriteapi.com")
                .appendPath("v3")
                .appendPath("events")
                .appendPath("search")
                .appendQueryParameter("categories", preferenceId)//need to call the id variable from here )
                .appendQueryParameter("token", "IULJK3QH2256C6ARBMQR");
        myUrl = builder.build().toString();
        myUrl = myUrl.replaceAll("%20","");
        myUrl = myUrl.replaceAll("%2C%20",",");
        myUrl = myUrl.replaceAll("%2C","");

        JsonObjectRequest eventReq =
                new JsonObjectRequest(
                        Request.Method.GET,
                        myUrl,null,       //this is where the application is currently crashing
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
                                        //eventModel.setDate(event.getJSONObject("start").getString("timezone"));
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


        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(EventFeed.this,EventIndexActivity.class);
                i.putExtra("ValueKey", eventModelList.get(position).getTitle());
                i.putExtra("ValueKey2", eventModelList.get(position).getTime());
                i.putExtra("ValueKey3", eventModelList.get(position).getLocation());
                i.putExtra("ValueKey4", eventModelList.get(position).getThumbnailUrl());

                startActivity(i);

            }
        });



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
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.feed:
                Intent feedIntent = new Intent(this, EventFeed.class);
                this.startActivity(feedIntent);
                return true;




            case R.id.locationFeed:
                Intent areaIntent = new Intent(this, EventsInMyArea.class);
                this.startActivity(areaIntent);
                return true;


            case R.id.swipeView:
                Intent swipeIntent = new Intent(this, SwipeActivity.class);
                this.startActivity(swipeIntent);
                return true;




            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
