package com.kenazalfizza.farmersmarketplace.model;

import com.google.gson.annotations.SerializedName;
import com.kenazalfizza.farmersmarketplace.api.response.ProductResponse;
import com.kenazalfizza.farmersmarketplace.session.current.UserCurrent;

public class Product {

    @SerializedName("id")
    String productId;

    @SerializedName("name")
    String productName;

    @SerializedName("description")
    String productDescription;

    @SerializedName("type")
    String productType;

    @SerializedName("price")
    int productPrice;

    @SerializedName("stock")
    int productStock;

    @SerializedName("store_id")
    String storeId;

    @SerializedName("store_name")
    String storeName;

    @SerializedName("store_city")
    String storeLocation;

    @SerializedName("user_id")
    String userId;

    String productUnit;

    public Product() {

    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

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

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProduct(ProductResponse productResponse) {
        this.setProductId(productResponse.getProductId());
        this.setProductName(productResponse.getProductName());
        this.setProductType(productResponse.getProductType());
        this.setProductPrice(productResponse.getProductPrice());
        this.setProductStock(productResponse.getProductStock());
        this.setProductDescription(productResponse.getProductDescription());
        this.setStoreId(productResponse.getStoreId());
        this.setStoreName(productResponse.getStoreName());
        this.setStoreLocation(productResponse.getStoreLocation());
        this.setUserId(UserCurrent.getUserId());
    }
}
