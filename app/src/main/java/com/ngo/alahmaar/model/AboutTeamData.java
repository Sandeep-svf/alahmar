package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AboutTeamData {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("designation")
    @Expose
    public String designation;
    @SerializedName("image")
    @Expose
    public String image;

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getImage() {
        return image;
    }
}
