package com.example.apple.whattodo.UserPreferanceCalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.example.apple.whattodo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeHead;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


@Layout(R.layout.event_card_view)
public class EventCard {



    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @SwipeView
    private android.view.View cardView;
    private FirebaseAuth auth;
    private Profile category;
    private Profile eventId;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private boolean likeEvent ;
    private int swipeCounter=0 ;
    public static ArrayList<String> preferanceId  = new ArrayList<String>();


    public static ArrayList<String> getPreferanceId() {
        return preferanceId;
    }

    public EventCard(
            //userId int,
            Context context,
            Profile profile,
            SwipePlaceHolderView swipeView) {
        mContext = context;
        category = profile;
        eventId = profile;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved(){
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        MultiTransformation multi = new MultiTransformation(
                new BlurTransformation(mContext, 30),
                new RoundedCornersTransformation(
                        mContext, Utils.dpToPx(7), 0,
                        RoundedCornersTransformation.CornerType.TOP));

        Glide.with(mContext).load(category.getImageUrl())
                .bitmapTransform(multi)
                .into(profileImageView);
        nameAgeTxt.setText(category.getName());
        eventId.setEventId(eventId.getEventId());
    }

    @SwipeHead
    private void onSwipeHeadCard() {
        Glide.with(mContext).load(category.getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(
                        mContext, Utils.dpToPx(7), 0,
                        RoundedCornersTransformation.CornerType.TOP))
                .into(profileImageView);
        cardView.invalidate();
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
//        mSwipeView.addView(this);
        // save to db that user does not like this category:
        //User is the owner of the device
        //you can "inject" the id of user, becasue its theonly thing you need
        //app.post(userId, this.category, wantsThis);
        //use the following method to make the above:
//        new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                { userId: userId, categoryId, this.category.Id, wantsThis},
        //          function that handles the response (ok, notOk)

        // Write a message to the database

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        likeEvent=false;
//        DatabaseReference myRef = database.getReference(category.getName()+"---"+likeEvent);
//        myRef.setValue(auth.getUid());




    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        //I figured that I only need to know if they like the event
        Log.d("EVENT", "onSwipedIn");
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        likeEvent=true;
        DatabaseReference myRef = database.getReference("UserPreference"+"---"+category.getName()+"---"+eventId.getEventId());
        myRef.setValue(auth.getUid()+category.getName()+"---"+likeEvent+"---");
        //
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("UserPreferenceSports & Fitness108");
        // myRef.setValue(likeEvent=true);
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        myRef.setValue(eventId.getEventId());
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

                //add the value to the list so I can build the preference variable to search on android Api
                preferanceId.add(value);
                Log.d(TAG, String.valueOf(preferanceId));

                //Save the users Preferance Id to drive
                StringBuilder stringBuilder = new StringBuilder();
                for(String s : preferanceId){
                    stringBuilder.append(s);
                    stringBuilder.append(",");

                }



            }




            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        ;
    }

    @SwipeInState
    private void onSwipeInState(){

        Log.d("EVENT", "onSwipeInState");


        ;
    }



    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}

