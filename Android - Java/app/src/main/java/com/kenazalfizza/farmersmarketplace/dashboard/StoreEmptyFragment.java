package com.kenazalfizza.farmersmarketplace.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.store.StoreRegisterActivity;


public class StoreEmptyFragment extends Fragment {
    Button btn_gotoRegisterStore;

    public StoreEmptyFragment() {
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
        View view = inflater.inflate(R.layout.fragment_store_empty, container, false);

        btn_gotoRegisterStore = view.findViewById(R.id.btn_goto_register_store);

        btn_gotoRegisterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StoreRegisterActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}