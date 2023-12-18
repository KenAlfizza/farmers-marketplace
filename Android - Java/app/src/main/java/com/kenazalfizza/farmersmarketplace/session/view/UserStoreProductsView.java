package com.kenazalfizza.farmersmarketplace.session.view;

import com.kenazalfizza.farmersmarketplace.model.ProductListItem;

import java.util.ArrayList;

public class UserStoreProductsView {
    public static ArrayList<ProductListItem> productListItems = new ArrayList<>();

    public static ArrayList<ProductListItem> getProductListItems() {
        return productListItems;
    }

    public static void setProductListItems(ArrayList<ProductListItem> productListItems) {
        UserStoreProductsView.productListItems = productListItems;
    }
}
