package com.example.scratchapplication.model.profile;

public class FollowingList {
    private int imageAvatar;
    private String name;
    private String bio;
    private String numberInfo;

    public FollowingList(int imageAvatar, String name, String bio, String numberInfo) {
        this.imageAvatar = imageAvatar;
        this.name = name;
        this.bio = bio;
        this.numberInfo = numberInfo;
    }

    public int getImageAvatar() {
        return imageAvatar;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public void setImageAvatar(int imageAvatar) {
        this.imageAvatar = imageAvatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getNumberInfo() {
        return numberInfo;
    }

    public void setNumberInfo(String numberFollowers) {
        this.numberInfo = numberFollowers;
    }




}
