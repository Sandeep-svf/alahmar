package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalNumberNotification {

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("total_notification")
    @Expose
    public Integer totalNotification;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getTotalNotification() {
        return totalNotification;
    }

    public void setTotalNotification(Integer totalNotification) {
        this.totalNotification = totalNotification;
    }
}
