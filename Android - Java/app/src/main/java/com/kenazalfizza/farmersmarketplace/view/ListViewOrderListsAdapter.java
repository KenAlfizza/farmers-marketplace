package com.kenazalfizza.farmersmarketplace.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kenazalfizza.farmersmarketplace.R;


public class ListViewOrderListsAdapter extends BaseAdapter {
    Context context;
    String[] orderIds;
    String[] orderStatuses;
    LayoutInflater inflater;



    public ListViewOrderListsAdapter(Context applicationContext, String[] orderId, String[] orderStatus) {
        this.context = context;
        this.orderIds = orderId;
        this.orderStatuses = orderStatus;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return orderIds.length;
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
        TextView orderId = (TextView)  view.findViewById(R.id.order_id);
        TextView orderStatus = (TextView) view.findViewById(R.id.order_status);

        orderId.setText("Order ID " + orderIds[i]);
        orderStatus.setText(orderStatuses[i]);
        return view;
    }

}
