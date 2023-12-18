package com.kenazalfizza.farmersmarketplace.api.response.cart;

import com.google.gson.annotations.SerializedName;

public class CartStore {
    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("location")
    String location;

    @SerializedName("available")
    boolean available;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "CartStore{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", available=" + available +
                '}';
    }
}
