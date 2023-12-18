package com.kenazalfizza.farmersmarketplace.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.orders.OrderDetailsActivity;
import com.kenazalfizza.farmersmarketplace.view.ListViewOrderListsAdapter;


public class OrderListFragment extends Fragment {
    ListView list_pending_order;
    ListView list_history_order;
    String[] orderIds = {"2","4","1"};
    String[] orderStatuses = {"Pending","Confirmation","Shipping"};
    String[] orderDates = {"8/2/2022", "2/5/2020", "5/6/2021"};

    public OrderListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        elementsFindViewById(view);
        listSetAdapter();



        list_pending_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
                startActivity(intent);
                Log.d(null, "Clicked");
            }
        });

        list_history_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
            }
        });
        return view;


    }

    public void elementsFindViewById(View view) {
        list_pending_order = view.findViewById(R.id.list_orders_pending);
        list_history_order = view.findViewById(R.id.list_orders_history);
    }

    public void listSetAdapter() {
        ListViewOrderListsAdapter listViewOrderPendingListsAdapter = new ListViewOrderListsAdapter(getContext(), orderIds, orderStatuses);
        list_pending_order.setAdapter(listViewOrderPendingListsAdapter);

        ListViewOrderListsAdapter listViewOrderHistoryListsAdapter = new ListViewOrderListsAdapter(getContext(), orderIds, orderDates);
        list_history_order.setAdapter(listViewOrderHistoryListsAdapter);
    }
}