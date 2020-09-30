package com.example.kartikonlinefirebase.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Banner {



    @SerializedName("bannerImageUrl")
    @Expose
    private String bannerImageUrl = "";

    @SerializedName("catalogueId")
    @Expose
    private String catalogueId = "";

    @SerializedName("catalogueTitle")
    @Expose
    private String catalogueTitle = "";

    @SerializedName("catalogueTitle")
    @Expose
    private boolean isClickable = false;

    @SerializedName("positionOnUser")
    @Expose
    private String positionOnUser = "";



    public String getCatalogueTitle() {
        return catalogueTitle;
    }

    public String getPositionOnUser() {
        return positionOnUser;
    }

    public void setPositionOnUser(String positionOnUser) {
        this.positionOnUser = positionOnUser;
    }


    public void setCatalogueTitle(String catalogueTitle) {
        this.catalogueTitle = catalogueTitle;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }


    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(String catalogueId) {
        this.catalogueId = catalogueId;
    }

}
