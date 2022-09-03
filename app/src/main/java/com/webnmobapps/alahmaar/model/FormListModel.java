package com.webnmobapps.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FormListModel {



        @SerializedName("success")
        @Expose
        public String success;
        @SerializedName("msg")
        @Expose
        public String msg;
        @SerializedName("data")
        @Expose
        public List<FormListResult> data = null;


}
