package com.kenazalfizza.farmersmarketplace.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


import java.util.ArrayList;

public class Cart {
    public static ArrayList<CartProduct> productList = new ArrayList<>();
    public static ArrayList<CartStore> storeList = new ArrayList<>();

    // POPULATE ENTRY
    public static void populate() {
        CartStore cartStore0 = new CartStore();
        cartStore0.storeId = "st2190421";
        cartStore0.storeName = "Store 1";
        cartStore0.storeLocation = "Jakarta";
        addStore(cartStore0);

        CartStore cartStore1 = new CartStore();
        cartStore1.storeId = "st13413231";
        cartStore1.storeName = "Store 2";
        cartStore1.storeLocation = "Purwokerto";
        addStore(cartStore1);

        CartProduct cartProduct0 = new CartProduct();
        cartProduct0.storeId = "st2190421";
        cartProduct0.productId = "pr3195813";
        cartProduct0.productName = "Product 1";
        cartProduct0.productQuantity = 10;
        cartProduct0.productPrice = 10000;
        addProduct(cartProduct0);

        CartProduct cartProduct1 = new CartProduct();
        cartProduct1.storeId = "st13413231";
        cartProduct1.productId = "pr44124214";
        cartProduct1.productName = "Product 2";
        cartProduct1.productQuantity = 10;
        cartProduct1.productPrice = 10000;
        addProduct(cartProduct1);

        CartProduct cartProduct2 = new CartProduct();
        cartProduct2.storeId = "st13413231";
        cartProduct2.productId = "pr124124012";
        cartProduct2.productName = "Product 3";
        cartProduct2.productQuantity = 10;
        cartProduct2.productPrice = 10000;
        addProduct(cartProduct2);
    }

    // GET LIST SIZE
    public int getProductListSize() {
        return productList.size();
    }
    public int getStoreListSize() {
        return storeList.size();
    }


    // ADD ENTRY
    public static void addProduct(CartProduct product) {
        for (CartProduct p: productList) {
            if (p.productId.equals(product.productId)) {
                addProductQuantity(product.productId, product.productQuantity);
                return;
            }
        }
        productList.add(product);
    }
    public static void addStore(CartStore store) {
        for (CartStore s: storeList) {
            if (s.storeId.equals(store.storeId)) {
                return;
            }
        }
        storeList.add(store);
    }

    // GET ENTRY
    public static CartStore getStore(String store_id) {
        for (CartStore s : storeList) {
            if (s.storeId.equals(store_id)) {
                return s;
            }
        }
        return null;
    }

    public static String getStoreName(String store_id) {
        for (CartStore s : storeList) {
            if (s.storeId.equals(store_id)) {
                return s.storeName;
            }
        }
        return "default";
    }

    public static String getStoreLocation(String store_id) {
        for (CartStore s : storeList) {
            if (s.storeId.equals(store_id)) {
                return s.storeLocation;
            }
        }
        return "";
    }

    public static CartProduct getProduct(String product_id) {
        for (CartProduct p : productList) {
            if (p.productId.equals(product_id)) {
                return p;
            }
        }
        return null;
    }

    public static ArrayList<CartProduct> getProducts(String store_id) {
        ArrayList<CartProduct> pList = new ArrayList<>();
        for (CartProduct p : productList) {
            if (p.storeId.equals(store_id)) {
                pList.add(p);
            }
        }
        return pList;
    }

    public static String getProductName(String product_id) {
        for (CartProduct p : productList) {
            if (p.storeId.equals(product_id)) {
                return p.productName;
            }
        }
        return "";
    }

    public int getProductQuantity(String product_id) {
        for (CartProduct p : productList) {
            if (p.productId.equals(product_id)) {
                return p.productQuantity;
            }
        }
        return -1;
    }

    // QUANTITY CHANGE
    public static void addProductQuantity(String product_id, int quantity) {
        for (CartProduct p : productList) {
            if (p.productId.equals(product_id)) {
                p.productQuantity += quantity;
                return;
            }
        }
    }

    public static void minusProductQuantity(String product_id, int quantity) {
        for (CartProduct p : productList) {
            if (p.productId.equals(product_id)) {
                p.productQuantity -= quantity;
            }
        }
    }
    // REMOVE PRODUCTS
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void removeProduct(String product_id) {
        String s_id = getProduct(product_id).storeId;
        productList.removeIf(p -> p.productId.equals(product_id));
        if (getProducts(s_id).size() == 0) {
            storeList.removeIf(s -> s.storeId.equals(s_id));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void removeStore(String store_id) {
        storeList.removeIf(s -> s.storeId.equals(store_id));
        productList.removeIf(p -> p.storeId.equals(store_id));
    }


}