package com.example.apple.whattodo.EventController;



        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;

        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;

        import android.view.MenuItem;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.example.apple.whattodo.R;
        import com.example.apple.whattodo.UserPreferanceCalculator.SwipeActivity;
        import com.squareup.picasso.Picasso;


public class EventIndexActivity extends AppCompatActivity {




    private TextView title,time, location;
    private LayoutInflater inflater;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventindexactivity);
        Intent intent = getIntent();
        String eventtitle = intent.getExtras().getString("ValueKey");
        String eventtime = intent.getExtras().getString("ValueKey2");
        String eventLocation = intent.getExtras().getString("ValueKey3");
        String eventImage = intent.getExtras().getString("ValueKey4");

        ImageView image = (ImageView) findViewById(R.id.thumbnail);

        Picasso.with(this)
                .load(eventImage)
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_normal_background) // optional
                .error(R.drawable.common_full_open_on_phone)         // optional
                .into(image);




        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.date);
        location = (TextView) findViewById(R.id.location);








        title.setText(eventtitle);
        time.setText(eventtime);
        location.setText(eventLocation);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

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
