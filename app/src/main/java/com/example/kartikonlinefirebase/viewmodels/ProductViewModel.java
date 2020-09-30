package com.example.kartikonlinefirebase.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kartikonlinefirebase.models.Image;
import com.example.kartikonlinefirebase.models.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {

    Product mProduct = new Product();

    public MutableLiveData<Boolean> eventDataChanged = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> isSaveClicked = new MutableLiveData<>(false);


    private String productId;
    private MutableLiveData<String> catalogueId = new MutableLiveData<>("");
    private MutableLiveData<String> availableQuantity = new MutableLiveData<>("0");
    private MutableLiveData<String> cartonQuanity = new MutableLiveData<>("0");
    private MutableLiveData<String> categoryName = new MutableLiveData<>("");
    private MutableLiveData<String> color = new MutableLiveData<>("");
    private MutableLiveData<String> colorSelection = new MutableLiveData<>("");
    private MutableLiveData<String> description = new MutableLiveData<>("");
    private MutableLiveData<String> discountPrice = new MutableLiveData<>("0");

    private MutableLiveData<Boolean> isOutOfStock = new MutableLiveData<>(true);
    private MutableLiveData<String> productName = new MutableLiveData<>("");
    private MutableLiveData<String> price = new MutableLiveData<>("0");
    private MutableLiveData<String> setQuantity = new MutableLiveData<>("0");
    private MutableLiveData<String> size = new MutableLiveData<>("0");
    private MutableLiveData<String> sizeSelection = new MutableLiveData<>("");
    private MutableLiveData<String> soleName = new MutableLiveData<>("");
    private MutableLiveData<String> sortTags = new MutableLiveData<>("");
    private MutableLiveData<String> type = new MutableLiveData<>("");
    private MutableLiveData<String> notes = new MutableLiveData<>("");
    private MutableLiveData<String> videoUrl = new MutableLiveData<>("");
    private MutableLiveData<Boolean> isShowOutOfStock = new MutableLiveData<>(true);
    private MutableLiveData<Boolean> isForceAllowOrder = new MutableLiveData<>(false);


    public void onDataChanged(){
        eventDataChanged.setValue(true);
    }

    public Product onClickSaveButton(){

        isSaveClicked.setValue(true);

        mProduct.setCatalogueId(catalogueId.getValue());
        mProduct.setProductName(productName.getValue());
        mProduct.setPrice(price.getValue());
        mProduct.setDiscountPrice(discountPrice.getValue());
        mProduct.setCartonQuanity(cartonQuanity.getValue());
        mProduct.setSetQuantity(setQuantity.getValue());
        mProduct.setSize(size.getValue());
        mProduct.setSizeSelection(sizeSelection.getValue());
        mProduct.setColor(colorSelection.getValue());
        mProduct.setColorSelection(colorSelection.getValue());
        mProduct.setCategoryName(categoryName.getValue());
        mProduct.setSortTags(sortTags.getValue());
        mProduct.setType(type.getValue());
        mProduct.setSoleName(soleName.getValue());
        mProduct.setDescription(description.getValue());
        mProduct.setIsShowOutOfStock(isShowOutOfStock.getValue());
        mProduct.setIsForceAllowOrder(isForceAllowOrder.getValue());
        mProduct.setIsOutOfStock(isOutOfStock.getValue());
        mProduct.setAvailableQuantity(availableQuantity.getValue());
        mProduct.setNotes(notes.getValue());
        mProduct.setVideoUrl(videoUrl.getValue());


        return mProduct;
    }


    public LiveData<String> getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(String catalogueId) {
        this.catalogueId.setValue(catalogueId);
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public LiveData<String> getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity.setValue(availableQuantity);
    }

    public LiveData<String> getCartonQuanity() {
        return cartonQuanity;
    }

    public void setCartonQuanity(String cartonQuanity) {
        this.cartonQuanity.setValue(cartonQuanity);
    }

    public LiveData<String> getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.setValue(categoryName);
    }

    public LiveData<String> getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color.setValue(color);
    }

    public LiveData<String> getColorSelection() {
        return colorSelection;
    }

    public void setColorSelection(String colorSelection) {
        this.colorSelection.setValue(colorSelection);
    }

    public LiveData<String> getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public LiveData<String> getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice.setValue(discountPrice);
    }


    public LiveData<Boolean> getIsOutOfStock() {
        return isOutOfStock;
    }

    public void setIsOutOfStock(Boolean isOutOfStock) {
        this.isOutOfStock.setValue(isOutOfStock);
    }

    public LiveData<String> getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.setValue(productName);
    }

    public LiveData<String> getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price.setValue(price);
    }

    public LiveData<String> getSetQuantity() {
        return setQuantity;
    }

    public void setSetQuantity(String setQuantity) {
        this.setQuantity.setValue(setQuantity);
    }

    public LiveData<String> getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size.setValue(size);
    }

    public LiveData<String> getSizeSelection() {
        return sizeSelection;
    }

    public void setSizeSelection(String sizeSelection) {
        this.sizeSelection.setValue(sizeSelection);
    }

    public LiveData<String> getSoleName() {
        return soleName;
    }

    public void setSoleName(String soleName) {
        this.soleName.setValue(soleName);
    }

    public LiveData<String> getSortTags() {
        return sortTags;
    }

    public void setSortTags(String sortTags) {
        this.sortTags.setValue(sortTags);
    }

    public LiveData<String> getType() {
        return type;
    }

    public void setType(String type) {
        this.type.setValue(type);
    }

    public LiveData<String> getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.setValue(notes);
    }

    public LiveData<String> getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl.setValue(videoUrl);
    }

    public LiveData<Boolean> getIsShowOutOfStock() {
        return isShowOutOfStock;
    }

    public void setIsShowOutOfStock(Boolean isShowOutOfStock) {
        this.isShowOutOfStock.setValue(isShowOutOfStock);
    }

    public LiveData<Boolean> getIsForceAllowOrder() {
        return isForceAllowOrder;
    }

    public void setIsForceAllowOrder(Boolean isForceAllowOrder) {
        this.isForceAllowOrder.setValue(isForceAllowOrder);
    }

}
