package com.example.scratchapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfilePojo {
    @SerializedName("data")
    @Expose
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }
}
