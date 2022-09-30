package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileData {

@SerializedName("id")
@Expose
public Integer id;
@SerializedName("username")
@Expose
public String username;
@SerializedName("email")
@Expose
public String email;
@SerializedName("phone_number")
@Expose
public String phoneNumber;
@SerializedName("image")
@Expose
public String image;

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImage() {
        return image;
    }
}