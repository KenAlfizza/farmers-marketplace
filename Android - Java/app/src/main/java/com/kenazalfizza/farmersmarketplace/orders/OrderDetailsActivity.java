package com.kenazalfizza.farmersmarketplace.orders;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.view.ListViewOrderDetailsAdapter;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {
    static String orderId = "";
    ListView list_product;

    ArrayList<String> orderProductIds = new ArrayList<>();
    ArrayList<String> orderProductNames = new ArrayList<>();

    ArrayList<Integer> orderProductQuantity = new ArrayList<>();

    ArrayList<Integer> orderProductUnitPrice = new ArrayList<>();
    ArrayList<Integer> orderProductTotalPrice = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

    }
    protected void setArrayLists() {
        orderProductIds.add("pr1");
        orderProductIds.add("pr6");
        orderProductIds.add("pr3");
        orderProductIds.add("pr4");

        orderProductNames.add("Products1");
        orderProductNames.add("Products6");
        orderProductNames.add("Products3");
        orderProductNames.add("Products4");

        orderProductQuantity.add(1);
        orderProductQuantity.add(2);
        orderProductQuantity.add(3);
        orderProductQuantity.add(11);

        orderProductUnitPrice.add(200);
        orderProductUnitPrice.add(700);
        orderProductUnitPrice.add(1000);
        orderProductUnitPrice.add(2000);
    }

    protected void calculateProductTotalPrices() {
        for (int i=0; i<orderProductIds.size(); i++) {
            orderProductTotalPrice.add(orderProductUnitPrice.get(i)*orderProductQuantity.get(i));
        }
    }

    public void elementsFindViewById(View view) {
        list_product = view.findViewById(R.id.list_order_product);
    }
    public void listSetAdapter() {
        ListViewOrderDetailsAdapter listViewOrderDetailsAdapter = new ListViewOrderDetailsAdapter(getApplicationContext(), orderProductIds, orderProductNames, orderProductQuantity, orderProductUnitPrice, orderProductTotalPrice);
        list_product.setAdapter(listViewOrderDetailsAdapter);
    }
}