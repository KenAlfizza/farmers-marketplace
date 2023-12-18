package com.kenazalfizza.farmersmarketplace.store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.StoreResponse;
import com.kenazalfizza.farmersmarketplace.session.current.UserStoreCurrent;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreDetailsActivity extends AppCompatActivity {
    String baseURL = "http://192.168.1.5/";
    public static String storeId = "";
    private TextView tv_storeName, tv_storePhone, tv_storeEmail, tv_storeAddress, tv_storeDescription, tv_storeNameContact;
    private LinearLayout lay_navBack;
    private Button btn_goto_updateStore;
    private ImageView iv_productImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);

        elementsFindViewById();

        requestStoreDetails(storeId);


        lay_navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_goto_updateStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StoreUpdateActivity.class);
                startActivity(intent);
            }
        });

    }

    public static void setStoreId(String storeId) {
        StoreDetailsActivity.storeId = storeId;
    }

    public void onResume() {
        super.onResume();
        requestStoreDetails(storeId);
    }

    public void elementsFindViewById() {
        // Find View By Id for TextView
        tv_storeName = (TextView) findViewById(R.id.tv_store_name);
        tv_storePhone = (TextView) findViewById(R.id.tv_store_contact_phone);
        tv_storeEmail= (TextView) findViewById(R.id.tv_store_contact_email);
        tv_storeAddress = (TextView) findViewById(R.id.tv_store_address);
        tv_storeDescription = (TextView) findViewById(R.id.tv_store_description);
        iv_productImage = (ImageView) findViewById(R.id.iv_store_image);


        lay_navBack = (LinearLayout) findViewById(R.id.li_nav_back);
        btn_goto_updateStore = (Button) findViewById(R.id.btn_goto_update_store);

    }

    public void requestStoreDetails (String storeId) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<StoreResponse> storeResponseCall = apiRequestInterface.requestStoreByStoreId(storeId);
        storeResponseCall.enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                StoreResponse storeResponse = response.body();
                setCurrentUserStoreDetails(storeResponse);
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connection failure. Try again later ...", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setCurrentUserStoreDetails(StoreResponse storeResponse) {
        tv_storeName.setText(storeResponse.getStoreName());
        tv_storePhone.setText(storeResponse.getStorePhone());
        tv_storeEmail.setText(storeResponse.getStoreEmail());
        tv_storeAddress.setText(storeResponse.getStoreAddress());
        tv_storeDescription.setText(storeResponse.getStoreDescription());

        String id = storeResponse.getStoreId();
        String uri = "marketplace/store/res/";
        String url = baseURL + uri + id + ".png";

        Picasso.get()
                .load(url)
                .fit()
                .centerCrop()
                .into(iv_productImage);

        if (storeResponse.getStoreId().equals(UserStoreCurrent.getStoreId())) {
            btn_goto_updateStore.setVisibility(View.VISIBLE);
        } else {
            btn_goto_updateStore.setVisibility(View.GONE);
        }
    }
}