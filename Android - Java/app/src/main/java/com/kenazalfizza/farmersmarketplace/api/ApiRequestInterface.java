package com.kenazalfizza.farmersmarketplace.api;

import android.graphics.Bitmap;

import com.kenazalfizza.farmersmarketplace.api.response.CommunityResponse;
import com.kenazalfizza.farmersmarketplace.api.response.ProductListResponse;
import com.kenazalfizza.farmersmarketplace.api.response.ProductResponse;
import com.kenazalfizza.farmersmarketplace.api.response.RegisterResponse;
import com.kenazalfizza.farmersmarketplace.api.response.StatusResponse;
import com.kenazalfizza.farmersmarketplace.api.response.StoreResponse;
import com.kenazalfizza.farmersmarketplace.api.response.LoginResponse;
import com.kenazalfizza.farmersmarketplace.api.response.cart.CartResponse;
import com.kenazalfizza.farmersmarketplace.model.ProductListItem;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiRequestInterface {

    @FormUrlEncoded // Type of request body
    @POST("marketplace/account/login/") // HTTP Request and URI
    Call<LoginResponse>
    /* Call<T> Invoking Retrofit Request and Response Method
        LoginResponse is the response body type
     */
    loginUser(
            // Filling the POST Fields
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("marketplace/account/register/")
    Call<RegisterResponse>
    registerUser(
            @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("marketplace/account/update/")
    Call<StatusResponse>
    updateUser(
            @Field("field") String field,
            @Field("data") String data
    );

    @FormUrlEncoded
    @POST("marketplace/store/update/")
    Call<StatusResponse>
    updateStore(
            @Field("field") String field,
            @Field("data") String data
    );


    @Multipart
    @POST("marketplace/store/register/")
    Call<StoreResponse>
    registerStore(
            @Part("user_id") RequestBody user_id,
            @Part("store_id") RequestBody store_id,
            @Part MultipartBody.Part image,
            @Part("store_name") RequestBody name,
            @Part("store_email") RequestBody email,
            @Part("store_phone") RequestBody phone,
            @Part("store_province") RequestBody province,
            @Part("store_city") RequestBody city,
            @Part("store_postcode") RequestBody postcode,
            @Part("store_address") RequestBody address,
            @Part("store_description") RequestBody desc

    );

    @FormUrlEncoded
    @POST("marketplace/store/retrieve/uid/")
    Call<StoreResponse>
    requestStoreByUserId(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("marketplace/store/retrieve/sid/")
    Call<StoreResponse>
    requestStoreByStoreId(
            @Field("store_id") String store_id
    );

    @FormUrlEncoded
    @POST("marketplace/product/retrieve/")
    Call<ProductResponse>
    requestProduct(
            @Field("product_id") String product_id
    );

    @Multipart
    @POST("marketplace/product/register/")
    Call<StatusResponse>
    registerProduct(
            @Part("user_id") RequestBody user_id,
            @Part("store_id") RequestBody store_id,
            @Part("id") RequestBody id,
            @Part MultipartBody.Part image,
            @Part("name") RequestBody name,
            @Part("type") RequestBody type,
            @Part("price") RequestBody price,
            @Part("stock") RequestBody stock,
            @Part("description") RequestBody desc
    );

    @Multipart
    @POST("marketplace/product/update/")
    Call<StatusResponse>
    updateProduct(
            @Part("user_id") RequestBody user_id,
            @Part("store_id") RequestBody store_id,
            @Part("id") RequestBody id,
            @Part MultipartBody.Part image,
            @Part("name") RequestBody name,
            @Part("type") RequestBody type,
            @Part("price") RequestBody price,
            @Part("stock") RequestBody stock,
            @Part("description") RequestBody desc
    );

    @Multipart
    @POST("marketplace/product/remove/")
    Call<StatusResponse>
    removeProduct(
            @Part("user_id") RequestBody user_id,
            @Part("user_password") RequestBody user_password,
            @Part("store_id") RequestBody store_id,
            @Part("product_id") RequestBody product_id
    );


    @FormUrlEncoded
    @POST("marketplace/store/retrieve/products/")
    Call<ProductListResponse>
    requestStoreProducts(
            @Field("store_id") String store_id
    );

    @POST("marketplace/product/retrieve/latest/")
    Call<ProductListItem>
    requestProductLatest();

    @FormUrlEncoded
    @POST("marketplace/product/retrieve/category/")
    Call<ProductListResponse>
    requestProductCategory(
            @Field("type") String type,
            @Field("current_size") int current_size,
            @Field("request_size") int request_size
    );

    @FormUrlEncoded
    @POST("marketplace/cart/retrieve/")
    Call<CartResponse>
    requestCart(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("marketplace/cart/update/")
    Call<StatusResponse>
    updateCart(
            @Field("user_id") String user_id,
            @Field("stores") String cart_stores_json,
            @Field("products") String cart_products_json
    );

    @FormUrlEncoded
    @POST("marketplace/store/retrieve/products/")
    Call<StatusResponse>
    registerCommunity(
            @Field("user_id") String user_id,
            @Field("community_id") String community_id,
            @Field("community_name") int community_name,
            @Field("community_province") int community_province
    );

    @FormUrlEncoded
    @POST("marketplace/store/retrieve/products/")
    Call<CommunityResponse>
    retrieveCommunity(
            @Field("community_id") String community_id
    );

    @FormUrlEncoded
    @POST("marketplace/community/retrieve/")
    Call<CommunityResponse>
    joinCommunity(
            @Field("community_id") String community_id,
            @Field("community_password") String community_pass
    );
}
