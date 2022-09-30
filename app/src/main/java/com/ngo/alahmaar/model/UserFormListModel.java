package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserFormListModel {



        @SerializedName("success")
        @Expose
        public String success;
        @SerializedName("msg")
        @Expose
        public String msg;
        @SerializedName("data")
        @Expose
        public List<UserFormListResult> data = null;

    public String getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public List<UserFormListResult> getData() {
        return data;
    }
}
