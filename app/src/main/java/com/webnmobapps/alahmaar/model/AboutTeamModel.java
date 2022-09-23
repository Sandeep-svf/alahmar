package com.webnmobapps.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AboutTeamModel {
    @SerializedName("data")
    @Expose
    public List<AboutTeamData> data = null;
    @SerializedName("success")
    @Expose
    public String success;


    public List<AboutTeamData> getData() {
        return data;
    }

    public String getSuccess() {
        return success;
    }
}
