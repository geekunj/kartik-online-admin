package com.example.kartikonlinefirebase.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Set {

    @SerializedName("setQty")
    @Expose
    private String setQty = "";

    public String getSetQty() {
        return setQty;
    }

    public void setSetQty(String setQty) {
        this.setQty = setQty;
    }

}
