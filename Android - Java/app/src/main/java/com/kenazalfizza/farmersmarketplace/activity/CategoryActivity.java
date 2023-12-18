package com.kenazalfizza.farmersmarketplace.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.ProductListResponse;
import com.kenazalfizza.farmersmarketplace.model.ProductListItem;
import com.kenazalfizza.farmersmarketplace.view.RecyclerViewProductListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryActivity extends AppCompatActivity {
    // Variable declaration
    static String productCategory = "";
    String baseURL = "http://192.168.1.5/";

    TextView tv_category;
    ImageView iv_category;

    RecyclerView recyclerView;
    RecyclerViewProductListAdapter recyclerViewProductListAdapter;
    ArrayList<ProductListItem> rowsArrayProductList = new ArrayList<>();

    //ArrayList<Bitmap> rowsArrayImageList = new ArrayList<>();
    boolean isLoading = false;
    boolean allLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        elementsFindViewById();

        populateData(productCategory);

        initScrollListener(productCategory);

    }
    protected void elementsFindViewById() {
        recyclerView = findViewById(R.id.rv_product_list);
        tv_category = findViewById(R.id.tv_category);
        iv_category = findViewById(R.id.iv_category);

        String category = "";
        switch (productCategory) {
            case "vegetable":
                category = "Vegetables";
                iv_category.setImageResource(R.drawable.ic_category_vegetable);
                break;
            case "fruit":
                category = "Fruits";
                iv_category.setImageResource(R.drawable.ic_category_fruit);
                break;
            case "animal":
                category = "Animal Produce";
                iv_category.setImageResource(R.drawable.ic_category_animals);
                break;
            case "seed":
                category = "Seeds";
                iv_category.setImageResource(R.drawable.ic_category_seed);
                break;
            case "other":
                category = "Others";
                iv_category.setImageResource(R.drawable.ic_category_other);
                break;
            default :
                category = "default";
                break;
        }
        tv_category.setText(category);
    }
    /*
    private void populateDataOld() {
        // Populate the recyclerView with items

        int start = 1; // Start index of item loaded
        int end = 10; // The end index of the item
        int maxPopulatedLength = end-start + 1; // Length of items loaded

        //Note: The index of item starts at 1 NOT 0 for DataBase purposes

        String[] field = {"user_id", "start", "end"};
        String[] data = {UserModel.getUserId(), String.valueOf(start), String.valueOf(end)};
        String result = "";
        TransferData getProductList = new TransferData(
                "http://192.168.1.5/class_test/products/productRetrieveList.php?method=user_limit",
                "POST",
                field, data);
        if (getProductList.startTransfer()) {
            if (getProductList.onComplete()) {
                result = getProductList.getResult();
                try {
                    Gson gson = new Gson(); // Instantiation of GSON object
                    Product[] products = gson.fromJson(result, Product[].class);
                    // Converting the JSON Object Array into Product Object Array

                    for (Product product : products) {
                        rowsArrayIdList.add(product.getProductId());
                        rowsArrayItemList.add(product.getProductName());
                        rowsArrayLocationList.add(product.getStoreLocation());
                        rowsArrayPriceList.add(String.valueOf(product.getProductPrice()));
                    }
                    if (products.length < maxPopulatedLength) {
                        allLoaded = true;
                    }
                } catch (Throwable e){
                    allLoaded = true;
                    Log.d(null, result);
                }
            }
        }
    }

    private void initAdapterOld() {
        // Initialising the adapter for recyclerView
        recyclerViewProductListAdapter = new RecyclerViewProductListAdapterOld(rowsArrayIdList,
                rowsArrayItemList, rowsArrayLocationList, rowsArrayPriceList);
        recyclerView.setAdapter(recyclerViewProductListAdapter);
    }

    private void initScrollListenerOld() {
        // Initialisation of a scroll listener to detect the user scrolling
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager =
                        (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading && !allLoaded) {
                    if (linearLayoutManager != null
                            && linearLayoutManager.findLastCompletelyVisibleItemPosition()
                            == rowsArrayItemList.size() - 1) {
                        // When it reaches the bottom of the list more item is loaded if
                        // not all has been loaded
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMoreOld() {
        // add a null element ino the arrays to temporarily move the list up
        // to make room for progressBar
        rowsArrayIdList.add(null);
        rowsArrayItemList.add(null);
        rowsArrayLocationList.add(null);
        rowsArrayPriceList.add(null);

        // notify the adapter that the item has been inserted at position where it was null
        recyclerViewProductListAdapter.notifyItemInserted(rowsArrayIdList.size() - 1);
        recyclerViewProductListAdapter.notifyItemInserted(rowsArrayItemList.size() - 1);
        recyclerViewProductListAdapter.notifyItemInserted(rowsArrayLocationList.size() - 1);
        recyclerViewProductListAdapter.notifyItemInserted(rowsArrayPriceList.size() - 1);


        // Handler to run the TransferData between the app and the webserver
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayIdList.remove(rowsArrayIdList.size() - 1);
                rowsArrayItemList.remove(rowsArrayItemList.size() - 1);
                rowsArrayItemList.remove(rowsArrayLocationList.size() - 1);
                rowsArrayPriceList.remove(rowsArrayPriceList.size() - 1);
                //rowsArrayImageList.remove(rowsArrayImageList.size() - 1);
                int scrollPosition = rowsArrayItemList.size();
                recyclerViewProductListAdapter.notifyItemRemoved(scrollPosition);

                int currentSize = scrollPosition;

                int start = currentSize+1;
                int end = start+9;

                int maxPopulatedLength = end - start + 1;

                String[] field = {"user_id", "start", "end"};
                String[] data = {UserModel.getUserId(), String.valueOf(start), String.valueOf(end)};
                String result;
                TransferData getProductList = new TransferData(
                        "http://192.168.1.5/class_test/products/productRetrieveList.php?method=user_limit",
                        "POST",
                        field, data);
                if (getProductList.startTransfer()) {
                    if (getProductList.onComplete()) {
                        result = getProductList.getResult();
                        Log.d(null, result);
                        try {
                            Gson gson = new Gson();
                            Product[] products = gson.fromJson(result, Product[].class);

                            for (Product product : products) {
                                rowsArrayIdList.add(product.getProductId());
                                rowsArrayItemList.add(product.getProductName());
                                rowsArrayLocationList.add(product.getStoreLocation());
                                rowsArrayPriceList.add(String.valueOf(product.getProductPrice()));
                            }
                        } catch (Throwable e) {
                            allLoaded = true;
                        }
                    }
                }
                recyclerViewProductListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }


     */

    public static void setCategory(String productCategory) {
        CategoryActivity.productCategory = productCategory;
    }

    private void populateData(String productCategory) {
        int currentSize = 0; // Start index of item loaded
        int requestSize = 5;

        //Note: The index of item starts at 1 NOT 0 for DataBase purposes

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<ProductListResponse> productStoreListResponseCall = apiRequestInterface.requestProductCategory(productCategory, currentSize, requestSize);

            productStoreListResponseCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                ProductListResponse productListResponse = response.body();
                assert productListResponse != null;
                rowsArrayProductList = productListResponse.getProductList();
                Log.d(null, productListResponse.toString());
                initAdapter(rowsArrayProductList);
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

    private void initScrollListener(String productCategory) {
        // Initialisation of a scroll listener to detect the user scrolling
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager =
                        (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading && !allLoaded) {
                    if (linearLayoutManager != null
                            && linearLayoutManager.findLastCompletelyVisibleItemPosition()
                            == rowsArrayProductList.size() - 1) {
                        // When it reaches the bottom of the list more item is loaded if
                        // not all has been loaded
                        loadMore(productCategory);
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore(String productCategory) {
        int requestSize = 5;

        isLoading = true;
        // add a null element ino the arrays to temporarily move the list up
        // to make room for progressBar
        rowsArrayProductList.add(null);

        // notify the adapter that the item has been inserted at position where it was null
        recyclerViewProductListAdapter.notifyItemInserted(rowsArrayProductList.size() - 1);


        // Handler to run the TransferData between the app and the webserver
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayProductList.remove(rowsArrayProductList.size() - 1);
                int scrollPosition = rowsArrayProductList.size();
                recyclerViewProductListAdapter.notifyItemRemoved(scrollPosition);

                int currentSize = scrollPosition;

                int start = currentSize+1;
                int end = start+9;
                int maxPopulatedLength = end - start + 1;

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.5/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

                Call<ProductListResponse> productStoreListResponseCall = apiRequestInterface.requestProductCategory(productCategory, currentSize, requestSize);

                productStoreListResponseCall.enqueue(new Callback<ProductListResponse>() {
                    @Override
                    public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                        ProductListResponse productListResponse = response.body();
                        rowsArrayProductList.addAll(productListResponse.getProductList());
                        if (rowsArrayProductList.size() < maxPopulatedLength) {
                            allLoaded = true;
                        }
                        Log.d(null, rowsArrayProductList.toString());
                        recyclerViewProductListAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }

                    @Override
                    public void onFailure(Call<ProductListResponse> call, Throwable t) {
                        Log.d(null, t.toString());
                    }
                });

            }
        }, 2000);
    }
}