package com.kenazalfizza.farmersmarketplace.session.current;

import com.kenazalfizza.farmersmarketplace.api.response.cart.CartProduct;
import com.kenazalfizza.farmersmarketplace.api.response.cart.CartStore;

import java.util.ArrayList;

public class UserCartCurrent {

    static ArrayList<CartStore> cartStores = new ArrayList<>();
    static ArrayList<CartProduct> cartProducts = new ArrayList<>();

    public static void setCartStores(ArrayList<CartStore> cartStores) {
        UserCartCurrent.cartStores = cartStores;
    }

    public static ArrayList<CartStore> getCartStores() {
        return cartStores;
    }

    public static void setCartProducts(ArrayList<CartProduct> cartProducts) {
        UserCartCurrent.cartProducts = cartProducts;
    }

    public static ArrayList<CartProduct> getCartProducts() {
        return cartProducts;
    }
}
