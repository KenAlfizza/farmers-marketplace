package com.kenazalfizza.farmersmarketplace.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.CryptoHash;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.RegisterResponse;
import com.kenazalfizza.farmersmarketplace.generator.IdGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    // Variable declaration
    Button btn_register, btn_goto_login;
    TextInputEditText et_register_name, et_register_email, et_register_phone, et_register_password;
    protected String userId, userName, userEmail, userPhone, userPassword;

    boolean error;
    String error_message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Assigning the button variables with the buttons on the gui
        btn_register = findViewById(R.id.btn_register);
        btn_goto_login = findViewById(R.id.btn_goto_login);

        // Assigning the text box variables with the text box in the gui
        et_register_name = findViewById(R.id.et_register_name);
        et_register_email = findViewById(R.id.et_register_email);
        et_register_phone = findViewById(R.id.et_register_phone);
        et_register_password = findViewById(R.id.et_register_password);

        // Method to check if the register button is pressed
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error = false;
                // Assigning local variables with the value on each text box
                userName = getFieldValue(et_register_name, "Name");
                userEmail = getFieldValue(et_register_email, "Email");
                userPhone = getFieldValue(et_register_phone, "Phone Number");
                userPassword = getFieldValue(et_register_password, "Password");


                // Generate a unique user id based on their registration information
                IdGenerator generateID = new IdGenerator();
                userId = generateID.userId(userName);

                // Encrypt password before registering the user / sending data to the server
                CryptoHash cryptoHash = new CryptoHash(userPassword);
                userPassword = cryptoHash.run();

                // Calling the method to send the data into database
                if (error) {
                    error_message = error_message + "required";
                    Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                    error_message = "";
                } else {
                    registerUser(userId, userName, userEmail, userPhone, userPassword);
                }
            }
        });

        // Method to check if the login button is pressed
        btn_goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            } // End the current RegisterActivity and goes back into login Activity
        });

    }

    public String getFieldValue(EditText et, String field_desc) {
        String result = "";
        if (!TextUtils.isEmpty(et.getText().toString())) {
            result = et.getText().toString();
        } else {
            error = true;
            error_message = error_message + field_desc + ", ";
        }
        return result;
    }

    protected void registerUser(String userId, String userName, String userEmail, String userPhone, String userPassword) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<RegisterResponse> registerUserCall = apiRequestInterface
                .registerUser(userId, userName, userEmail, userPhone, userPassword);

        registerUserCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse registerResponse = response.body();
                if (registerResponse.getStatus().equals("success")) {
                    Toast.makeText(getApplicationContext(), "Registering...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed. Account already exist...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Registration failed. Try again later...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}