package com.kenazalfizza.farmersmarketplace.api.response.cart;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CartResponse {

    @SerializedName("store")
    ArrayList<CartStore> cartStores = new ArrayList<>();


    @SerializedName("product")
    ArrayList<CartProduct> cartProducts = new ArrayList<>();


    public ArrayList<CartStore> getCartStores() {
        return cartStores;
    }

    public ArrayList<CartProduct> getCartProducts() {
        return cartProducts;
    }
}

