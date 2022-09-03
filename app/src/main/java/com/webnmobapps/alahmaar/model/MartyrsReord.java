package com.webnmobapps.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MartyrsReord {

@SerializedName("name")
@Expose
public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}