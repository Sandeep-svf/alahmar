package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserProfileModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("data")
@Expose
public GetUserProfileResult getUserProfileResult;


    public String getSuccess() {
        return success;
    }

    public String getMsg() {
        return message;
    }

    public GetUserProfileResult getData() {
        return getUserProfileResult;
    }
}