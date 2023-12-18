package com.kenazalfizza.farmersmarketplace.store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.StatusResponse;
import com.kenazalfizza.farmersmarketplace.model.StoreModel;
import com.kenazalfizza.farmersmarketplace.model.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreUpdateActivity extends AppCompatActivity {
    TextInputEditText et_update_storeName, et_update_storePhone, et_update_storeEmail;
    TextInputEditText et_update_storeProvince, et_update_storeCity, et_update_storePostcode, et_update_storeAddress;
    TextInputEditText et_update_storeDesc;

    Button btn_update_store;
    LinearLayout lay_nav_back;
    String userId, storeName, storeEmail, storePhone, storeProvince, storeCity, storePostcode, storeAddress, storeDescription;
    ProgressBar pb_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_update);

        elementsFindViewById();

        getStoreData();

        setHint();

        btn_update_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String preparedField = setUpdatedFieldData()[0];
                String preparedData = setUpdatedFieldData()[1];
                updateStore(preparedField, preparedData);
                finish();
            }
        });

        lay_nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    protected void elementsFindViewById() {
        pb_progress = findViewById(R.id.pb_progress);

        btn_update_store = findViewById(R.id.btn_update_store);

        lay_nav_back = findViewById(R.id.lay_nav_back);

        et_update_storeName = findViewById(R.id.et_update_store_name);
        et_update_storeEmail = findViewById(R.id.et_update_store_email);
        et_update_storePhone = findViewById(R.id.et_update_store_phone);
        et_update_storeProvince = findViewById(R.id.et_update_store_province);
        et_update_storeCity = findViewById(R.id.et_update_store_city);
        et_update_storePostcode = findViewById(R.id.et_update_store_postcode);
        et_update_storeAddress = findViewById(R.id.et_update_store_address);
        et_update_storeDesc = findViewById(R.id.et_update_store_desc);

    }

    protected void getStoreData() {

        userId = UserModel.getUserId();
        storeName = StoreModel.getStoreName();
        storeEmail = StoreModel.getStoreEmail();
        storePhone = StoreModel.getStorePhone();
        storeProvince = StoreModel.getStoreProvince();
        storeCity = StoreModel.getStoreCity();
        storePostcode = StoreModel.getStorePostcode();
        storeAddress = StoreModel.getStoreAddress();
        storeDescription = StoreModel.getStoreDescription();

    }

    protected void setHint() {
        et_update_storeName.setHint(storeName);
        et_update_storeEmail.setHint(storeEmail);
        et_update_storePhone.setHint(storePhone);
        et_update_storeProvince.setHint(storeProvince);
        et_update_storeCity.setHint(storeCity);
        et_update_storePostcode.setHint(storePostcode);
        et_update_storeAddress.setHint(storeAddress);
        et_update_storeDesc.setHint(storeDescription);
    }


    protected String[] setUpdatedFieldData() {

        String field = "user_id" + ",";
        String data = userId + ",";

        field = field + "store_id" + ",";
        data = data + StoreModel.getStoreId() + ",";
        // Assign New Value to Variables if the box is not empty
        if (!TextUtils.isEmpty(et_update_storeName.getText().toString())) {
            storeName = et_update_storeName.getText().toString();
            field = field + "store_name" + ",";
            data = data + storeName + ",";
        }

        if (!TextUtils.isEmpty(et_update_storeEmail.getText().toString())) {
            storeEmail = et_update_storeEmail.getText().toString();
            field = field + "store_email" + ",";
            data = data + storeEmail + ",";
        }

        if (!TextUtils.isEmpty(et_update_storePhone.getText().toString())) {
            storePhone = et_update_storePhone.getText().toString();
            field = field + "store_phone" + ",";
            data = data + storePhone + ",";
        }

        if (!TextUtils.isEmpty(et_update_storeProvince.getText().toString())) {
            storeProvince = et_update_storeProvince.getText().toString();
            field = field + "store_province" + ",";
            data = data + storeProvince + ",";
        }

        if (!TextUtils.isEmpty(et_update_storeCity.getText().toString())) {
            storeCity = et_update_storeCity.getText().toString();
            field = field + "store_city" + ",";
            data = data + storeCity + ",";
        }
        if (!TextUtils.isEmpty(et_update_storePostcode.getText().toString())) {
            storePostcode = et_update_storePostcode.getText().toString();
            field = field + "store_postcode" + ",";
            data = data + storePostcode + ",";
        }

        if (!TextUtils.isEmpty(et_update_storeAddress.getText().toString())) {
            storeAddress = et_update_storeAddress.getText().toString();
            field = field + "store_address" + ",";
            data = data + storeAddress + ",";
        }

        if (!TextUtils.isEmpty(et_update_storeDesc.getText().toString())) {
            storeDescription = et_update_storeDesc.getText().toString();
            field = field + "store_description" + ",";
            data = data + storeDescription + ",";
        }

        String preparedData = data.substring(0, data.length()-1);
        String preparedField = field.substring(0, field.length()-1);

        return new String[] {preparedField, preparedData};

    }

    protected void updateStore(String preparedField, String preparedData) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<StatusResponse> updateStoreStatusResponseCall = apiRequestInterface.updateStore(preparedField, preparedData);

        updateStoreStatusResponseCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                StatusResponse statusResponse = response.body();
                Log.d(null, statusResponse.toString());
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Log.d(null, t.toString());
            }
        });
    }
/*

    protected void updateStore(String preparedField, String preparedData) {

        String[] post_field = new String[2];
        post_field[0] = "field";
        post_field[1] = "data";

        String[] post_data = new String[2];
        post_data[0] = preparedField;
        post_data[1] = preparedData;

        String message = "Connection Failed";
        String result = "";
        TransferData updateAccount = new TransferData("http://192.168.1.5/class_test/stores/storeUpdate.php", "POST", post_field, post_data);
        if (updateAccount.startTransfer()) {
            if (updateAccount.onComplete()) {
                // Refresh the fragment to load a new data
                result = updateAccount.getResult(); // Result from server
                Log.d(null, result); // Logcat Result From Server

                switch (result) {
                    case "failed":
                        message = "Update Failed";
                        break;
                    case "success":
                        message = "Update Success";
                        break;
                }
            }
        }
        if (result.equals("success")) {
            StoreModel.retrieveStoreDetails(userId);
        }
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }

    protected void updateStoreJSON() {
        Gson gson = new Gson();
    }

 */
}