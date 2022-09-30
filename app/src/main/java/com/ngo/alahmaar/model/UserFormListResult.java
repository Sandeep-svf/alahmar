package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserFormListResult {

@SerializedName("link")
@Expose
public String link;
@SerializedName("name")
@Expose
public String name;


    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }
}