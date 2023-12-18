package com.kenazalfizza.farmersmarketplace.model;

import com.google.gson.annotations.SerializedName;

public class ProductListItem {
    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("price")
    String price;

    @SerializedName("type")
    String type;

    @SerializedName("city")
    String location;

    @Override
    public String toString() {
        return "ProductListItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }
}