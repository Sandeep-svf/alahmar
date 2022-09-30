package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommuntyPostResult {

@SerializedName("id")
@Expose
public Integer id;
@SerializedName("image")
@Expose
public String image;
@SerializedName("topic_description")
@Expose
public String topicDescription;
@SerializedName("likes")
@Expose
public Integer likes;
@SerializedName("comment")
@Expose
public Integer comment;
    @SerializedName("headdings")
    @Expose
    public String headdings;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("is_liked")
    @Expose
    public String is_liked;

    public String getIs_liked() {
        return is_liked;
    }

    public Integer getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public Integer getLikes() {
        return likes;
    }

    public Integer getComment() {
        return comment;
    }

    public String getHeaddings() {
        return headdings;
    }

    public String getDate() {
        return date;
    }

    public void setIs_liked(String is_liked) {
        this.is_liked = is_liked;
    }
}