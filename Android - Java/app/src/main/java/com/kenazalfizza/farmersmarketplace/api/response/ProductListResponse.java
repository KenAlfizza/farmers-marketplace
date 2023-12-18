package com.kenazalfizza.farmersmarketplace.api.response;

import com.google.gson.annotations.SerializedName;
import com.kenazalfizza.farmersmarketplace.model.ProductListItem;

import java.util.ArrayList;

public class ProductListResponse {

    @SerializedName("products")
    ArrayList<ProductListItem> productList;

    @Override
    public String toString() {
        return "ProductListResponse{" +
                "productList=" + productList +
                '}';
    }

    public ArrayList<ProductListItem> getProductList() {
        return productList;
    }

    public int getProductListSize() {
        return productList.size();
    }

    public ProductListItem getProductListItem(int index) {
        return productList.get(index);
    }
}
