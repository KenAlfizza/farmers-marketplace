package com.kenazalfizza.farmersmarketplace.api.response.cart;

import com.google.gson.annotations.SerializedName;
import com.kenazalfizza.farmersmarketplace.model.Product;

public class CartProduct {

    @SerializedName("store_id")
    String store_id;

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("price")
    int price;

    @SerializedName("quantity")
    int quantity;

    @SerializedName("stock")
    int stock;

    @SerializedName("available")
    Boolean available;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getStoreId() {
        return store_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStock() {
        return stock;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "CartProduct{" +
                "store_id='" + store_id + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity='" + quantity + '\'' +
                ", stock='" + stock + '\'' +
                ", available=" + available +
                '}';
    }

    public CartProduct toCartProduct(Product product) {
        store_id = product.getStoreId();
        id = product.getProductId();
        name = product.getProductName();
        price = product.getProductPrice();
        quantity = 1;
        stock = product.getProductStock();
        available = true;
        return this;
    }
}
