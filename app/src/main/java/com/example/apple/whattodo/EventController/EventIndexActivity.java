package com.example.apple.whattodo.EventController;

        import android.app.Activity;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.CalendarContract;
        import android.support.v7.app.AppCompatActivity;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.apple.whattodo.ChatApplication.ChatMain;
        import com.example.apple.whattodo.ChatApplication.EventChatRoom;
        import com.example.apple.whattodo.MainActivity;
        import com.example.apple.whattodo.R;
        import com.example.apple.whattodo.UserPreferanceCalculator.SwipeActivity;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.squareup.picasso.Picasso;

        import java.util.GregorianCalendar;


public class EventIndexActivity extends AppCompatActivity {


    private TextView title,time, location;
    private Button back,calender, notifications,purchaseTickets;
    private FirebaseAuth mAuth;
    private FirebaseUser fbUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventindexactivity);
        Intent intent = getIntent();
        final String eventtitle = intent.getExtras().getString("ValueKey");
        final String eventtime = intent.getExtras().getString("ValueKey2");
        final String eventLocation = intent.getExtras().getString("ValueKey3");
        final String eventImage = intent.getExtras().getString("ValueKey4");
        final String eventUrl = intent.getExtras().getString("ValueKey5");
        final String eventDescription = intent.getExtras().getString("ValueKey6");


        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

        final ImageView image = (ImageView) findViewById(R.id.thumbnail);

        Picasso.with(this)
                .load(eventImage)
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_normal_background) // optional
                .error(R.drawable.common_full_open_on_phone)         // optional
                .into(image);




        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.date);
        location = (TextView) findViewById(R.id.location);
        back = (Button) findViewById(R.id.btn_Home);
        calender = (Button) findViewById(R.id.add_Calender);
        notifications = (Button) findViewById(R.id.notify_Me);
        purchaseTickets = (Button) findViewById(R.id.purchase);
        title.setText(eventtitle);
        time.setText(eventtime);
        location.setText(eventLocation);



        back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(EventIndexActivity.this, EventFeed.class));
        }
    });
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setData(CalendarContract.Events.CONTENT_URI);
                calIntent.putExtra(CalendarContract.Events.TITLE, eventtitle);
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocation);
               calIntent.putExtra(CalendarContract.Events.DESCRIPTION, eventDescription);
                GregorianCalendar calDate = new GregorianCalendar(2012, 7, 15);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calDate.getTimeInMillis());
                startActivity(calIntent);
            }
        });


        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventIndexActivity.this,EventChatRoom.class);
                i.putExtra("ValueKey", eventtitle);
                startActivity(i);

            }
        });


        purchaseTickets.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usersEvents").child(fbUser.getUid());
                EventModel eventModel = new EventModel(eventtitle, eventLocation,eventtime , eventtime, null, eventImage, null);
                String eventsId = databaseReference.push().getKey();
                databaseReference.child(eventsId).setValue(eventModel);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(eventUrl));
                startActivity(intent);
            }
        });







    }


    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu, menu);
        return true;
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

            case R.id.userMenu:
                Intent userIntent = new Intent(this, MainActivity.class);
                this.startActivity(userIntent);
                return true;



            case R.id.upComingEvents:
                Intent upcomingEvents = new Intent(this, UsersUpcomingEvents.class);
                this.startActivity(upcomingEvents);
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }












}
