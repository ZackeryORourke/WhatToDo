package com.example.apple.whattodo.UserPreferanceCalculator;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.example.apple.whattodo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;



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
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private boolean likeEvent ;
    private int swipeCounter=0 ;

    public EventCard(
            //userId int,
            Context context,
            Profile profile,
            SwipePlaceHolderView swipeView) {
        mContext = context;
        category = profile;
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

//    @Click(R.id.profileImageView)
//    private void onClick(){
//        Log.d("EVENT", "profileImageView click");
////        mSwipeView.addView(this);
//    }

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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        likeEvent=false;
        DatabaseReference myRef = database.getReference(category.getName()+"---"+likeEvent);
        myRef.setValue(auth.getUid());





    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        likeEvent=true;
        DatabaseReference myRef = database.getReference(category.getName()+"---"+likeEvent);
        myRef.setValue(auth.getUid());
       // myRef.setValue(likeEvent=true);



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


//accept is swipe in
//reject is swipe out