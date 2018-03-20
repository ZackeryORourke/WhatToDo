package com.example.apple.whattodo.EventController;


public class EventModel {

    private String title,location;
    private String date, time;
    private String thumbnailUrl;
    private String url;
    private Integer[] imgid;
    public EventModel(){

    }


    public EventModel(String title, String location, String date, String time, String thumbnailUrl, String url) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.location = location;
        this.date = date;
        this.time = time;
        this.url = url;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



}








