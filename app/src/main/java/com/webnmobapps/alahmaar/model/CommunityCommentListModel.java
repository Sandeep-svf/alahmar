package com.webnmobapps.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommunityCommentListModel {

@SerializedName("data")
@Expose
public List<CommunityCommentListResult> data = null;
@SerializedName("success")
@Expose
public String success;


    public List<CommunityCommentListResult> getData() {
        return data;
    }

    public void setData(List<CommunityCommentListResult> data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}