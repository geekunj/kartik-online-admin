package com.example.kartikonlinefirebase.models;

public class WholesaleCustomer {

    private String shopName;
    private String representativeName;
    private String whatsappNumber;
    private String otherNumber;
    private String gstNumber;
    private String emailId;
    private String fullAddress;
    private String landmark;
    private String city;
    private String postalCode;
    private String transportName;
    private String transportAddress;
    private String transportNumber;
    private Boolean isBlocked;
    private Boolean isPrior;


    public String getShopName() {
        return shopName;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public String getOtherNumber() {
        return otherNumber;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getTransportName() {
        return transportName;
    }

    public String getTransportAddress() {
        return transportAddress;
    }

    public String getTransportNumber() {
        return transportNumber;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public Boolean getPrior() {
        return isPrior;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public void setPrior(Boolean prior) {
        isPrior = prior;
    }


}
