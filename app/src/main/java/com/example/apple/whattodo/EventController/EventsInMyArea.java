package com.example.apple.whattodo.EventController;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.apple.whattodo.R;
import com.example.apple.whattodo.UserPreferanceCalculator.SwipeActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventsInMyArea extends AppCompatActivity {

    // Log tag
    private static final String TAG = EventsInMyArea.class.getSimpleName();

    // Events json url
    public String myUrl;
    private ProgressDialog pDialog;
    private List<EventModel> eventModelList = new ArrayList<EventModel>();
    private ListView listView;
    private CustomListAdapter adapter;
    protected LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    double longitude;
    double latitude;
    String longitude1;
    String latitude1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_feed);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, eventModelList);
        listView.setAdapter((ListAdapter) adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            latitude1 = String.valueOf(location.getLatitude());
                            longitude1 = String.valueOf(location.getLongitude());
                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("https")
                                    .authority("www.eventbriteapi.com")
                                    .appendPath("v3")
                                    .appendPath("events")
                                    .appendPath("search")
                                    .appendQueryParameter("location.latitude", latitude1)
                                    .appendQueryParameter("location.longitude", longitude1)
                                    .appendQueryParameter("token", "IULJK3QH2256C6ARBMQR");
                            myUrl = builder.build().toString();
                            PullFromEventBright();
                        }
                    }
                });


        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(EventsInMyArea.this,EventIndexActivity.class);
                i.putExtra("ValueKey", eventModelList.get(position).getTitle());
                i.putExtra("ValueKey2", eventModelList.get(position).getTime());
                i.putExtra("ValueKey3", eventModelList.get(position).getLocation());
                i.putExtra("ValueKey4", eventModelList.get(position).getThumbnailUrl());
                i.putExtra("ValueKey5", eventModelList.get(position).getUrl());

                startActivity(i);

            }
        });



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



    public void URLBuilder(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.eventbriteapi.com")
                .appendPath("v3")
                .appendPath("events")
                .appendPath("search")
                .appendQueryParameter("location.latitude", latitude1)
                .appendQueryParameter("location.longitude", longitude1)
                .appendQueryParameter("token", "IULJK3QH2256C6ARBMQR");
        myUrl = builder.build().toString();
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


    private void PullFromEventBright(){

        // Creating volley request obj //my current issue is that this won't run
        JsonObjectRequest eventReq = new JsonObjectRequest(Request.Method.GET, myUrl,null, new Response.Listener<JSONObject>() {
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
                        eventModel.setUrl(event.getString("url"));
                        // eventModel.setLocation(employee.getJSONObject("description").getString("text"));
                       // eventModel.setDate(event.getJSONObject("start").getString("timezone"));
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




}
