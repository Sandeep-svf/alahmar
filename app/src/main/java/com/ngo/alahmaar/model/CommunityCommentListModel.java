package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommunityCommentListModel {

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public List<CommunityCommentListResult> data = null;


    public String getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public List<CommunityCommentListResult> getData() {
        return data;
    }
}