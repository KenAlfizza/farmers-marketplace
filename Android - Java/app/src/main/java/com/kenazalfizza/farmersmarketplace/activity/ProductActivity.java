package com.kenazalfizza.farmersmarketplace.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.account.CartStoreActivity;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.store.StoreActivity;
import com.kenazalfizza.farmersmarketplace.store.StoreProductUpdateActivity;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.ProductResponse;
import com.kenazalfizza.farmersmarketplace.api.response.StatusResponse;
import com.kenazalfizza.farmersmarketplace.api.response.cart.CartStore;
import com.kenazalfizza.farmersmarketplace.session.current.UserCartCurrent;
import com.kenazalfizza.farmersmarketplace.session.current.UserCurrent;
import com.kenazalfizza.farmersmarketplace.session.current.UserStoreCurrent;
import com.kenazalfizza.farmersmarketplace.api.response.cart.CartProduct;
import com.kenazalfizza.farmersmarketplace.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProductActivity extends AppCompatActivity {
    String baseURL = "http://192.168.1.5/";

    static String productId = "";
    static String storeId = "";

    TextView tv_productName, tv_productType, tv_productPrice, tv_productStock, tv_productDescription;
    TextView tv_storeName, tv_storeLocation;

    ImageView iv_productImage, iv_storeImage;
    ImageView iv_gotoCart;
    ImageView iv_back;

    LinearLayout li_goto_storeDetails;

    CardView cv_addtocart, cv_edit_product;
    Product product = new Product();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        elementsFindViewById();
        getProduct(productId);

        iv_gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CartStoreActivity.class);
                startActivity(intent);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cv_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductToCart();
                updateCartDb();
                Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        cv_edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StoreProductUpdateActivity.class);
                StoreProductUpdateActivity.setProduct(product);
                startActivity(intent);
            }
        });

        li_goto_storeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
                StoreActivity.setStoreId(product.getStoreId());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getProduct(productId);
    }

    protected void elementsFindViewById() {
        tv_productName = findViewById(R.id.tv_product_name);
        tv_productType = findViewById(R.id.tv_product_type);
        tv_productPrice = findViewById(R.id.tv_product_price);
        tv_productDescription = findViewById(R.id.tv_product_description);
        tv_productStock = findViewById(R.id.tv_product_stock);

        tv_storeName = findViewById(R.id.tv_store_name);
        tv_storeLocation = findViewById(R.id.tv_store_location);
        iv_storeImage = findViewById(R.id.iv_store_image);

        iv_productImage = findViewById(R.id.iv_product_image);
        iv_storeImage = findViewById(R.id.iv_store_image);
        iv_back = findViewById(R.id.iv_back);

        iv_gotoCart = findViewById(R.id.iv_goto_cart);

        cv_addtocart = findViewById(R.id.cv_addtocart);
        cv_edit_product = findViewById(R.id.cv_edit_product);

        li_goto_storeDetails = findViewById(R.id.li_goto_store_details);
    }

    public static void setProductId(String productId) {
        ProductActivity.productId = productId;
    }

    protected void getProduct(String productId) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<ProductResponse> productResponseCall = apiRequestInterface.requestProduct(productId);

        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                ProductResponse productResponse = response.body();

                setProduct(productResponse);
                setProductDetails(productResponse);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connection failure. Try again later...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void setProduct(ProductResponse productResponse) {
        product.setProduct(productResponse);
    }

    @SuppressLint("SetTextI18n")
    protected void setProductDetails(ProductResponse productResponse) {
        tv_productName.setText(product.getProductName());
        String type = product.getProductType();
        tv_productType.setText(type.substring(0,1).toUpperCase() + type.substring(1).toLowerCase());
        tv_productPrice.setText("Rp " + product.getProductPrice());
        tv_productStock.setText(String.valueOf(product.getProductStock()));
        String desc = product.getProductDescription();
        tv_productDescription.setText(desc);

        String productResURL = baseURL + "marketplace/product/res/" + product.getProductId() + ".png";
        Log.d(null, productResURL);
        setProductImage(productResURL);

        tv_storeName.setText(product.getStoreName());
        tv_storeLocation.setText(product.getStoreLocation());
        storeId = product.getStoreId();
        String storeResURL = baseURL + "marketplace/store/res/" + product.getStoreId() + ".png";
        setStoreImage(storeResURL);
        setButtonMenu();

    }

    private void setButtonMenu() {
        if (product.getStoreId().equals(UserStoreCurrent.getStoreId())) {
            cv_addtocart.setVisibility(View.GONE);
        } else {
            cv_edit_product.setVisibility(View.GONE);
        }
    }

    private void setProductImage(String url) {
        double token=0;
        token=Math.random();
        url = url + "?"+token;
        // Token is used to change the url, forcing Picasso to update image
        Picasso.get()
                .load(url)
                .fit()
                .centerCrop()
                .into(iv_productImage);
    }

    private void setStoreImage(String url) {
        Picasso.get()
                .load(url)
                .fit()
                .centerCrop()
                .into(iv_storeImage);
    }

    protected void setProductDetailsOld() {
        String productName = product.getProductName();
        String productType = product.getProductType();
        String productPrice = "Rp " + String.valueOf(product.getProductPrice());
        String productDescription = product.getProductDescription();
        String storeName = product.getStoreName();
        String storeLocation = product.getStoreLocation();

        tv_productName.setText(productName);
        tv_productType.setText(productType);
        tv_productPrice.setText(productPrice);
        tv_productDescription.setText(productDescription);
        tv_storeName.setText(storeName);
        tv_storeLocation.setText(storeLocation);
    }


    protected void addProductToCart() {
        boolean productInCart = false;
        ArrayList<CartProduct> cartProductsCurrent = UserCartCurrent.getCartProducts();

        for (CartProduct cp : cartProductsCurrent) {
            if (product.getProductId().equals(cp.getId())) {
                productInCart = true;
                break;
            }
        }
        if (productInCart) {
            for (CartProduct cp : cartProductsCurrent) {
                if (product.getProductId().equals(cp.getId())) {
                    cp.setQuantity(cp.getQuantity()+1);
                    break;
                }
            }
        } else {
            CartProduct newCartProduct = new CartProduct().toCartProduct(product);
            cartProductsCurrent.add(newCartProduct);
        }
        UserCartCurrent.setCartProducts(cartProductsCurrent);
        Log.d(null, UserCartCurrent.getCartProducts().toString());

        boolean storeInCart = false;
        ArrayList<CartStore> cartStoresCurrent = UserCartCurrent.getCartStores();
        for (CartStore cs : cartStoresCurrent) {
            if (product.getStoreId().equals(cs.getId())) {
                storeInCart = true;
                break;
            }
        }
        if (!storeInCart) {
            CartStore newCartStore = new CartStore();
            newCartStore.setId(product.getStoreId());
            newCartStore.setName(product.getStoreName());
            newCartStore.setLocation(product.getStoreLocation());
            newCartStore.setAvailable(true);
            cartStoresCurrent.add(newCartStore);
            UserCartCurrent.setCartStores(cartStoresCurrent);
        }
        Log.d(null, UserCartCurrent.getCartStores().toString());

    }

    private void updateCartDb() {
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
}