package com.example.kartikonlinefirebase.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kartikonlinefirebase.models.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatalogueProductViewModel extends ViewModel {


    public class Catalogue {

        @SerializedName("catalogueId")
        @Expose
        private String catalogueId;

        @SerializedName("catalogueTitle")
        @Expose
        private MutableLiveData<String> catalogueTitle = new MutableLiveData<>("Blank");

        @SerializedName("isPublished")
        @Expose
        private MutableLiveData<Boolean> isPublished = new MutableLiveData<>(false);

        @SerializedName("visitors")
        @Expose
        private MutableLiveData<String> visitors = new MutableLiveData<>("0");

        @SerializedName("catalogueImageUrl")
        @Expose
        private MutableLiveData<String> catalogueImageUrl = new MutableLiveData<>("");

        @SerializedName("products")
        @Expose
        private MutableLiveData <List<ProductViewModel>> products = new MutableLiveData<>();

        public Catalogue(){

        }

        public String getCatalogueId() {
            return catalogueId;
        }

        public void setCatalogueId(String catalogueId) {
            this.catalogueId = catalogueId;
        }

        public LiveData<String> getCatalogueTitle() {
            return catalogueTitle;
        }

        public void setCatalogueTitle(String catalogueTitle) {
            this.catalogueTitle.setValue(catalogueTitle);
        }

        public LiveData <Boolean> getIsPublished() {
            return isPublished;
        }

        public void setIsPublished(Boolean isPublished) {
            this.isPublished.setValue(isPublished);
        }

        public LiveData<String> getVisitors() {
            return visitors;
        }

        public void setVisitors(String visitors) {
            this.visitors.setValue(visitors);
        }

        public LiveData<List<ProductViewModel>> getProducts() {
            return products;
        }

        public void setProducts(List<ProductViewModel> products) {
            this.products.setValue(products);
        }

    }


}
