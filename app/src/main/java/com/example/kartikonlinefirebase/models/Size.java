package com.example.kartikonlinefirebase.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Size {

    @SerializedName("size")
    @Expose
    private String size = "";

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
