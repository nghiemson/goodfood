package com.example.scratchapplication.fragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Follow {
    @SerializedName("uIdFollow")
    @Expose
    private String uIdFollow;
    @SerializedName("uId")
    @Expose
    private String uId;

    public Follow(String uIdFollow, String uId) {
        this.uIdFollow = uIdFollow;
        this.uId = uId;
    }
}
