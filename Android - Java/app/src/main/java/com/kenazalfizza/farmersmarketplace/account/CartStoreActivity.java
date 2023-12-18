package com.kenazalfizza.farmersmarketplace.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.StatusResponse;
import com.kenazalfizza.farmersmarketplace.api.response.cart.CartResponse;
import com.kenazalfizza.farmersmarketplace.session.current.UserCartCurrent;
import com.kenazalfizza.farmersmarketplace.session.current.UserCurrent;
import com.kenazalfizza.farmersmarketplace.api.response.cart.CartStore;
import com.kenazalfizza.farmersmarketplace.view.RecyclerViewCartStoreAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartStoreActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewCartStoreAdapter recyclerViewStoreListAdapter;
    ArrayList<CartStore> storeList = new ArrayList<>();

    boolean isLoading = false;
    boolean allLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_store);

        elementsFindViewById();
        retrieveCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateCart();
    }

    protected void elementsFindViewById() {
        recyclerView = findViewById(R.id.rv_cart_store);
    }

    private void retrieveCart() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<CartResponse> cartResponseCall = apiRequestInterface.requestCart(UserCurrent.getUserId());

        cartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                CartResponse cartResponse = response.body();
                assert cartResponse != null;
                UserCartCurrent.setCartStores(cartResponse.getCartStores());
                UserCartCurrent.setCartProducts(cartResponse.getCartProducts());
                initAdapter();
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.d(null, t.toString());
            }
        });

    }

    private void updateCart() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);
        String cartProductsJson = gson.toJson(UserCartCurrent.getCartProducts());
        String cartStoresJson = gson.toJson(UserCartCurrent.getCartStores());

        Log.d(null, cartProductsJson);
        Log.d(null, cartStoresJson);
        Call<StatusResponse> statusResponseCall = apiRequestInterface.updateCart(UserCurrent.getUserId(), cartStoresJson, cartProductsJson);
        statusResponseCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                StatusResponse statusResponse = response.body();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Log.d(null, t.toString());
            }
        });
    }

    private void initAdapter() {
        // Initialising the adapter for recyclerView
        ArrayList<CartStore> storeList = UserCartCurrent.getCartStores();
        recyclerViewStoreListAdapter = new RecyclerViewCartStoreAdapter(storeList);
        recyclerView.setAdapter(recyclerViewStoreListAdapter);

        Log.d(null, recyclerView.getAdapter().toString());
    }
}