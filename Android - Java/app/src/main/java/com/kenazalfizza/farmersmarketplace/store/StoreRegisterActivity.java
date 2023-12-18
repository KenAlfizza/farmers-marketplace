package com.kenazalfizza.farmersmarketplace.store;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.activity.DashboardActivity;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.StoreResponse;
import com.kenazalfizza.farmersmarketplace.session.current.UserCurrent;
import com.kenazalfizza.farmersmarketplace.session.current.UserStoreCurrent;
import com.kenazalfizza.farmersmarketplace.generator.IdGenerator;
import com.kenazalfizza.farmersmarketplace.model.StoreModel;
import com.kenazalfizza.farmersmarketplace.model.UserModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreRegisterActivity extends AppCompatActivity {
    private final int RESULT_LOAD_IMG = 100;

    TextInputEditText et_register_store_name, et_register_store_phone, et_register_store_email;
    TextInputEditText et_register_store_province, et_register_store_city, et_register_store_postcode, et_register_store_address;
    TextInputEditText et_register_store_desc;
    Button btn_register_store;
    ImageButton btn_register_store_image;
    private String storeId, storeName, storeEmail, storePhone, storeProvince, storeCity, storePostcode, storeAddress, storeDescription;
    private String userId;
    boolean error;
    String error_message = "";
    String error_component = "";
    Bitmap storeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_register);

        elementsFindViewById();

        storeImage = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ic_store_empty_grey);

        btn_register_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_component = "";
                error_message = "";
                error = false;

                userId = UserModel.getUserId();

                IdGenerator generateId = new IdGenerator();
                storeId = generateId.storeId();

                storeName = getFieldValue(et_register_store_name, "Name");
                storeEmail = getFieldValue(et_register_store_email, "Email");
                storePhone = getFieldValue(et_register_store_phone, "Phone");
                storeProvince = getFieldValue(et_register_store_province, "Province");
                storeCity = getFieldValue(et_register_store_city, "City");
                storePostcode = getFieldValue(et_register_store_postcode, "Post Code");
                storeAddress = getFieldValue(et_register_store_address, "Address");
                storeDescription = getFieldValue(et_register_store_desc, "Description");

                if (error) {
                    error_component = error_component.substring(0,error_component.length()-2);
                    error_message = error_component + " required";
                    Snackbar.make(findViewById(R.id.lay_create_store), error_message, Snackbar.LENGTH_SHORT).show();
                } else {
                    registerStore();
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btn_register_store_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
    }

    protected void elementsFindViewById() {
        btn_register_store = findViewById(R.id.btn_register_store);
        btn_register_store_image = findViewById(R.id.btn_register_store_image);
        et_register_store_name = findViewById(R.id.et_register_store_name);
        et_register_store_email = findViewById(R.id.et_register_store_email);
        et_register_store_phone = findViewById(R.id.et_register_store_phone);
        et_register_store_province = findViewById(R.id.et_register_store_province);
        et_register_store_city = findViewById(R.id.et_register_store_city);
        et_register_store_postcode = findViewById(R.id.et_register_store_postcode);
        et_register_store_address = findViewById(R.id.et_register_store_address);
        et_register_store_desc = findViewById(R.id.et_register_store_desc);

    }

    public String getFieldValue(EditText et, String field_desc) {
        String result = "";
        if (!TextUtils.isEmpty(et.getText().toString())) {
            result = et.getText().toString();
        } else {
            error = true;
            error_component = error_component + field_desc + ", ";
        }
        return result;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK) {
            if (resultCode == RESULT_OK) {
                final Uri imageUri = data.getData();
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    storeImage = selectedImage;
                    Picasso.get()
                            .load(imageUri)
                            .fit()
                            .centerCrop()
                            .into(btn_register_store_image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void registerStore() {
    //create a file to write bitmap data
        String filename = storeId + ".png";
        Bitmap img = storeImage;
        File f = new File(getApplicationContext().getCacheDir(), filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        Bitmap bitmap = img;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody user_id =
                RequestBody.create(MediaType.parse("multipart/form-data"), UserCurrent.getUserId());

        RequestBody store_id =
                RequestBody.create(MediaType.parse("multipart/form-data"), storeId);

        RequestBody store_name =
                RequestBody.create(MediaType.parse("multipart/form-data"), storeName);

        MultipartBody.Part store_image =
                MultipartBody.Part.createFormData("store_image", f.getName(), RequestBody.create(MediaType.parse("image/*"), f));

        RequestBody store_email =
                RequestBody.create(MediaType.parse("multipart/form-data"), storeEmail);

        RequestBody store_phone =
                RequestBody.create(MediaType.parse("multipart/form-data"), storePhone);

        RequestBody store_province =
                RequestBody.create(MediaType.parse("multipart/form-data"), storeProvince);

        RequestBody store_city =
                RequestBody.create(MediaType.parse("multipart/form-data"), storeCity);

        RequestBody store_postcode =
                RequestBody.create(MediaType.parse("multipart/form-data"), storePostcode);

        RequestBody store_address =
                RequestBody.create(MediaType.parse("multipart/form-data"), storeAddress);

        RequestBody store_description =
                RequestBody.create(MediaType.parse("multipart/form-data"), storeDescription);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<StoreResponse> storeRegisterResponseCall = apiRequestInterface
                .registerStore(
                        user_id, store_id, store_image, store_name, store_email, store_phone, store_province,
                        store_city, store_postcode, store_address, store_description
                );
        storeRegisterResponseCall.enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                Toast.makeText(getApplicationContext(), "Registering Store...", Toast.LENGTH_SHORT).show();
                UserStoreCurrent.setStoreId(storeId);
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Registration failed. Try again later...", Toast.LENGTH_SHORT).show();
            }
        });
    }

}