package com.kenazalfizza.farmersmarketplace.model;

import com.google.gson.annotations.SerializedName;

public class OrderModel {
    @SerializedName("order_id")
    String orderId;

    @SerializedName("user_id")
    String userId;

    @SerializedName("product_id")
    String productId;

}
