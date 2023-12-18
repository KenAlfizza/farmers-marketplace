package com.kenazalfizza.farmersmarketplace.view;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.activity.ProductActivity;
import com.kenazalfizza.farmersmarketplace.api.response.cart.CartProduct;
import com.kenazalfizza.farmersmarketplace.session.current.UserCartCurrent;

import java.util.ArrayList;

public class RecyclerViewCartProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<CartProduct> mCartProductArray;
    public String storeId;


    public RecyclerViewCartProductAdapter(ArrayList<CartProduct> rowsArrayProduct, String storeId) {
        mCartProductArray = rowsArrayProduct;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart_product, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mCartProductArray == null ? 0 : mCartProductArray.size();
        // If the element of array is null, it will return zero, else return the size of array
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        String productId;
        int productStock;
        int productQuantity;
        Boolean productAvailable;
        // Declare the view element to be changed here
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductStock;

        EditText etProductQuantity;

        CardView cvCartProduct;

        ImageView iv_remove;

        //ImageView ivImage;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Finding the view element by id
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            etProductQuantity = itemView.findViewById(R.id.et_product_quantity);
            tvProductStock = itemView.findViewById(R.id.tv_product_stock);
            cvCartProduct = itemView.findViewById(R.id.cv_cart_product);
            etProductQuantity = itemView.findViewById(R.id.et_product_quantity);

            iv_remove = itemView.findViewById(R.id.iv_remove);
            //ivImage = itemView.findViewById(R.id.image);

            cvCartProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (productAvailable) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, ProductActivity.class);
                        ProductActivity.setProductId(productId);
                        context.startActivity(intent);
                    }
                }
            });

            etProductQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String txt = etProductQuantity.getText().toString();
                    if (!txt.equals("")) {
                        productQuantity = Integer.parseInt(txt);
                        if (productStock < productQuantity) {
                            Toast.makeText(itemView.getContext(), "Quantity is greater than available stock", Toast.LENGTH_SHORT).show();
                            etProductQuantity.setText(String.valueOf(productStock));
                            productQuantity = productStock;
                        }
                        for (CartProduct p : mCartProductArray) {
                            if (p.getId().equals(productId)) {
                                p.setQuantity(productQuantity);
                            }
                        }
                    }

                }
            });

            iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index=-1;
                    for (CartProduct p : mCartProductArray) {
                        if (p.getId().equals(productId)) {
                            index = mCartProductArray.indexOf(p);
                        }
                    }
                    notifyItemRemoved(index);
                    mCartProductArray.remove(index);
                    UserCartCurrent.setCartProducts(mCartProductArray); // Update Current User Cart
                }
            });
        }
    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        CartProduct cartProduct = mCartProductArray.get(position);
        Log.d(null, cartProduct.toString());

        String id = cartProduct.getId();
        String name = cartProduct.getName();
        int price = cartProduct.getPrice();
        int quant = cartProduct.getQuantity();
        int stock = cartProduct.getStock();
        boolean available = cartProduct.isAvailable();


        viewHolder.productId = id;
        viewHolder.productStock = stock;
        viewHolder.tvProductName.setText(name);
        viewHolder.tvProductPrice.setText(String.valueOf(price));
        viewHolder.tvProductStock.setText(String.valueOf(stock));

        if (quant > stock) {
            viewHolder.etProductQuantity.setText(String.valueOf(stock));
        } else {
            viewHolder.etProductQuantity.setText(String.valueOf(quant));
        }
        viewHolder.productAvailable = available;

        //viewHolder.ivImage.setImageBitmap(image);

    }

}