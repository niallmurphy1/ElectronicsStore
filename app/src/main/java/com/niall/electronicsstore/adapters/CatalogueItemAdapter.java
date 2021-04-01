package com.niall.electronicsstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.niall.electronicsstore.R;
import com.niall.electronicsstore.entities.Item;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CatalogueItemAdapter extends RecyclerView.Adapter<CatalogueItemAdapter.ViewHolder>{

    private LayoutInflater layoutInflater;
    private ArrayList<Item> items = new ArrayList<>();
   // private ArrayList<Item> filteredItems = new ArrayList<>();
    private ViewHolder.OnItemListener onItemListener;


    public CatalogueItemAdapter(Context context, ViewHolder.OnItemListener onItemListener ){
        this.layoutInflater = LayoutInflater.from(context);
        this.onItemListener = onItemListener;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_card, parent, false);

        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView itemImage;
        public TextView titleText;
        public TextView manufacturerText;
        public TextView priceText;
        public TextView categoryText;

        OnItemListener onItemListener;
        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image_view);
            titleText = itemView.findViewById(R.id.item_title_text);
            manufacturerText = itemView.findViewById(R.id.manufacturer_text_view);
            priceText = itemView.findViewById(R.id.price_text_view);
            categoryText = itemView.findViewById(R.id.category_text_view);
            this.onItemListener = onItemListener;
        }

        public void setData(Item item) {

            Picasso.get()
                    .load(item.getImage())
                    .fit()
                    .centerCrop()
                    .into(itemImage);
            double price = (item.getPriceCents()/100.00);
            priceText.setText(formatPriceEuro(price));
            manufacturerText.setText(item.getManufacturer());
            categoryText.setText(item.getCategory());
            titleText.setText(item.getName());

        }

        public String formatPriceEuro(double price){
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE);
            return formatter.format(price);

        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }

        public interface OnItemListener{

            public void onItemClick(int position);
        }
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }



    public void setOnItemListener(ViewHolder.OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public void addItems(ArrayList<Item> theItems){
        this.items = theItems;
    }
}
