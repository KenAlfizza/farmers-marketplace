package com.kenazalfizza.farmersmarketplace.store;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenazalfizza.farmersmarketplace.dialog.AuthDialogFragment;
import com.kenazalfizza.farmersmarketplace.CryptoHash;
import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.dialog.RemoveDialogFragment;
import com.kenazalfizza.farmersmarketplace.activity.DashboardActivity;
import com.kenazalfizza.farmersmarketplace.api.ApiRequestInterface;
import com.kenazalfizza.farmersmarketplace.api.response.StatusResponse;
import com.kenazalfizza.farmersmarketplace.session.current.UserCurrent;
import com.kenazalfizza.farmersmarketplace.session.current.UserStoreCurrent;
import com.kenazalfizza.farmersmarketplace.model.Product;
import com.kenazalfizza.farmersmarketplace.model.StoreModel;
import com.kenazalfizza.farmersmarketplace.model.UserModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreProductUpdateActivity extends FragmentActivity
        implements RemoveDialogFragment.RemoveDialogListener, AuthDialogFragment.AuthDialogListener
    {
    String baseURL = "http://192.168.1.5/";
    private final int RESULT_LOAD_IMG = 100;

    ImageButton ib_update_productImage;
    EditText et_update_productName, et_update_productStock, et_update_productPrice, et_update_productDescription;
    Button btn_update_product, btn_remove_product;
    RadioButton rb_productType;
    String updatedProductType, productType;
    static Product product = new Product();
    Bitmap productImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);

        elementsFindViewById();

        autofillEditTextProductDetails(product);
        getProductImage();

        btn_update_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProductData(product);
                updateProduct();
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

        btn_remove_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemoveDialog();

            }
        });

        ib_update_productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
    }

    public static void setProduct(Product p) {
        product = p;
    }

    protected void elementsFindViewById() {
        et_update_productName = findViewById(R.id.et_update_product_name);
        et_update_productStock = findViewById(R.id.et_update_product_stock);
        et_update_productPrice = findViewById(R.id.et_update_product_price);
        et_update_productDescription = findViewById(R.id.et_update_product_description);
        ib_update_productImage = findViewById(R.id.ib_update_product_image);
        btn_update_product = findViewById(R.id.btn_update_product);
        btn_remove_product = findViewById(R.id.btn_remove_product);

    }
/*
    protected Product getProduct(String productId) {

        String[] field = {"product_id"};
        String[] data = {productId};
        Product[] products;

        TransferData getProduct = new TransferData("http://192.168.1.5/class_test/products/productRetrieve.php?method=id", "POST", field, data);
        if (getProduct.startTransfer()) {

            if (getProduct.onComplete()) {
                // Refresh the fragment to load a new data
                String json = getProduct.getResult(); // JSON Array from Webserver
                Gson gson = new Gson();

                products = gson.fromJson(json, Product[].class);
                Log.d(null, json); // Logcat JSON Result From Server
                return products[0];
            }
        }
        return null;
    }

 */
    protected void getProductImage() {
        String resUrl = baseURL + "marketplace/product/res/" + product.getProductId() + ".png";
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                productImage = bitmap;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        double token=0;
        token=Math.random();
        resUrl = resUrl + "?"+token;
        // Token is used to change the url, forcing Picasso to update image

        Picasso.get()
                .load(resUrl)
                .into(target);

        Picasso.get()
                .load(resUrl)
                .fit()
                .centerCrop()
                .into(ib_update_productImage);
    }

    protected void autofillEditTextProductDetails(Product product) {
        et_update_productName.setText(product.getProductName());
        et_update_productStock.setText(String.valueOf(product.getProductStock()));
        et_update_productPrice.setText(String.valueOf(product.getProductPrice()));
        et_update_productDescription.setText(product.getProductDescription());
        productType = product.getProductType();


        checkRadioButton();
    }

    public void checkRadioButton() {
        switch (productType) {
            case "vegetable":
                rb_productType = findViewById(R.id.radio_product_type_vegetable);
                rb_productType.setChecked(true);
                updatedProductType = "vegetable";
                break;
            case "fruit":
                rb_productType = findViewById(R.id.radio_product_type_fruit);
                rb_productType.setChecked(true);
                updatedProductType = "fruit";
                break;
            case "animal":
                rb_productType = findViewById(R.id.radio_product_type_animal);
                rb_productType.setChecked(true);
                updatedProductType = "animal";
                break;
            case "seed":
                rb_productType = findViewById(R.id.radio_product_type_seed);
                rb_productType.setChecked(true);
                updatedProductType = "seed";
                break;
        }
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked

        switch(view.getId()) {
            case R.id.radio_product_type_vegetable:
                if (checked)
                    updatedProductType = "vegetable";
                break;
            case R.id.radio_product_type_fruit:
                if (checked)
                    updatedProductType = "fruit";
                break;
            case R.id.radio_product_type_animal:
                if (checked)
                    updatedProductType = "animal";
                break;
            case R.id.radio_product_type_seed:
                if (checked)
                    updatedProductType = "seed";
                break;
        }
        Log.d("null", updatedProductType);
    }

    protected void updateProductData(Product product) {
        product.setUserId(UserModel.getUserId());
        product.setStoreId(StoreModel.getStoreId());
        product.setProductName(et_update_productName.getText().toString());
        product.setProductType(updatedProductType);
        product.setProductStock(Integer.parseInt(et_update_productStock.getText().toString()));
        product.setProductPrice(Integer.parseInt(et_update_productPrice.getText().toString()));
        product.setProductDescription(et_update_productDescription.getText().toString());
    }

    protected void updateProduct() {
        //create a file to write bitmap data
        String filename = product.getProductId() + ".png";
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
                RequestBody.create(MediaType.parse("multipart/form-data"), product.getProductId());

        MultipartBody.Part image =
                MultipartBody.Part.createFormData("image", f.getName(), RequestBody.create(MediaType.parse("image/*"), f));

        RequestBody name =
                RequestBody.create(MediaType.parse("multipart/form-data"), product.getProductName());

        RequestBody type =
                RequestBody.create(MediaType.parse("multipart/form-data"), product.getProductType());

        RequestBody price =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(product.getProductPrice()));

        RequestBody stock =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(product.getProductStock()));

        RequestBody desc =
                RequestBody.create(MediaType.parse("multipart/form-data"), product.getProductDescription());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);


        Call<StatusResponse> registerProductResponseCall = apiRequestInterface.updateProduct(
                user_id, store_id, id, image, name, type, price, stock, desc);

        registerProductResponseCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                StatusResponse statusResponse = response.body();
                Log.d(null, statusResponse.toString());
                finish();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Log.d(null, t.toString());
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
                            .into(ib_update_productImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        }
    }

