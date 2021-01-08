package com.example.scratchapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Save {
    @SerializedName("uId")
    @Expose
    private String uId;
    @SerializedName("rId")
    @Expose
    private String rId;

    public Save(String uId, String rId) {
        this.uId = uId;
        this.rId = rId;
    }

    public String getuId() {
        return uId;
    }

    public String getrId() {
        return rId;
    }
}
