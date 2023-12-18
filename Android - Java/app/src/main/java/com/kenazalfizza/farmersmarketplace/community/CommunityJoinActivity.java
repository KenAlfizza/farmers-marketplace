package com.kenazalfizza.farmersmarketplace.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.activity.DashboardActivity;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.CommunityResponse;
import com.kenazalfizza.farmersmarketplace.api.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommunityJoinActivity extends AppCompatActivity {
    EditText et_join_id, et_join_pass;
    Button btn_join;
    String joinId, joinPassword;
    String baseURL = "http://192.168.1.5/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_join);

        elementsFindViewById();

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getElementData();
                joinCommunity();

                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void elementsFindViewById() {
        et_join_id = findViewById(R.id.et_join_id);
        et_join_pass = findViewById(R.id.et_join_password);

        btn_join = findViewById(R.id.btn_join);
    }

    private void getElementData() {
        joinId = et_join_id.getText().toString();
        joinPassword = et_join_pass.getText().toString();

    }

    private void joinCommunity() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        // Instantiate Retrofit with base url and GSON Converter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Retrofit instantiate the API interface
        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);
        // Run the loginUser() method of the API with arguments
        Call<CommunityResponse> communityResponseCall = apiRequestInterface.joinCommunity(joinId,joinPassword);

        communityResponseCall.enqueue(new Callback<CommunityResponse>() {
            @Override
            public void onResponse(Call<CommunityResponse> call, Response<CommunityResponse> response) {
                CommunityResponse communityResponse = response.body();
                setUserCommunityCurrent();
            }

            @Override
            public void onFailure(Call<CommunityResponse> call, Throwable t) {

            }
        });
    }

    private void setUserCommunityCurrent() {

    }
}