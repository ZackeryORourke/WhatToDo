package com.example.apple.whattodo.EventController;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.apple.whattodo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersUpcomingEvents extends AppCompatActivity {

    DatabaseReference databaseReference;
    private List<EventModel> eventItems = new ArrayList<>();
    DatabaseReference upcomingEvents;
    private ListView listView;
    private CustomListAdapter customListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_upcoming_events);
        listView = findViewById(R.id.upcomingEventList);
        customListAdapter = new CustomListAdapter(this, eventItems);
        listView.setAdapter(customListAdapter);
        usersEventsFeed();

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(UsersUpcomingEvents.this, EventIndexActivity.class);
                i.putExtra("ValueKey", eventItems.get(position).getTitle());
                i.putExtra("ValueKey2", eventItems.get(position).getTime());
                i.putExtra("ValueKey3", eventItems.get(position).getLocation());
                i.putExtra("ValueKey4", eventItems.get(position).getThumbnailUrl());
                i.putExtra("ValueKey5", eventItems.get(position).getUrl());
                i.putExtra("ValueKey6", eventItems.get(position).getDescription());

                startActivity(i);
                

            }
        });



    }

    public void usersEventsFeed() {


        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        upcomingEvents = FirebaseDatabase.getInstance().getReference("usersEvents").child(user);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot events : dataSnapshot.getChildren()) {
                    EventModel eventModel = events.getValue(EventModel.class);

                    //Setting the list of items to the adapter
                    eventItems.add(eventModel);
                    listView.setAdapter(customListAdapter);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        upcomingEvents.addListenerForSingleValueEvent(valueEventListener);
    }


}
