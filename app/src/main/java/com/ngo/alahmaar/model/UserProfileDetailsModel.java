package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileDetailsModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("msg")
@Expose
public String msg;
@SerializedName("data")
@Expose
public UserProfileData userProfileData;

    public String getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public UserProfileData getData() {
        return userProfileData;
    }
}