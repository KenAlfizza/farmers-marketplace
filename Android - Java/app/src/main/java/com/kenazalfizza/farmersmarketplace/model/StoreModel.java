package com.kenazalfizza.farmersmarketplace.model;

import android.util.Log;

public class StoreModel {

    public static String storeId, storeName, storeEmail, storePhone, storeProvince, storeCity, storePostcode, storeAddress, storeDescription;

    // region Getters and Setters

    public static String getStoreId() {
        return storeId;
    }

    public static void setStoreId(String userId) {
        StoreModel.storeId = userId;
    }

    public static String getStoreName() {
        return storeName;
    }

    public static void setStoreName(String storeName) {
        StoreModel.storeName = storeName;
    }

    public static String getStoreEmail() {
        return storeEmail;
    }

    public static void setStoreEmail(String storeEmail) {
        StoreModel.storeEmail = storeEmail;
    }

    public static String getStorePhone() {
        return storePhone;
    }

    public static void setStorePhone(String storePhone) {
        StoreModel.storePhone = storePhone;
    }

    public static String getStoreProvince() {
        return storeProvince;
    }

    public static void setStoreProvince(String storeProvince) {
        StoreModel.storeProvince = storeProvince;
    }

    public static String getStoreCity() {
        return storeCity;
    }

    public static void setStoreCity(String storeCity) {
        StoreModel.storeCity = storeCity;
    }

    public static String getStorePostcode() {
        return storePostcode;
    }

    public static void setStorePostcode(String storePostcode) {
        StoreModel.storePostcode = storePostcode;
    }

    public static String getStoreAddress() {
        return storeAddress;
    }

    public static void setStoreAddress(String storeAddress) {
        StoreModel.storeAddress = storeAddress;
    }

    public static String getStoreDescription() {
        return storeDescription;
    }

    public static void setStoreDescription(String storeDescription) {
        StoreModel.storeDescription = storeDescription;
    }
    // endregion

    /*
    public static void retrieveStoreDetails(String userId) {
        String[] field = new String[1];
        field[0] = "user_id";
        //Creating array for data
        String[] data = new String[1];
        data[0] = userId;

        TransferData storeRetrieve = new TransferData("http://192.168.1.5/class_test/stores/storeRetrieve.php", "POST", field, data);
        if (storeRetrieve.startTransfer()) {
            if (storeRetrieve.onComplete()) {
                String _result = storeRetrieve.getResult();
                Log.d(null, _result);
                boolean error = false;
                switch (_result) {
                    case "not_found":
                        error = true;
                        break;
                }

                if (!error) {
                    String[] result = _result.split(";");
                    if (UserModel.getUserId().equals(result[0])) {
                        storeId = result[1];
                        storeName = result[2];
                        storeEmail = result[3];
                        storePhone = result[4];
                        storeProvince = result[5];
                        storeCity = result[6];
                        storePostcode = result[7];
                        storeAddress = result[8];
                        storeDescription = result[9];
                    }
                }

            }
        }
    }

     */
}
