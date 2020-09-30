package com.example.kartikonlinefirebase.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CategoryViewModel extends ViewModel {

    public MutableLiveData<Boolean> isUploaded = new MutableLiveData<>(false);

    private MutableLiveData<String> categoryImageUrl = new MutableLiveData<>("");


    private MutableLiveData<String> categoryName = new MutableLiveData<>("");



    public MutableLiveData<String> getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl.setValue(categoryImageUrl);
    }

    public MutableLiveData<String> getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.setValue(categoryName);
    }





}
