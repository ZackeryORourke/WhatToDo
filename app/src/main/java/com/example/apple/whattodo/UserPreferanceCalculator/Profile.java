package com.example.apple.whattodo.UserPreferanceCalculator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Profile {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String imageUrl;

    @SerializedName("eventId")
    @Expose
    private String eventId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId= eventId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }




}
