package com.example.apple.whattodo.UserPreferanceCalculator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zackeryorourke on 09/03/2018.
 */

public class SubProfile {


    @SerializedName("eventId")
    @Expose
    private String eventId;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("name")
    @Expose
    private String name;

    public SubProfile(String eventId, String url, String name) {
        this.eventId = eventId;
        this.url = url;
        this.name = name;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
