package com.kenazalfizza.farmersmarketplace.store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.ProductListResponse;
import com.kenazalfizza.farmersmarketplace.session.current.UserStoreCurrent;
import com.kenazalfizza.farmersmarketplace.session.current.UserStoreProductsCurrent;
import com.kenazalfizza.farmersmarketplace.model.ProductListItem;
import com.kenazalfizza.farmersmarketplace.view.RecyclerViewProductListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreProductsActivity extends AppCompatActivity {
    // Variable declaration
    RecyclerView recyclerView;
    RecyclerViewProductListAdapter recyclerViewProductListAdapter;
    ArrayList<ProductListItem> rowsArrayProductList = new ArrayList<>();

    boolean isLoading = false;
    boolean allLoaded = false;

    ImageButton btn_goto_product_register;
    static String storeId;
    String baseURL = "http://192.168.1.5/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_products);

        elementsFindViewById();

        getProductList();

        btn_goto_product_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StoreProductRegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public static void setStoreId(String storeId) {
        StoreProductsActivity.storeId = storeId;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductList();
        initAdapter(UserStoreProductsCurrent.getProductListItems());
    }

    protected void elementsFindViewById() {
        btn_goto_product_register = findViewById(R.id.btn_goto_register_product);
        recyclerView = findViewById(R.id.rv_product);

        if (!storeId.equals(UserStoreCurrent.getStoreId())) {
            btn_goto_product_register.setVisibility(View.GONE);
        }
    }

    private void getProductList() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<ProductListResponse> productStoreListResponseCall = apiRequestInterface.requestStoreProducts(storeId);

        productStoreListResponseCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                ProductListResponse productListResponse = response.body();
                assert productListResponse != null;
                UserStoreProductsCurrent.setProductListItems(productListResponse.getProductList());
                Log.d("AAAA", UserStoreProductsCurrent.getProductListItems().toString());
                initAdapter(UserStoreProductsCurrent.getProductListItems());
            }
            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Log.d(null, t.toString());
            }
        });
    }

    private void initAdapter(ArrayList<ProductListItem> rowsArrayProductList) {
        // Initialising the adapter for recyclerView
        recyclerViewProductListAdapter = new RecyclerViewProductListAdapter(rowsArrayProductList, baseURL);
        recyclerView.setAdapter(recyclerViewProductListAdapter);
    }
}