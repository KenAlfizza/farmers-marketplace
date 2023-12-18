package com.kenazalfizza.farmersmarketplace.store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.model.DeliveryService;
import com.kenazalfizza.farmersmarketplace.model.Product;
import com.kenazalfizza.farmersmarketplace.model.StoreModel;
import com.kenazalfizza.farmersmarketplace.model.UserModel;

import java.util.ArrayList;

public class StoreDeliveryActivity extends AppCompatActivity {
    ArrayList<DeliveryService> deliveryServices = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_delivery);

    }
}
/*
    public ArrayList<DeliveryService> requestDeliveryServices() {
        int start = 1; // Start index of item loaded
        int end = 10; // The end index of the item
        int maxPopulatedLength = end-start + 1; // Length of items loaded

        //Note: The index of item starts at 1 NOT 0 for DataBase purposes

        String[] field = {"user_id", "start", "end"};
        String[] data = {StoreModel.getStoreId(), String.valueOf(start), String.valueOf(end)};
        String result = "";

        TransferData getProductList = new TransferData(
                "http://192.168.1.5/class_test/products/productRetrieveList.php?method=user_limit",
                "POST",
                field, data);
        if (getProductList.startTransfer()) {
            if (getProductList.onComplete()) {
                result = getProductList.getResult();
                try {
                    Gson gson = new Gson(); // Instantiation of GSON object
                    deliveryServices.add(gson.fromJson(result, DeliveryService.class));

                } catch (Throwable e){
                    allLoaded = true;
                    Log.d(null, result);
                }


        return ;
    }

    private void populateData() {
        // Populate the recyclerView with items

        int start = 1; // Start index of item loaded
        int end = 10; // The end index of the item
        int maxPopulatedLength = end-start + 1; // Length of items loaded

        //Note: The index of item starts at 1 NOT 0 for DataBase purposes

        String[] field = {"user_id", "start", "end"};
        String[] data = {UserModel.getUserId(), String.valueOf(start), String.valueOf(end)};
        String result = "";
        TransferData getProductList = new TransferData(
                "http://192.168.1.5/class_test/products/productRetrieveList.php?method=user_limit",
                "POST",
                field, data);
        if (getProductList.startTransfer()) {
            if (getProductList.onComplete()) {
                result = getProductList.getResult();
                try {
                    Gson gson = new Gson(); // Instantiation of GSON object
                    Product[] products = gson.fromJson(result, Product[].class);
                    // Converting the JSON Object Array into Product Object Array

                    for (Product product : products) {
                        rowsArrayIdList.add(product.getProductId());
                        rowsArrayItemList.add(product.getProductName());
                        rowsArrayLocationList.add(product.getStoreLocation());
                        rowsArrayPriceList.add(String.valueOf(product.getProductPrice()));
                    }
                    if (products.length < maxPopulatedLength) {
                        allLoaded = true;
                    }
                } catch (Throwable e){
                    allLoaded = true;
                    Log.d(null, result);
                }
            }
        }
    }
}

 */