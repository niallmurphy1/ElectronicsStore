package com.niall.electronicsstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.niall.electronicsstore.R;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.util.NumberFormatter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChildUserPurchasedItemsAdapter extends RecyclerView.Adapter<ChildUserPurchasedItemsAdapter.ViewHolder>{

    List<Item> purchasedItems;
    ViewHolder.OnItemListener onItemListener;


    public ChildUserPurchasedItemsAdapter(){

    }

    public void setPurchasedItems(List<Item> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    public ViewHolder.OnItemListener getOnItemListener() {
        return onItemListener;
    }

    public void setOnItemListener(ViewHolder.OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.purchased_item_card, parent, false);

        return new ViewHolder(view, onItemListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setData(purchasedItems.get(position));
    }

    @Override
    public int getItemCount() {
        return purchasedItems.size();
    }

    public ChildUserPurchasedItemsAdapter(ArrayList<Item> purchasedItems){
        this.purchasedItems = purchasedItems;
    }

    public List<Item> getPurchasedItems() {
        return purchasedItems;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView purchasedItemImage;
        TextView purchasedItemNameText;
        TextView purchasedItemQuantityText;
        TextView purchasedItemPriceText;
        OnItemListener onItemListener;


        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            this.purchasedItemImage = itemView.findViewById(R.id.purchased_item_child_rcv_item_image);
            this.purchasedItemNameText = itemView.findViewById(R.id.purchased_item_child_rcv_item_name);
            this.purchasedItemQuantityText = itemView.findViewById(R.id.purchased_item_child_rcv_item_quantity);
            this.purchasedItemPriceText = itemView.findViewById(R.id.purchased_item_child_rcv_item_price);

            this.onItemListener = onItemListener;

        }

        public void setData(Item item){

            Picasso.get()
                    .load(item.getImage())
                    .fit()
                    .centerCrop()
                    .into(purchasedItemImage);

            purchasedItemNameText.setText(item.getName());
            purchasedItemQuantityText.setText("Quantity: " + item.getCustQuant());
            purchasedItemPriceText.setText(NumberFormatter.formatPriceEuros(item.getPriceCents()));

            //TODO: need to fix this listener, getting null pointer exception
            itemView.setOnClickListener(v -> onItemListener.onItemClick( item));

        }


        public interface OnItemListener{

            public void onItemClick(Item item);
        }
    }
}
