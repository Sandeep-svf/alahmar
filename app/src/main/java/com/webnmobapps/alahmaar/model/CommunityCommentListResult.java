package com.webnmobapps.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommunityCommentListResult {

@SerializedName("id")
@Expose
public Integer id;
@SerializedName("image")
@Expose
public String image;
@SerializedName("topic_description")
@Expose
public String topicDescription;
@SerializedName("headdings")
@Expose
public String headdings;
@SerializedName("date")
@Expose
public String date;
@SerializedName("likes")
@Expose
public Integer likes;
@SerializedName("comment")
@Expose
public Integer comment;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public String getHeaddings() {
        return headdings;
    }

    public void setHeaddings(String headdings) {
        this.headdings = headdings;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }
}