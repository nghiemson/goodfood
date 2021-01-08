package com.example.scratchapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListUsers {
    @SerializedName("users")
    @Expose
    private List<Profile> profiles;

    public List<Profile> getProfiles() {
        return profiles;
    }
}
