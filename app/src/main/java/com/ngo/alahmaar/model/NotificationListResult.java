package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationListResult {

@SerializedName("id")
@Expose
public Integer id;
@SerializedName("user_receiver")
@Expose
public Integer userReceiver;
@SerializedName("notification_message")
@Expose
public String notificationMessage;
@SerializedName("notification_headding")
@Expose
public String notificationHeadding;
@SerializedName("recieved_date")
@Expose
public String recievedDate;

    public Integer getId() {
        return id;
    }

    public Integer getUserReceiver() {
        return userReceiver;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public String getNotificationHeadding() {
        return notificationHeadding;
    }

    public String getRecievedDate() {
        return recievedDate;
    }
}