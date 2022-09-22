package com.webnmobapps.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonSuccessMsgModel {
    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("msg")
    @Expose
    public String msg;


    public String getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }
}
