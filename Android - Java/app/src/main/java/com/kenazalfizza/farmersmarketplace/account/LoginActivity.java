package com.kenazalfizza.farmersmarketplace.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.CryptoHash;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.activity.DashboardActivity;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.LoginResponse;
import com.kenazalfizza.farmersmarketplace.api.response.StoreResponse;
import com.kenazalfizza.farmersmarketplace.session.current.UserCurrent;
import com.kenazalfizza.farmersmarketplace.session.current.UserStoreCurrent;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    Button btn_login, btn_goto_register;
    TextInputEditText et_email, et_password;
    private String userEmail, userPassword;
    String baseURL = "http://192.168.1.5/";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_goto_register = findViewById(R.id.btn_goto_register);

        et_email = findViewById(R.id.et_login_email);
        et_password = findViewById(R.id.et_login_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userEmail = String.valueOf(et_email.getText());
                userPassword = String.valueOf(et_password.getText());

                // Encrypt password before verification to the server
                CryptoHash cryptoHash = new CryptoHash(userPassword);
                userPassword = cryptoHash.run();

                loginUser(userEmail, userPassword);
            }
        });

        btn_goto_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void loginUser(String userEmail, String userPassword) {
        // Instantiate GSON
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
        Call<LoginResponse> loginResponseCall = apiRequestInterface.loginUser(userEmail, userPassword);
        // Enqueue method must be used as it allows network connection under child thread otherwise program crash
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            // Method run when the request success
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body(); // Getting the response and storing in local variable
                String status = loginResponse.getStatus(); // Getting the status of login response
                // Check if the status is authorised of login response
                if (status.equals("Authorized")) {
                    setCurrentUser(loginResponse); // Set the current user session
                    requestStoreDetails(loginResponse.getUserId()); // Set the current user store session
                    Toast.makeText(getApplicationContext(), "Logging you in...", Toast.LENGTH_SHORT).show();
                    // Show login message
                    // Starting Dashboard Activity and Finish (end) Current Activity
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show(); // Show login status error message
                }
            }
            // Method run when the request fail
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connection Failure", Toast.LENGTH_SHORT).show();
                // Show request error message "Connection Failure"
            }
        });

    }

    public void requestStoreDetails(String userId) {
        // Instantiate GSON
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        // Instantiate Retrofit with base url and GSON Converter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL) // Webserver URL
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Retrofit instantiate the API interface
        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);
        //
        Call<StoreResponse> storeResponseCall = apiRequestInterface.requestStoreByUserId(userId);
        // Enqueue method must be used as it allows network connection under child thread.
        // Otherwise program crash
        storeResponseCall.enqueue(new Callback<StoreResponse>() {
            @Override
            // Method run when the request success
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                StoreResponse storeResponse = response.body();
                Log.d(null, storeResponse.toString());
                setCurrentUserStore(storeResponse);
            }
            // Method run when the request fail
            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                // Do nothing
            }
        });
    }

    public void setCurrentUser(LoginResponse loginResponse) {
        UserCurrent.setUserId(loginResponse.getUserId());
        UserCurrent.setUserName(loginResponse.getUserName());
        UserCurrent.setUserEmail(loginResponse.getUserEmail());
        UserCurrent.setUserPhone(loginResponse.getUserPhone());
        Log.d(null, UserCurrent.getUserName());
    }

    public void setCurrentUserStore(StoreResponse storeResponse) {
        UserStoreCurrent.setStoreId(storeResponse.getStoreId());
        UserStoreCurrent.setStoreName(storeResponse.getStoreName());
        UserStoreCurrent.setStoreEmail(storeResponse.getStoreEmail());
        UserStoreCurrent.setStorePhone(storeResponse.getStorePhone());
        UserStoreCurrent.setStoreProvince(storeResponse.getStoreProvince());
        UserStoreCurrent.setStoreCity(storeResponse.getStoreCity());
        UserStoreCurrent.setStorePostcode(storeResponse.getStorePostcode());
        UserStoreCurrent.setStoreAddress(storeResponse.getStoreAddress());
        UserStoreCurrent.setStoreDescription(storeResponse.getStoreDescription());
    }

}