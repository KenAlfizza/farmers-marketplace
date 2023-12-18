package com.kenazalfizza.farmersmarketplace.model;

import com.google.gson.annotations.SerializedName;

public class CartStore {
    @SerializedName("store_id")
    String storeId;

    @SerializedName("store_name")
    String storeName;

    @SerializedName("store_location")
    String storeLocation;

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

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    @Override
    public String toString() {
        return "CartStoreModel{" +
                "storeId='" + storeId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeLocation='" + storeLocation + '\'' +
                '}';
    }

}
