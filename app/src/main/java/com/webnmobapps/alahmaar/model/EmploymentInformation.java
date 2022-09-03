package com.webnmobapps.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmploymentInformation {

@SerializedName("link:")
@Expose
public String link;
@SerializedName("name")
@Expose
public String name;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}