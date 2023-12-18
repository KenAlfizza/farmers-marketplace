package com.kenazalfizza.farmersmarketplace.model;

public class DeliveryService {

    String storeId;

    String deliveryId;

    String deliveryCourier;

    String deliveryType;

    String deliveryPrice;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDeliveryCourier() {
        return deliveryCourier;
    }

    public void setDeliveryCourier(String deliveryCourier) {
        this.deliveryCourier = deliveryCourier;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
}
