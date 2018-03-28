package com.example.apple.whattodo.UserPreferanceCalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.example.apple.whattodo.AccountActivity.LoginActivity;
import com.example.apple.whattodo.AccountActivity.RegisterActivity;
import com.example.apple.whattodo.AccountActivity.ResetPasswordActivity;
import com.example.apple.whattodo.EventController.EventFeed;
import com.example.apple.whattodo.EventController.EventIndexActivity;
import com.example.apple.whattodo.MainActivity;
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


@Layout(R.layout.event_card_view)
public class EventCard extends AppCompatActivity {


    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView eventName;

    @SwipeView
    private android.view.View cardView;
    private FirebaseAuth auth;
    private Profile profile;
    private Profile eventId;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private boolean likeEvent;
    private int swipeCounter = 0;
    public static ArrayList<String> preferanceId = new ArrayList<String>();


    public static ArrayList<String> getPreferanceId() {
        return preferanceId;
    }

    public EventCard (
            Context context,
            Profile profile,
            SwipePlaceHolderView swipeView) {
        mContext = context;
        this.profile = profile;
        eventId = profile;
        mSwipeView = swipeView;
    }


    @Resolve
    private void onResolved1() {
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        MultiTransformation multi = new MultiTransformation(
                new BlurTransformation(mContext, 30),
                new RoundedCornersTransformation(
                        mContext, Utils.dpToPx(7), 0,
                        RoundedCornersTransformation.CornerType.TOP));

        Glide.with(mContext).load(profile.getImageUrl())
                .bitmapTransform(multi)
                .into(profileImageView);
        eventName.setText(profile.getName());
//        eventId.setEventId(eventId.getEventId());


    }



    @SwipeHead
    private void onSwipeHeadCard() {


        Glide.with(mContext).load(profile.getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(
                        mContext, Utils.dpToPx(7), 0,
                        RoundedCornersTransformation.CornerType.TOP))
                .into(profileImageView);
        cardView.invalidate();
    }


    @SwipeOut
    private void onSwipedOut() {
        Log.d("EVENT", "onSwipedOut");


    }



    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn() {

        eventId.setEventId(eventId.getEventId());
        Log.d("EVENT", "onSwipedIn");
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ID" +FirebaseAuth.getInstance().getUid());
        myRef.setValue(eventId.getEventId());
        database = FirebaseDatabase.getInstance();
        final FirebaseDatabase finalDatabase = database;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //Trying to refresh the view
                String value = dataSnapshot.getValue(String.class);
                //add the value to the list so I can build the preference variable to search on android Api
                //If the Id does not exist, then add that to preference Id
                if (!preferanceId.contains(value)) {
                    preferanceId.add(value);

                    if (value.equals("200")) {
                        //In Here add the preference Id to the database when the Id is 200 because this marks the end
                        // On top of that, then bring the user our of the application -
                        // Write a message to the database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myPref = database.getReference("Preference Logs "+ auth.getUid());
                        myPref.setValue(preferanceId);






                    }


                }



                //load the sub categories until they have selected what they want
                //My issue here is that the sub categories require the screen to be reloaded

//                List<SubProfile> subProfiles = profile.getSubcategory();
//                for (SubProfile sp : subProfiles) {
//                    String id = sp.getEventId();
//                    String name = sp.getName();
//                    String url = sp.getUrl();
//                    DatabaseReference myPrefRef = finalDatabase.getReference("UserPrefString");
//                    myPrefRef.setValue(preferanceId);
//
//                }


             //   startActivity(new Intent(EventCard.this, SubEventCard.class));



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
    private void onSwipeInState() {

        Log.d("EVENT", "onSwipeInState");




        ;
    }




    @SwipeOutState
    private void onSwipeOutState() {
        Log.d("EVENT", "onSwipeOutState");
    }

}

