package com.example.kartikonlinefirebase.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Catalogue {

    @SerializedName("catalogueTitle")
    @Expose
    private String catalogueTitle = "";

    @SerializedName("isPublished")
    @Expose
    private Boolean isPublished = false;

    @SerializedName("visitors")
    @Expose
    private String visitors = "";

    @SerializedName("catalogueType")
    @Expose
    private String catalogueType = "";

    @SerializedName("catalogueImageUrl")
    @Expose
    private String catalogueImageUrl = "";


    public Catalogue(){

    }

    public String getCatalogueTitle() {
        return catalogueTitle;
    }

    public void setCatalogueTitle(String catalogueTitle) {
        this.catalogueTitle = catalogueTitle;
    }

    public String getVisitors() {
        return visitors;
    }

    public void setVisitors(String visitors) {
        this.visitors = visitors;
    }

    public String getCatalogueType() {
        return catalogueType;
    }

    public void setCatalogueType(String catalogueType) {
        this.catalogueType = catalogueType;
    }

    public String getCatalogueImageUrl() {
        return catalogueImageUrl;
    }

    public void setCatalogueImageUrl(String catalogueImageUrl) {
        this.catalogueImageUrl = catalogueImageUrl;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

}
