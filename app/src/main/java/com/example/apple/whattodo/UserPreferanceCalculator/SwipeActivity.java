package com.example.apple.whattodo.UserPreferanceCalculator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.example.apple.whattodo.EventController.EventFeed;
import com.example.apple.whattodo.EventController.EventsInMyArea;
import com.example.apple.whattodo.R;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

public class SwipeActivity extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipt_activity);

        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        int bottomMargin = Utils.dpToPx(160);
        Point windowSize = Utils.getDisplaySize(getWindowManager());
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeMaxChangeAngle(2f)
                        .setSwipeInMsgLayoutId(R.layout.event_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.event_swipe_out_msg_view));




        for(Profile profile : Utils.loadProfiles(this.getApplicationContext()))


        {

            mSwipeView.addView(new EventCard(
                    mContext, profile,  mSwipeView));
        }



        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        findViewById(R.id.undoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.undoLastSwipe();
            }
        });
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


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
