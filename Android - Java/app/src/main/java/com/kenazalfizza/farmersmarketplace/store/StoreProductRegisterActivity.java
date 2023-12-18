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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.activity.DashboardActivity;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.StatusResponse;
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

public class StoreProductRegisterActivity extends AppCompatActivity {
    private final int RESULT_LOAD_IMG = 100;


    EditText et_register_product_name, et_register_product_stock, et_register_product_price, et_register_product_description;
    String productId, productName, productStock, productPrice, productType, productDescription;
    Button btn_register_product;

    ImageButton btn_register_product_image;
    Bitmap productImage;
    boolean error;
    String error_component = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_register);

        elementsFindViewById();



        btn_register_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_component = "";
                error = false;
                setRegisterProductValues();

                if (error) {
                    error_component = error_component.substring(0,error_component.length()-2);
                    String error_message = error_component + " required";
                    Snackbar.make(findViewById(R.id.lay_product_register), error_message, Snackbar.LENGTH_SHORT).show();
                } else {
                    registerProduct();

                }
            }
        });

        btn_register_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK) {
            if (resultCode == RESULT_OK) {
                final Uri imageUri = data.getData();
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    productImage = selectedImage;
                    Picasso.get()
                            .load(imageUri)
                            .fit()
                            .centerCrop()
                            .into(btn_register_product_image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void registerProduct() {
        //create a file to write bitmap data
        String filename = productId + ".png";
        if (productImage == null) {
            productImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_category_vegetable);
        }
        Bitmap img = productImage;
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

        // Parts within the multipart request
        RequestBody user_id =
                RequestBody.create(MediaType.parse("multipart/form-data"), UserCurrent.getUserId());

        RequestBody store_id =
                RequestBody.create(MediaType.parse("multipart/form-data"), UserStoreCurrent.getStoreId());

        RequestBody id =
                RequestBody.create(MediaType.parse("multipart/form-data"), productId);

        MultipartBody.Part image =
                MultipartBody.Part.createFormData("image", f.getName(), RequestBody.create(MediaType.parse("image/*"), f));

        RequestBody name =
                RequestBody.create(MediaType.parse("multipart/form-data"), productName);

        RequestBody type =
                RequestBody.create(MediaType.parse("multipart/form-data"), productType);

        RequestBody price =
                RequestBody.create(MediaType.parse("multipart/form-data"), productPrice);

        RequestBody stock =
                RequestBody.create(MediaType.parse("multipart/form-data"), productStock);

        RequestBody desc =
                RequestBody.create(MediaType.parse("multipart/form-data"), productDescription);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<StatusResponse> registerProductResponseCall = apiRequestInterface.registerProduct(
                user_id, store_id, id, image, name, type, price, stock, desc);

        registerProductResponseCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                StatusResponse statusResponse = response.body();
                Log.d(null, statusResponse.toString());
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Log.d(null, t.toString());
            }
        });

    }

    protected void setRegisterProductValues() {
        IdGenerator generateId = new IdGenerator();
        productId = generateId.productId();
        productName = getFieldValue(et_register_product_name, "Name");
        productStock = getFieldValue(et_register_product_stock, "Quantity");
        productPrice = getFieldValue(et_register_product_price, "Price");
        if (productType == null) {
            error = true;
            error_component = error_component + "Type" + ", ";
        }
        productDescription = getFieldValue(et_register_product_description, "Description");
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

    protected void elementsFindViewById() {
        et_register_product_name = findViewById(R.id.et_register_product_name);
        et_register_product_stock = findViewById(R.id.et_register_product_stock);
        et_register_product_price = findViewById(R.id.et_register_product_price);
        et_register_product_description = findViewById(R.id.et_register_product_description);

        btn_register_product = findViewById(R.id.btn_register_product);

        btn_register_product_image = findViewById(R.id.ib_register_product_image);

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_product_type_vegetable:
                if (checked)
                    productType = "vegetable";
                    break;
            case R.id.radio_product_type_fruit:
                if (checked)
                    productType = "fruit";
                    break;
            case R.id.radio_product_type_animal:
                if (checked)
                    productType = "animal";
                    break;
            case R.id.radio_product_type_seed:
                if (checked)
                    productType = "seed";
                    break;
        }
        Log.d("null", productType);
    }

    /*

    public void registerProductOld() {
        String[] field = new String[8];
        field[0] = "user_id";
        field[1] = "store_id";
        field[2] = "product_id";
        field[3] = "product_name";
        field[4] = "product_quantity";
        field[5] = "product_price";
        field[6] = "product_type";
        field[7] = "product_description";

        String[] data = new String[8];
        data[0] = UserModel.getUserId();
        data[1] = StoreModel.getStoreId();
        data[2] = productId;
        data[3] = productName;
        data[4] = productStock;
        data[5] = productPrice;
        data[6] = productType;
        data[7] = productDescription;

        String preparedField = "";
        String preparedData = "";

        for (int i=0; i< field.length; i++) {
            preparedField = preparedField + field[i] + ",";
            preparedData = preparedData + "'" + data[i] + "'" + ",";
        }

        preparedField = preparedField.substring(0,preparedField.length()-1);
        preparedData = preparedData.substring(0,preparedData.length()-1);

        String[] post_field = new String[2];
        post_field[0] = "field";
        post_field[1] = "data";

        String[] post_data = new String[2];
        post_data[0] = preparedField;
        post_data[1] = preparedData;
        Log.d(null, post_data[0]);
        Log.d(null, post_data[1]);

        TransferData registerStore = new TransferData("http://192.168.1.5/class_test/products/productRegister.php", "POST", post_field, post_data);

        if (registerStore.startTransfer()) {
            // TODO: Add loading bar/circle
            if (registerStore.onComplete()) {
                String result = registerStore.getResult(); // Obtaining the result from database with the getResult() method
                Log.d(null, result);

                // Go to LoginActivity Page after the data is sent
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

     */
}