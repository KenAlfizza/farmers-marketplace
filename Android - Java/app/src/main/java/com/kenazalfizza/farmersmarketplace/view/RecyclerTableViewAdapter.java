package com.kenazalfizza.farmersmarketplace.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kenazalfizza.farmersmarketplace.R;
import com.kenazalfizza.farmersmarketplace.activity.ProductActivity;

import java.util.List;

public class RecyclerTableViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<String> mItemId;
    public List<String> mItemList;
    public List<String> mItemPrice;
    //public List<Bitmap> mItemImage;

    public RecyclerTableViewAdapter(List<String> itemId, List<String> itemList, List<String> itemPrice) { //,List<Bitmap> itemImage) {

        mItemId = itemId;
        mItemList = itemList;
        mItemPrice = itemPrice;
        //mItemImage = itemImage;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_table, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
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

        public String id;
        // Declare the view element to be changed here
        TextView tvItem;
        TextView tvPrice;
        //ImageView ivImage;

        CardView cvItemList;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Finding the view element by id
            tvItem = itemView.findViewById(R.id.item);
            tvPrice = itemView.findViewById(R.id.price);
            //ivImage = itemView.findViewById(R.id.image);

            cvItemList = itemView.findViewById(R.id.cv_item_card);

            cvItemList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ProductActivity.class);
                    ProductActivity.setProductId(id);
                    context.startActivity(intent);
                }
            });
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        // Declaring progressBar
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        String id = mItemId.get(position);
        String item = mItemList.get(position);
        String price = mItemPrice.get(position);
        //Bitmap image = mItemImage.get(position);

        viewHolder.id = id;
        viewHolder.tvItem.setText(item);
        viewHolder.tvPrice.setText("Rp " + price);
        //viewHolder.ivImage.setImageBitmap(image);


    }
}