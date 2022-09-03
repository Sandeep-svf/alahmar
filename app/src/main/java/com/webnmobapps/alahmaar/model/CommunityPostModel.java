package com.webnmobapps.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommunityPostModel {


        @SerializedName("data")
        @Expose
        public List<CommuntyPostResult> data = null;
        @SerializedName("success")
        @Expose
        public String success;

    public List<CommuntyPostResult> getData() {
        return data;
    }

    public String getSuccess() {
        return success;
    }
}
