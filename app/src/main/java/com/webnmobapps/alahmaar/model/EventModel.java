package com.webnmobapps.alahmaar.model;

public class EventModel {
    private String eventname;
    private String eventCreatedAt;
    private int eventProfile;
    private String eventDesc;


    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getEventCreatedAt() {
        return eventCreatedAt;
    }

    public void setEventCreatedAt(String eventCreatedAt) {
        this.eventCreatedAt = eventCreatedAt;
    }

    public int getEventProfile() {
        return eventProfile;
    }

    public void setEventProfile(int eventProfile) {
        this.eventProfile = eventProfile;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }
}
