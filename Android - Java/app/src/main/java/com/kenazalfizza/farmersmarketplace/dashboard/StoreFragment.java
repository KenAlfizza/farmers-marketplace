package com.kenazalfizza.farmersmarketplace.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.activity.ProductActivity;
import com.kenazalfizza.farmersmarketplace.store.StoreDetailsActivity;
import com.kenazalfizza.farmersmarketplace.store.StoreProductRegisterActivity;
import com.kenazalfizza.farmersmarketplace.store.StoreProductsActivity;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.ProductListResponse;
import com.kenazalfizza.farmersmarketplace.api.response.StoreResponse;
import com.kenazalfizza.farmersmarketplace.session.current.UserCurrent;
import com.kenazalfizza.farmersmarketplace.session.current.UserStoreCurrent;
import com.kenazalfizza.farmersmarketplace.session.current.UserStoreProductsCurrent;
import com.kenazalfizza.farmersmarketplace.model.ProductListItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreFragment extends Fragment {
    String baseURL = "http://192.168.1.5/";
    String productResURL = baseURL + "marketplace/product/res/";
    String storeResURL = baseURL + "marketplace/store/res/";

    String storeId = UserStoreCurrent.getStoreId();
    String productPreview1Id, productPreview2Id;
    TextView tv_storeName, tv_storePhone, tv_storeLocation;
    LinearLayout li_gotoStoreDetails, li_gotoStoreProducts;
    LinearLayout li_store_product_preview, li_store_product_preview_empty;
    LinearLayout li_productTest;
    CardView cv_product_preview_1, cv_product_preview_2;

    TextView tv_product_preview_1_name, tv_product_preview_2_name;
    TextView tv_product_preview_1_price, tv_product_preview_2_price;

    ImageView iv_product_preview_1_image, iv_product_preview_2_image;
    ImageView iv_register_product;
    ImageView iv_storeImage;

    StoreEmptyFragment storeEmptyFragment = new StoreEmptyFragment();

    public StoreFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestStoreDetails();
        getProductList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        elementsFindViewById(view);
        requestStoreDetails();
        getProductList();
        li_gotoStoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StoreDetailsActivity.class);
                StoreDetailsActivity.setStoreId(UserStoreCurrent.getStoreId());
                startActivity(intent);
            }
        });
        li_gotoStoreProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StoreProductsActivity.class);
                StoreProductsActivity.setStoreId(UserStoreCurrent.getStoreId());
                startActivity(intent);
            }
        });
        cv_product_preview_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductActivity.setProductId(productPreview1Id);
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                startActivity(intent);
            }
        });

        cv_product_preview_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductActivity.setProductId(productPreview2Id);
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                startActivity(intent);
            }
        });

        iv_register_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StoreProductRegisterActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void elementsFindViewById(View view) {
        // Find View By Id for TextView
        tv_storeName = (TextView) view.findViewById(R.id.tv_store_name);
        tv_storePhone = (TextView) view.findViewById(R.id.tv_store_phone);
        tv_storeLocation = (TextView) view.findViewById(R.id.tv_store_location);
        li_gotoStoreDetails = (LinearLayout) view.findViewById(R.id.li_goto_store_details);
        li_gotoStoreProducts = (LinearLayout) view.findViewById(R.id.li_goto_store_products);
        cv_product_preview_1 = (CardView) view.findViewById(R.id.cv_product_preview_1);
        cv_product_preview_2 = (CardView) view.findViewById(R.id.cv_product_preview_2);

        li_store_product_preview = (LinearLayout) view.findViewById(R.id.li_store_product_preview);
        li_store_product_preview_empty = (LinearLayout) view.findViewById(R.id.li_store_product_preview_empty);

        tv_product_preview_1_name = (TextView) view.findViewById(R.id.tv_product_preview_1_name);
        tv_product_preview_2_name = (TextView) view.findViewById(R.id.tv_product_preview_2_name);

        tv_product_preview_1_price = (TextView) view.findViewById(R.id.tv_product_preview_1_price);
        tv_product_preview_2_price = (TextView) view.findViewById(R.id.tv_product_preview_2_price);

        iv_product_preview_1_image = (ImageView) view.findViewById(R.id.iv_product_preview_1_image);
        iv_product_preview_2_image = (ImageView) view.findViewById(R.id.iv_product_preview_2_image);

        iv_register_product = (ImageView) view.findViewById(R.id.iv_register_product);
        iv_storeImage = (ImageView) view.findViewById(R.id.iv_store_image);

    }
    public void requestStoreDetails() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<StoreResponse> storeResponseCall = apiRequestInterface.requestStoreByUserId(UserCurrent.getUserId());
        storeResponseCall.enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                StoreResponse storeResponse = response.body();
                setCurrentUserStore(storeResponse);
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), "Connection failure. Try again later ...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProductList() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<ProductListResponse> productStoreListResponseCall = apiRequestInterface.requestStoreProducts(UserStoreCurrent.getStoreId());

        productStoreListResponseCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                ProductListResponse productListResponse = response.body();
                assert productListResponse != null;
                UserStoreProductsCurrent.setProductListItems(productListResponse.getProductList());
                Log.d("AAAA", UserStoreProductsCurrent.getProductListItems().toString());
                setProductPreview();
            }
            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Log.d(null, t.toString());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    protected void setProductPreview() {
        ArrayList<ProductListItem> productListItems =  UserStoreProductsCurrent.getProductListItems();
        int size = productListItems.size();

        if (size > 0) {
            li_store_product_preview_empty.setVisibility(View.GONE);
            ProductListItem productPreview1 = productListItems.get(size - 1);
            productPreview1Id = productPreview1.getId();
            tv_product_preview_1_name.setText(productPreview1.getName());
            tv_product_preview_1_price.setText("Rp" + productPreview1.getPrice());
            setImage(productResURL, productPreview1Id, iv_product_preview_1_image);

            if (size > 1) {
                ProductListItem productPreview2 = productListItems.get(size - 2);
                productPreview2Id = productPreview2.getId();
                tv_product_preview_2_name.setText(productPreview2.getName());
                tv_product_preview_2_price.setText("Rp" + productPreview2.getPrice());
                setImage(productResURL, productPreview2Id, iv_product_preview_2_image);
            } else {
                cv_product_preview_2.setVisibility(View.GONE);
            }
        } else {
            li_store_product_preview.setVisibility(View.GONE);
        }
    }
    protected void setImage(String url, String id, ImageView imageView) {
        String resURL = url + id + ".png";

        double token=0;
        token=Math.random();
        resURL = resURL + "?"+token;
        // Token is used to change the url, forcing Picasso to update image

        Picasso.get()
                .load(resURL)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    @SuppressLint("ResourceAsColor")
    public void setCurrentUserStore(StoreResponse storeResponse) {
        tv_storeName.setText(storeResponse.getStoreName());
        tv_storePhone.setText(storeResponse.getStorePhone());
        tv_storeLocation. setText(storeResponse.getStoreCity());

        String id = storeResponse.getStoreId();
        String uri = "marketplace/store/res/";
        setImage(baseURL+uri , id, iv_storeImage);
    }
}