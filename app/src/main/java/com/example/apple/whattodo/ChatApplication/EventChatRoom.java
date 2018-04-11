package com.example.apple.whattodo.ChatApplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apple.whattodo.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventChatRoom extends AppCompatActivity {


    private Button sendMessage;
    private EditText chatEditText;
    private TextView chatConversation;
    private String userName, roomName;
    private String temp_key;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Chats");
    private String chat_msg, chat_user_name;

    @Override
    protected void onCreate(@Nullable Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.event_chat);

        sendMessage = (Button) findViewById(R.id.sendButton);
        chatEditText = (EditText) findViewById(R.id.editTextMessage);
        chatConversation = (TextView) findViewById(R.id.chatConversation);


        //Grab the intents

        userName = getIntent().getExtras().get("UserName").toString();
        roomName = getIntent().getExtras().get("EventRoomName").toString();
        setTitle("Event Room  "+roomName);

        root = FirebaseDatabase.getInstance().getReference(roomName);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String,Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("Name", userName);
                map2.put("Message",chatEditText.getText().toString());

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

        while(i.hasNext()){
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
            chatConversation.append(chat_user_name+"  :  " + chat_msg + " \n");

        }
    }


}





