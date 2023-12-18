package com.kenazalfizza.farmersmarketplace.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.activity.CategoryActivity;
import com.kenazalfizza.farmersmarketplace.activity.ProductActivity;
import com.kenazalfizza.farmersmarketplace.account.CartStoreActivity;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.model.ProductListItem;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {

    LinearLayout li_cat_vegetable, li_cat_fruit, li_cat_animal, li_cat_seed, li_cat_other;


    ImageView iv_goto_cart, iv_goto_profile, iv_new_product_image;

    LinearLayout li_new_product;
    TextView tv_new_product_name, tv_new_product_location, tv_new_product_price;

    String newProductId;

    String baseURL = "http://192.168.1.5/";

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        retrieveNewProduct();

        elementsFindViewById(view);

        li_cat_vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                CategoryActivity.setCategory("vegetable");
                startActivity(intent);
            }
        });

        li_cat_fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                CategoryActivity.setCategory("fruit");
                startActivity(intent);
            }
        });

        li_cat_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                CategoryActivity.setCategory("animal");
                startActivity(intent);
            }
        });

        li_cat_seed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                CategoryActivity.setCategory("seed");
                startActivity(intent);
            }
        });

        li_cat_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                CategoryActivity.setCategory("other");
                startActivity(intent);
            }
        });

        iv_goto_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
            }
        });

        iv_goto_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CartStoreActivity.class);
                startActivity(intent);
            }
        });

        li_new_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                ProductActivity.setProductId(newProductId);
                startActivity(intent);
            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveNewProduct();
    }

    private void elementsFindViewById(View view) {
        // Find View By Id for Category
        li_cat_vegetable = view.findViewById(R.id.li_cat_vegetable);
        li_cat_fruit = view.findViewById(R.id.li_cat_fruit);
        li_cat_animal = view.findViewById(R.id.li_cat_animal);
        li_cat_seed = view.findViewById(R.id.li_cat_seed);
        li_cat_other = view.findViewById(R.id.li_cat_other);

        iv_goto_cart = view.findViewById(R.id.iv_goto_cart);
        iv_goto_profile = view.findViewById(R.id.iv_goto_profile);
        iv_new_product_image = view.findViewById(R.id.iv_new_product_image);


        li_new_product = view.findViewById(R.id.li_new_product);
        tv_new_product_name = view.findViewById(R.id.tv_new_product_name);
        tv_new_product_location = view.findViewById(R.id.tv_new_product_location);
        tv_new_product_price = view.findViewById(R.id.tv_new_product_price);
    }

    private void retrieveNewProduct() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<ProductListItem> productStoreListResponseCall = apiRequestInterface.requestProductLatest();

        productStoreListResponseCall.enqueue(new Callback<ProductListItem>() {
            @Override
            public void onResponse(Call<ProductListItem> call, Response<ProductListItem> response) {
                ProductListItem productListItem = response.body();
                setNewProductRelease(productListItem);
            }

            @Override
            public void onFailure(Call<ProductListItem> call, Throwable t) {
                Log.d(null, t.toString());
            }
        });
    }

    private void setNewProductRelease(ProductListItem p) {
        newProductId = p.getId();
        tv_new_product_name.setText(p.getName());
        tv_new_product_location.setText(p.getLocation());
        tv_new_product_price.setText("Rp" + p.getPrice());

        String resURL = baseURL + "marketplace/product/res/" + p.getId() + ".png";

        Picasso.get()
                .load(resURL)
                .fit()
                .centerCrop()
                .into(iv_new_product_image);
    }
}