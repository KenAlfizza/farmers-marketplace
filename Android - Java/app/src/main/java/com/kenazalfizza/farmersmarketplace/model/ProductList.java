package com.kenazalfizza.farmersmarketplace.model;

import com.google.gson.annotations.SerializedName;

public class ProductList {

    @SerializedName("product_name")
    String productName;

    @SerializedName("product_price")
    int productPrice;

    @SerializedName("product_image_url")
    String productImageURL;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public void setProductImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
    }
}
