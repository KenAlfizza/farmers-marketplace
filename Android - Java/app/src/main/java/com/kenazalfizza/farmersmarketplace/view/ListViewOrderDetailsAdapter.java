package com.kenazalfizza.farmersmarketplace.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kenazalfizza.farmersmarketplace.R;

import java.util.ArrayList;


public class ListViewOrderDetailsAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> orderProductIds = new ArrayList<>();
    ArrayList<String> orderProductNames = new ArrayList<>();
    ArrayList<Integer> orderProductQuantities = new ArrayList<>();
    ArrayList<Integer> orderProductUnitPrices = new ArrayList<>();
    ArrayList<Integer> orderProductTotalPrices = new ArrayList<>();

    LayoutInflater inflater;



    public ListViewOrderDetailsAdapter(Context applicationContext, ArrayList<String> orderProductIds, ArrayList<String> orderProductNames, ArrayList<Integer> orderProductQuantities, ArrayList<Integer> orderProductUnitPrice, ArrayList<Integer> orderProductTotalPrice) {
        this.context = context;
        this.orderProductIds = orderProductIds;
        this.orderProductNames = orderProductNames;
        this.orderProductQuantities = orderProductQuantities;
        this.orderProductUnitPrices = orderProductUnitPrice;
        this.orderProductTotalPrices = orderProductTotalPrice;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return orderProductIds.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_order_item, null);
        TextView orderProductName = (TextView)  view.findViewById(R.id.order_product_name);
        TextView orderProductQuantity = (TextView)  view.findViewById(R.id.order_product_quantity);
        TextView orderProductUnitPrice = (TextView)  view.findViewById(R.id.order_product_unit_price);
        TextView orderProductTotalPrice = (TextView)  view.findViewById(R.id.order_product_total_price);

        orderProductName.setText(orderProductNames.get(i));
        orderProductQuantity.setText(orderProductQuantities.get(i));
        orderProductUnitPrice.setText(orderProductUnitPrices.get(i));
        orderProductTotalPrice.setText(orderProductTotalPrices.get(i));

        return view;
    }

}
