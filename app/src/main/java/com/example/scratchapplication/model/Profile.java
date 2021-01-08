package com.example.scratchapplication.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.scratchapplication.room.DataConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "profiles")
public class Profile {
    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "follows")
    @SerializedName("follows")
    @Expose
    private List<String> follows = null;
    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "saves")
    @SerializedName("saves")
    @Expose
    private List<String> saves = null;

    @ColumnInfo(name = "address")
    @SerializedName("address")
    @Expose
    private String address;

    @ColumnInfo(name = "avatar")
    @NonNull
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @ColumnInfo(name = "likes")
    @SerializedName("likes")
    @Expose
    private Integer likes;

    @ColumnInfo(name = "userName")
    @SerializedName("userName")
    @Expose
    private String userName;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "userId")
    @SerializedName("userId")
    @Expose
    private String userId;

    public Profile() {
    }

    public Profile(List<String> follows, List<String> saves,
                    String address, String avatar,
                   Integer likes, String userName, String userId) {
        this.follows = follows;
        this.saves = saves;
        this.address = address;
        this.avatar = avatar;
        this.likes = likes;
        this.userName = userName;
        this.userId = userId;
    }

    public Profile(String address, @NonNull String avatar, String userName, @NonNull String userId) {
        this.address = address;
        this.avatar = avatar;
        this.userName = userName;
        this.userId = userId;
    }

    public List<String> getFollows() {
        return follows;
    }

    public void setFollows(List<String> follows) {
        this.follows = follows;
    }

    public List<String> getSaves() {
        return saves;
    }

    public void setSaves(List<String> saves) {
        this.saves = saves;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
