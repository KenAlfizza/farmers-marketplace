package com.kenazalfizza.farmersmarketplace.account;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.cart.CartProduct;
import com.kenazalfizza.farmersmarketplace.api.response.cart.CartResponse;
import com.kenazalfizza.farmersmarketplace.session.current.UserCartCurrent;
import com.kenazalfizza.farmersmarketplace.session.current.UserCurrent;
import com.kenazalfizza.farmersmarketplace.view.RecyclerViewCartProductAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartProductActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewCartProductAdapter recyclerViewProductListAdapter;
    ArrayList<CartProduct> productListAll = new ArrayList<>();
    ArrayList<CartProduct> productList = new ArrayList<>();

    static String storeId;

    boolean isLoading = false;
    boolean allLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_store);

        getProducts();



        elementsFindViewById();

        initAdapter(productList);
    }

    private void getProducts() {
        productListAll = UserCartCurrent.getCartProducts();
        for (CartProduct p : productListAll) {
            if (p.getStoreId().equals(storeId)) {
                productList.add(p);
            }
        }
        Log.d(null, productList.toString());
    }


    protected void elementsFindViewById() {
        recyclerView = findViewById(R.id.rv_cart_store);
    }

    private void retrieveCart() {
        int start = 1; // Start index of item loaded
        int end = 5; // The end index of the item
        int maxPopulatedLength = end-start + 1; // Length of items loaded

        //Note: The index of item starts at 1 NOT 0 for DataBase purposes

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
                productList = cartResponse.getCartProducts();
                initAdapter(productList);
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.d(null, t.toString());
                Log.d(null, "failed");
            }
        });

    }

    private void initAdapter(ArrayList<CartProduct> productList) {
        // Initialising the adapter for recyclerView
        Log.d(null, productList.toString());
        recyclerViewProductListAdapter = new RecyclerViewCartProductAdapter(productList, storeId);
        recyclerView.setAdapter(recyclerViewProductListAdapter);
    }

    public static void setStoreId(String storeId) {
        CartProductActivity.storeId = storeId;
    }


}