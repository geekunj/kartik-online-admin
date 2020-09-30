package com.example.kartikonlinefirebase.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("ProductId")
    @Expose
    private String ProductId;

    @SerializedName("productImageUrl")
    @Expose
    private String productImageUrl;

    @SerializedName("ProductId")
    @Expose
    private List<String> moreImagesUrls = new ArrayList<>();

    @SerializedName("catalogueId")
    @Expose
    private String catalogueId = "";

    @SerializedName("availableQuantity")
    @Expose
    private String availableQuantity = "0";

    @SerializedName("cartonQuanity")
    @Expose
    private String cartonQuanity = "0";

    @SerializedName("categoryName")
    @Expose
    private String categoryName = "";

    @SerializedName("color")
    @Expose
    private String color = "";

    @SerializedName("colorSelection")
    @Expose
    private String colorSelection = "";

    @SerializedName("description")
    @Expose
    private String description = "";

    @SerializedName("discountPrice")
    @Expose
    private String discountPrice = "0";

    @SerializedName("isOutOfStock")
    @Expose
    private Boolean isOutOfStock = true;

    @SerializedName("productName")
    @Expose
    private String productName = "";

    @SerializedName("price")
    @Expose
    private String price = "0";

    @SerializedName("setQuantity")
    @Expose
    private String setQuantity = "0";

    @SerializedName("size")
    @Expose
    private String size = "";

    @SerializedName("sizeSelection")
    @Expose
    private String sizeSelection = "";

    @SerializedName("soleName")
    @Expose
    private String soleName = "";

    @SerializedName("sortTags")
    @Expose
    private String sortTags = "";

    @SerializedName("type")
    @Expose
    private String type = "";

    @SerializedName("notes")
    @Expose
    private String notes = "";

    @SerializedName("videoUrl")
    @Expose
    private String videoUrl = "";

    @SerializedName("isShowOutOfStock")
    @Expose
    private Boolean isShowOutOfStock = true;

    @SerializedName("isForceAllowOrder")
    @Expose
    private Boolean isForceAllowOrder = true;


    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getCartonQuanity() {
        return cartonQuanity;
    }

    public void setCartonQuanity(String cartonQuanity) {
        this.cartonQuanity = cartonQuanity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorSelection() {
        return colorSelection;
    }

    public void setColorSelection(String colorSelection) {
        this.colorSelection = colorSelection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Boolean getIsOutOfStock() {
        return isOutOfStock;
    }

    public void setIsOutOfStock(Boolean isOutOfStock) {
        this.isOutOfStock = isOutOfStock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSetQuantity() {
        return setQuantity;
    }

    public void setSetQuantity(String setQuantity) {
        this.setQuantity = setQuantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSizeSelection() {
        return sizeSelection;
    }

    public void setSizeSelection(String sizeSelection) {
        this.sizeSelection = sizeSelection;
    }

    public String getSoleName() {
        return soleName;
    }

    public void setSoleName(String soleName) {
        this.soleName = soleName;
    }

    public String getSortTags() {
        return sortTags;
    }

    public void setSortTags(String sortTags) {
        this.sortTags = sortTags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Boolean getIsShowOutOfStock() {
        return isShowOutOfStock;
    }

    public void setIsShowOutOfStock(Boolean isShowOutOfStock) {
        this.isShowOutOfStock = isShowOutOfStock;
    }

    public Boolean getIsForceAllowOrder() {
        return isForceAllowOrder;
    }

    public void setIsForceAllowOrder(Boolean isForceAllowOrder) {
        this.isForceAllowOrder = isForceAllowOrder;
    }

    public String getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(String catalogueId) {
        this.catalogueId = catalogueId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public List<String> getMoreImagesUrls() {
        return moreImagesUrls;
    }

    public void setMoreImagesUrls(List<String> moreImagesUrls) {
        this.moreImagesUrls = moreImagesUrls;
    }

}