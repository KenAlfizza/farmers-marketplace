package com.kenazalfizza.farmersmarketplace.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.account.CartProductActivity;
import com.kenazalfizza.farmersmarketplace.api.response.cart.CartProduct;
import com.kenazalfizza.farmersmarketplace.api.response.cart.CartStore;
import com.kenazalfizza.farmersmarketplace.session.current.UserCartCurrent;

import java.util.ArrayList;

public class RecyclerViewCartStoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;

    public ArrayList<CartStore> storeList;

    public RecyclerViewCartStoreAdapter(ArrayList<CartStore> storeList) {
        this.storeList = storeList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart_store, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        populateItemRows((ItemViewHolder) viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return storeList == null ? 0 : storeList.size();
        // If the element of array is null, it will return zero, else return the size of array
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        // Viewholder of the cart store cards
        String storeId;
        Boolean storeAvailable;
        // Declare the view element to be changed here
        TextView tvStoreName;
        TextView tvStoreLocation;
        TextView tvStoreStatus;
        CardView cvStoreItem;

        ImageView iv_remove;



        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Finding the view element by id
            tvStoreName = itemView.findViewById(R.id.tv_store_name);
            tvStoreLocation = itemView.findViewById(R.id.tv_store_location);
            tvStoreStatus = itemView.findViewById(R.id.tv_status);
            cvStoreItem = itemView.findViewById(R.id.cv_cart_store);
            iv_remove = itemView.findViewById(R.id.iv_remove);

            cvStoreItem.setOnClickListener(new View.OnClickListener() {
                // Method when the user click the card item of the lists
                @Override
                public void onClick(View view) {
                    if (storeAvailable) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, CartProductActivity.class);
                        CartProductActivity.setStoreId(storeId);
                        context.startActivity(intent);
                        // Starting ProductActivity page
                    }
                }
            });

            iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index=-1;
                    for (CartStore s : storeList) {
                        if (s.getId().equals(storeId)) {
                            index = storeList.indexOf(s);
                        }
                    }
                    notifyItemRemoved(index);
                    storeList.remove(index);
                    UserCartCurrent.setCartStores(storeList); // Update Current User Cart

                    // Remove products with the same store id as the store removed
                    ArrayList<CartProduct> cartProducts = UserCartCurrent.getCartProducts();
                    for (CartProduct s : cartProducts) {
                        if (s.getStoreId().equals(storeId)) {
                            cartProducts.remove(s);
                        }
                    }
                    UserCartCurrent.setCartProducts(cartProducts);
                }
            });

        }
    }
    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        CartStore cartStore = storeList.get(position);


        String id = cartStore.getId();
        String name = cartStore.getName();
        String location = cartStore.getLocation();
        boolean available = cartStore.isAvailable();


        viewHolder.storeId = id;
        viewHolder.tvStoreName.setText(name);
        viewHolder.tvStoreLocation.setText(location);
        viewHolder.storeAvailable = available;

        if (!available) {
            viewHolder.tvStoreStatus.setVisibility(View.VISIBLE);
        }

    }
}