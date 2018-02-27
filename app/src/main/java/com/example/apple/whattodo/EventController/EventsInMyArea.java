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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.apple.whattodo.MainActivity;
import com.example.apple.whattodo.R;
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

    // Movies json url
    private static final String url = "https://www.eventbriteapi.com/v3/events/search/?location.latitude=53.3498&location.longitude=6.2603&token=IULJK3QH2256C6ARBMQR";
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
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                // Logic to handle location object

                          longitude= location.getLongitude();
                          latitude = location.getLatitude();
                          latitude1 = String.valueOf(location.getLatitude());
                          longitude1= String.valueOf(location.getLongitude());


                        }
                    }
                });




        // Creating volley request obj
        JsonObjectRequest eventReq = new JsonObjectRequest(Request.Method.GET, url, null,       //this is where the application is currently crashing
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        try {
                            JSONArray jsonArray = response.getJSONArray("events");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject employee = jsonArray.getJSONObject(i); //something goes wrong here, i think its possibly with the size of the reading
                                EventModel eventModel = new EventModel();
                                eventModel.setTitle(employee.getJSONObject("name").getString("text"));
                                // eventModel.setLocation(employee.getJSONObject("description").getString("text"));
                                eventModel.setDate(employee.getJSONObject("start").getString("timezone"));
                                eventModel.setTime(employee.getJSONObject("start").getString("local"));
                                eventModel.setThumbnailUrl(employee.getJSONObject("logo").getString("url"));

                                // adding movie to movies array
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





}
