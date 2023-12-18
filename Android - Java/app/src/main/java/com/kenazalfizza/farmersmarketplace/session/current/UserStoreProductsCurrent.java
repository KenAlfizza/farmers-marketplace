package com.kenazalfizza.farmersmarketplace.session.current;

import com.kenazalfizza.farmersmarketplace.model.ProductListItem;

import java.util.ArrayList;

public class UserStoreProductsCurrent {
    public static ArrayList<ProductListItem> productListItems = new ArrayList<>();

    public static ArrayList<ProductListItem> getProductListItems() {
        return productListItems;
    }

    public static void setProductListItems(ArrayList<ProductListItem> productListItems) {
        UserStoreProductsCurrent.productListItems = productListItems;
    }


}
