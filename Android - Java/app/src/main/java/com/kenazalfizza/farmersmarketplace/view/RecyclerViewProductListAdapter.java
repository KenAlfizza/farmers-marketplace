package com.kenazalfizza.farmersmarketplace.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.activity.ProductActivity;
import com.kenazalfizza.farmersmarketplace.model.ProductListItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    String baseURL;

    // Array declaration to store the data of each item
    public List<ProductListItem> mItemList;

    public RecyclerViewProductListAdapter(List<ProductListItem> itemList, String baseURL) {
        // Assigning the value of array
        mItemList = itemList;
        this.baseURL = baseURL;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Check if the view type item of the lists is a ViewItem
        if (viewType == VIEW_TYPE_ITEM) {
            // If true
            View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_product_item, parent, false);
            // Inflating the view with layout of R.layout.list_product_item
            return new ItemViewHolder(view);
            // Returning the view of ItemViewHolder
        } else {
            // If false
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress, parent, false);
            // Inflating the view with layout of R.layout.progress
            return new LoadingViewHolder(view);
            // Return the view of LoadingViewHolder
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        // When the program is binding its viewHolder
        if (viewHolder instanceof ItemViewHolder) {
            // If the instance bind is a Recycler.ViewHolder object, it will populate the lists
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            // If the instance bind is a LoadingViewHolder object, it will show loading view
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        String id;
        // Declare the view element to be changed here
        TextView tvItem;
        TextView tvPrice;
        TextView tvLocation;
        CardView cvItemList;
        ImageView ivImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Finding the view element by id
            tvItem = itemView.findViewById(R.id.item);
            tvPrice = itemView.findViewById(R.id.price);
            tvLocation = itemView.findViewById(R.id.location);

            cvItemList = itemView.findViewById(R.id.cv_item_list);

            ivImage = itemView.findViewById(R.id.image);

            cvItemList.setOnClickListener(new View.OnClickListener() {
                // Method when the user click the card item of the lists
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ProductActivity.class);
                    ProductActivity.setProductId(id);
                    context.startActivity(intent);
                    // Starting ProductActivity page
                }
            });

        }

    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        // Initialising progressBar
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            // finding view of progressBar by id
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed when the page is loading more item

    }
    @SuppressLint("SetTextI18n")
    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        Log.d(null, mItemList.toString());
        // Getting the element of the arrays at index
        // corresponding position to the item on the lists
        ProductListItem productListItemItem = mItemList.get(position);

        // Setting the element of the list items
        viewHolder.id = productListItemItem.getId();
        viewHolder.tvItem.setText(productListItemItem.getName());
        viewHolder.tvLocation.setText(productListItemItem.getLocation());
        viewHolder.tvPrice.setText("Rp" + productListItemItem.getPrice());
        //viewHolder.ivImage.setImageBitmap(image);

        String resURL = baseURL + "marketplace/product/res/" + productListItemItem.getId() + ".png";
        Picasso.get()
                .load(resURL)
                .fit()
                .centerCrop()
                .into(viewHolder.ivImage);
    }
}