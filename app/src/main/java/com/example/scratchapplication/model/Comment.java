package com.example.scratchapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("rId")
    @Expose
    private String rId;
    @SerializedName("cmtId")
    @Expose
    private String cmtId;

    public Comment(String comment, String userId, String rId) {
        this.comment = comment;
        this.userId = userId;
        this.rId = rId;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public void setCmtId(String cmtId) {
        this.cmtId = cmtId;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getComment() {
        return comment;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getrId() {
        return rId;
    }

    public String getCmtId() {
        return cmtId;
    }
}
