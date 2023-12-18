package com.kenazalfizza.farmersmarketplace.api.response;

import com.google.gson.annotations.SerializedName;

public class StoreResponse {

    @SerializedName("id")
    String storeId;

    @SerializedName("name")
    String storeName;

    @SerializedName("email")
    String storeEmail;

    @SerializedName("phone")
    String storePhone;

    @SerializedName("province")
    String storeProvince;

    @SerializedName("city")
    String storeCity;

    @SerializedName("postcode")
    String storePostcode;

    @SerializedName("address")
    String storeAddress;

    @SerializedName("description")
    String storeDescription;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreEmail() {
        return storeEmail;
    }

    public void setStoreEmail(String storeEmail) {
        this.storeEmail = storeEmail;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreProvince() {
        return storeProvince;
    }

    public void setStoreProvince(String storeProvince) {
        this.storeProvince = storeProvince;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public String getStorePostcode() {
        return storePostcode;
    }

    public void setStorePostcode(String storePostcode) {
        this.storePostcode = storePostcode;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    @Override
    public String toString() {
        return "StoreResponse{" +
                "storeId='" + storeId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeEmail='" + storeEmail + '\'' +
                ", storePhone='" + storePhone + '\'' +
                ", storeProvince='" + storeProvince + '\'' +
                ", storeCity='" + storeCity + '\'' +
                ", storePostcode='" + storePostcode + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", storeDescription='" + storeDescription + '\'' +
                '}';
    }
}