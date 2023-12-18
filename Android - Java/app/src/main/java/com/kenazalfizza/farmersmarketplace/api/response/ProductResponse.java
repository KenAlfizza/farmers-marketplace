package com.kenazalfizza.farmersmarketplace.api.response;

import com.google.gson.annotations.SerializedName;
import com.kenazalfizza.farmersmarketplace.model.Product;

public class ProductResponse {

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
    String storeCity;

    String productUnit;

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
        return storeCity;
    }

    public void setStoreLocation(String storeCity) {
        this.storeCity = storeCity;
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

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public Product toProduct() {
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductType(productType);
        product.setProductPrice(productPrice);
        product.setProductStock(productStock);
        product.setStoreId(storeId);
        product.setProductStock(productStock);
        product.setProductStock(productStock);
        return product;
    }

}
