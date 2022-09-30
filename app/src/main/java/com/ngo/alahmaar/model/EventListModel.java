package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventListModel {

@SerializedName("msg")
@Expose
public String msg;
@SerializedName("success")
@Expose
public String success;
@SerializedName("data")
@Expose
public ArrayList<EventResult> data = null;


    public String getMsg() {
        return msg;
    }

    public String getSuccess() {
        return success;
    }

    public ArrayList<EventResult> getData() {
        return data;
    }
}