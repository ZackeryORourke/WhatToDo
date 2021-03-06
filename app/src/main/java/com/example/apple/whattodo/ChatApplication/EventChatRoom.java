package com.example.apple.whattodo.ChatApplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apple.whattodo.EventController.UsersUpcomingEvents;
import com.example.apple.whattodo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventChatRoom extends AppCompatActivity {


    private Button sendMessage;
    private EditText chatEditText;
    private EditText chatConversation;
    private String userName, roomName;
    private String temp_key;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Chats");
    private String chat_msg, chat_user_name;
    private NotificationCompat.Builder notification;
    private FirebaseAuth auth;
    private static final int uniqueId= 123456;

    @Override
    protected void onCreate(@Nullable Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.event_chat);
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        sendMessage = (Button) findViewById(R.id.sendButton);
        chatEditText = (EditText) findViewById(R.id.editTextMessage);
        chatConversation = (EditText) findViewById(R.id.chatConversation);

        // Read from the database
        // Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Username" + FirebaseAuth.getInstance().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
               userName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        Intent intent = getIntent();
        roomName = intent.getExtras().getString("ValueKey");
        setTitle("Event Room  " + roomName);

        root = FirebaseDatabase.getInstance().getReference(roomName);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("Name", userName);
                map2.put("Message", chatEditText.getText().toString());

                message_root.updateChildren(map2);


            }
        });


        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();
//        String msg = chatConversation.getText().toString();
        while (i.hasNext()) {
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot) i.next()).getValue();
//            msg = msg + System.getProperty("line.separator") + chat_user_name + "  :  " + chat_msg;
            chatConversation.append(chat_user_name+"  :  " + chat_msg + System.getProperty("line.separator"));

            chatEditText.setText("");


            notification.setSmallIcon(R.drawable.icon);
            notification.setTicker("This is the ticker");
            notification.setWhen(System.currentTimeMillis());
            notification.setContentTitle("Chat Notification");
            notification.setContentText("People are talking in a conversation your in. Why not get involved?");
            Intent intent = new Intent(this,UsersUpcomingEvents.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent , PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);
            //Builds notification and issues it
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(uniqueId,notification.build());





        }

    }


}





