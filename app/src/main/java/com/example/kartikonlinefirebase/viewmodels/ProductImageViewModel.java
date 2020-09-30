package com.example.kartikonlinefirebase.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductImageViewModel extends ViewModel {

    @SerializedName("imageId")
    @Expose
    private String imageId;

    @SerializedName("imageUrl")
    @Expose
    private MutableLiveData<String> imageUrl;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl .setValue(imageUrl);
    }
}
