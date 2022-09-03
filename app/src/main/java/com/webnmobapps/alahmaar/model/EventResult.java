package com.webnmobapps.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventResult {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("headding")
    @Expose
    public String headding;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("gallery")
    @Expose
    public ArrayList<EventGallry> eventGallry = null;


    public Integer getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getHeadding() {
        return headding;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<EventGallry> getGallery() {
        return eventGallry;
    }
}