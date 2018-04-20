package com.example.apple.whattodo.ChatApplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.whattodo.EventController.EventFeed;
import com.example.apple.whattodo.EventController.EventsInMyArea;
import com.example.apple.whattodo.MainActivity;
import com.example.apple.whattodo.R;
import com.example.apple.whattodo.UserPreferanceCalculator.SwipeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatMain extends AppCompatActivity {

    private Button addChatRoom;
    private EditText eventChatRoom;
    private FirebaseAuth auth;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Chats");
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_events = new ArrayList<>();
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        Intent intent = getIntent();
        final String eventtitle = intent.getExtras().getString("ValueKey");
        addChatRoom =(Button) findViewById(R.id.addRoomButton);
        eventChatRoom= (EditText) findViewById(R.id.eventRoomChat);
        listView = (ListView) findViewById(R.id.chatRoomList);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_events);

        requeserUserName();




        listView.setAdapter(arrayAdapter);





        addChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String ,Object>();
                map.put(eventChatRoom.getText().toString(),"");//Get name of chat room
                root.updateChildren(map);


            }
        });


        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Make multiple rooms
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();


                while (i.hasNext()){

                    set.add(((DataSnapshot)i.next()).getKey());
                }

                list_of_events.clear();
                list_of_events.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),EventChatRoom.class);
                intent.putExtra("EventRoomName",((TextView)view).getText().toString());
                intent.putExtra("UserName", name);
                startActivity(intent);
            }
        });






    }

    private void requeserUserName() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(("Please Enter A Nick Name"));
        final EditText input_field = new EditText(this);
        builder.setView(input_field);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = input_field.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                requeserUserName();
            }
        });

        builder.show();






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
