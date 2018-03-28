package com.example.apple.whattodo.EventController;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.apple.whattodo.MainActivity;
import com.example.apple.whattodo.R;
import com.example.apple.whattodo.UserPreferanceCalculator.Profile;
import com.example.apple.whattodo.UserPreferanceCalculator.SwipeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    public String myUrl;
    private FirebaseAuth auth;
    private CustomListAdapter adapter;
    private Profile eventId;
    private String time, title;
    public static String preferanceId = new String();

    public static void setPreferanceId(String preferanceId) {
        EventFeed.preferanceId = preferanceId;
    }

    public static String getPreferanceId() {
        return preferanceId;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_feed);
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, eventModelList);
        listView.setAdapter((ListAdapter) adapter);
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading Your Preferences...");
        pDialog.show();
        firstMethod();
//        secondMethod();
    }

    public void firstMethod() {

        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Preference Logs " + FirebaseAuth.getInstance().getUid());


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> ids = new ArrayList<>();
                StringBuilder stringBuilder = new StringBuilder();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ids.add(dataSnapshot1.getValue(String.class));
                }
                for (int i = 0; i < ids.size(); i++) {
                    if (i != 0) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(ids.get(i));
                }
                String aa = stringBuilder.toString();
                secondMethod(aa);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String error = databaseError.getMessage();
            }
        });


    }


    public void secondMethod(String ids) {


        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.eventbriteapi.com")
                .appendPath("v3")
                .appendPath("events")
                .appendPath("search")
                .appendQueryParameter("categories", ids)//need to call the id variable from here )
                .appendQueryParameter("token", "IULJK3QH2256C6ARBMQR");
        myUrl = builder.build().toString();
        myUrl = myUrl.replaceAll(",200", "");

        JsonObjectRequest eventReq =
                new JsonObjectRequest(
                        Request.Method.GET,
                        myUrl, null,
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
                                       // eventModel.setDescription(event.getJSONObject("description").getString("text"));
                                        eventModel.setUrl(event.getString("url"));
                                        eventModel.setTime(event.getJSONObject("start").getString("local"));
                                        eventModel.setThumbnailUrl(event.getJSONObject("logo").getString("url"));

                                        // adding event to events array
                                        eventModelList.add(eventModel);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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

                Intent i = new Intent(EventFeed.this, EventIndexActivity.class);
                i.putExtra("ValueKey", eventModelList.get(position).getTitle());
                i.putExtra("ValueKey2", eventModelList.get(position).getTime());
                i.putExtra("ValueKey3", eventModelList.get(position).getLocation());
                i.putExtra("ValueKey4", eventModelList.get(position).getThumbnailUrl());
                i.putExtra("ValueKey5", eventModelList.get(position).getUrl());
                i.putExtra("ValueKey6", eventModelList.get(position).getDescription());

                startActivity(i);
                time = eventModelList.get(position).getTime();
                title = eventModelList.get(position).getTitle();

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


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

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

            case R.id.userMenu:
                Intent userIntent = new Intent(this, MainActivity.class);
                this.startActivity(userIntent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


