package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommunityCommentListResult {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("comment")
    @Expose
    public String comment;
    @SerializedName("community_post")
    @Expose
    public Integer communityPost;
    @SerializedName("user")
    @Expose
    public Integer user;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("image")
    @Expose
    public String image;

    public Integer getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public Integer getCommunityPost() {
        return communityPost;
    }

    public Integer getUser() {
        return user;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}