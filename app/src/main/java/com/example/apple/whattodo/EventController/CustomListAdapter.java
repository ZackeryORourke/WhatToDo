package com.example.apple.whattodo.EventController;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.whattodo.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<EventModel> eventItems;
    private ImageView image;

    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<EventModel> eventItems) {
        this.activity = activity;
        this.eventItems = eventItems;
    }

    @Override
    public int getCount() {
        return eventItems.size();
    }

    @Override
    public Object getItem(int location) {
        return eventItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        //TextView location = (TextView) convertView.findViewById(R.id.location);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView time= (TextView) convertView.findViewById(R.id.time);

        EventModel m = eventItems.get(position);

        ImageView image = (ImageView) convertView.findViewById(R.id.thumbnail);

        String url =(m.getThumbnailUrl());

        Picasso.with(activity)
                .load(url)
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_normal_background) // optional
                .error(R.drawable.common_full_open_on_phone)         // optional
                .into(image);

        title.setText(m.getTitle());
        //location.setText(m.getLocation());
        date.setText(m.getDate());
        //time.setText(m.getTime());

        return convertView;
    }



}