/*
    private void productUpdateOld(Product product) {
        Gson gson = new Gson();
        String productJson = gson.toJson(product);


        URL url = null;
        try {
            url = new URL("http://192.168.1.5/class_test/products/productUpdate.php");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = productJson.getBytes(StandardCharsets.UTF_8);
                //os.write(input, 0, input.length);
                Log.d(null, Arrays.toString(input));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    protected String[] setUpdatedFieldData() {

        String field = "user_id" + ",";
        String data = UserModel.getUserId() + ",";

        field = field + "product_id" + ",";
        data = data + productId + ",";
        // Assign New Value to Variables if the box is not empty
        if (!TextUtils.isEmpty(et_update_productName.getText().toString())) {
            productName = et_update_productName.getText().toString();
            field = field + "product_name" + ",";
            data = data + productName + ",";
        }
        if (!updatedProductType.equals(productType)) {
            field = field + "product_name" + ",";
            data = data + productType + ",";
        }

        if (!TextUtils.isEmpty(et_update_productStock.getText().toString())) {
            productName = et_update_productStock.getText().toString();
            field = field + "product_quantity" + ",";
            data = data + productStock + ",";
        }
        if (!TextUtils.isEmpty(et_update_productPrice.getText().toString())) {
            productName = et_update_productPrice.getText().toString();
            field = field + "product_price" + ",";
            data = data + productPrice + ",";
        }
        if (!TextUtils.isEmpty(et_update_productDescription.getText().toString())) {
            productName = et_update_productDescription.getText().toString();
            field = field + "product_description" + ",";
            data = data + productDescription + ",";
        }

        return new String[]{"0"};
    }


 */
    public void showRemoveDialog() {
        RemoveDialogFragment removeDialog = new RemoveDialogFragment();
        removeDialog.setTitle("Remove Product");
        removeDialog.setMessage("This will product will be permanently removed");
        removeDialog.show(getSupportFragmentManager(), "RemoveDialogFragment");
    }

    public void showAuthDialog() {
        AuthDialogFragment authDialog = new AuthDialogFragment();
        authDialog.show(getSupportFragmentManager(), "AuthDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog instanceof RemoveDialogFragment) {
            showAuthDialog();

        } else if (dialog instanceof AuthDialogFragment) {
            String auth_password = ((AuthDialogFragment) dialog).getAuthPassword();
            CryptoHash cryptoHash = new CryptoHash(auth_password);
            auth_password = cryptoHash.run();
            removeProduct(UserCurrent.getUserId(), auth_password, UserStoreCurrent.getStoreId(), product.getProductId());
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    protected void removeProduct(String userId, String userPassword, String storeId, String productId) {
        RequestBody user_id =
                RequestBody.create(MediaType.parse("multipart/form-data"), userId);

        RequestBody user_password =
                RequestBody.create(MediaType.parse("multipart/form-data"), userPassword);

        RequestBody store_id =
                RequestBody.create(MediaType.parse("multipart/form-data"), storeId);

        RequestBody product_id =
                RequestBody.create(MediaType.parse("multipart/form-data"), productId);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiRequestInterface apiRequestInterface = retrofit.create(ApiRequestInterface.class);

        Call<StatusResponse> removeProductStatusResponseCall = apiRequestInterface.removeProduct(user_id, user_password, store_id, product_id);

        removeProductStatusResponseCall.enqueue(new Callback<StatusResponse>() {
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
}