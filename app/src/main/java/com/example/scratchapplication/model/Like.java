package com.example.scratchapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Like {
    @SerializedName("rId")
    @Expose
    private String rId;
    @SerializedName("userId")
    @Expose
    private String uId;

    public Like(String rId, String uId) {
        this.rId = rId;
        this.uId = uId;
    }

    public String getrId() {
        return rId;
    }

    public String getuId() {
        return uId;
    }
}
