package com.kenazalfizza.farmersmarketplace.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.CryptoHash;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.account.LoginActivity;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.StatusResponse;
import com.kenazalfizza.farmersmarketplace.session.current.UserCurrent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {

    TextView tv_userName, tv_userEmail, tv_userPhone;
    EditText et_userName, et_userEmail, et_userPhone, et_userPassword;
    Button btn_update_account_details;
    String userId, userName, userEmail, userPhone, userPassword;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Refresh User Details

        // Assigning UserModel data to local variable
        userId = UserCurrent.getUserId();
        userName = UserCurrent.getUserName();
        userEmail = UserCurrent.getUserEmail();
        userPhone = UserCurrent.getUserPhone();

        /// region Profile/Account Details

        // Find View By Id for TextView
        tv_userName = (TextView) view.findViewById(R.id.tv_username);
        tv_userEmail = (TextView) view.findViewById(R.id.tv_useremail);
        tv_userPhone = (TextView) view.findViewById(R.id.tv_userphone);


        // Assign data to TextView
        tv_userName.setText(userName);
        tv_userEmail.setText(userEmail);
        tv_userPhone.setText(userPhone);

        /// endregion

        /// region Update Account Details
        et_userName = (EditText) view.findViewById(R.id.et_update_account_name);
        et_userEmail = (EditText) view.findViewById(R.id.et_update_account_email);
        et_userPhone = (EditText) view.findViewById(R.id.et_update_account_phone);
        et_userPassword = (EditText) view.findViewById(R.id.et_update_account_password);

        // Assign default hints to the Exit Text Box
        et_userName.setText(userName);
        et_userEmail.setText(userEmail);
        et_userPhone.setText(userPhone);

        /// endregion

        /// region Update Button
        btn_update_account_details = (Button) view.findViewById(R.id.btn_update_account);
        btn_update_account_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String field = "";
                String data = "";

                field = "id" + ",";
                data = userId + ",";

                // Assign New Value to Variables if the box is not empty
                if (!TextUtils.isEmpty(et_userName.getText().toString())) {
                    userName = et_userName.getText().toString();
                    field = field + "name" + ",";
                    data = data + userName + ",";
                }

                if (!TextUtils.isEmpty(et_userEmail.getText().toString())) {
                    userEmail = et_userEmail.getText().toString();
                    field = field + "email" + ",";
                    data = data + userEmail + ",";
                }
                if (!TextUtils.isEmpty(et_userPhone.getText().toString())) {
                    userPhone = et_userPhone.getText().toString();
                    field = field + "phone" + ",";
                    data = data + userPhone + ",";
                }
                if (!TextUtils.isEmpty(et_userPassword.getText().toString())) {
                    // Encrypt Password
                    userPassword = et_userPassword.getText().toString();
                    CryptoHash cryptoHash = new CryptoHash(userPassword);
                    userPassword = cryptoHash.run();
                    field = field + "password" + ",";
                    data = data + userPassword + ",";
                }
                //Log.d(null, userId + userName);

                updateUser(field, data);
            }

        });

        /// endregion

        // Inflate the layout for this fragment
        return view;
    }

    public void updateUser(String field, String data) {
        String preparedField = "";
        String preparedData = "";

        preparedData = data.substring(0, data.length()-1);
        preparedField = field.substring(0, field.length()-1);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<StatusResponse> updateUserResponseCall = apiRequestInterface.updateUser(preparedField, preparedData);

        updateUserResponseCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                StatusResponse statusResponse = response.body();
                Log.d(null, statusResponse.toString());
                if (statusResponse.getStatus().equals("success")) {
                    Toast.makeText(getContext(), "Account Updated", Toast.LENGTH_SHORT);
                    getActivity().finish();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Log.d(null, t.toString());
            }
        });
    }

}