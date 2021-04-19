package com.niall.electronicsstore.adapters;
import android.content.res.Resources;
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


public class OrderHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<OrderHistoryAdapterItem> items = new ArrayList<>();
    private final OnItemClickListener listener;
    public OrderHistoryAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == R.layout.item_rcv_order_history_date) {
            return new DateViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        } else  {
            return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), listener);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  DateViewHolder) {
            ((DateViewHolder) holder).bind((DateItem) items.get(position));
        } else if (holder instanceof OrderViewHolder) {
            ((OrderViewHolder) holder).bind((OrderItem) items.get(position));
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    public int getItemViewType(int position) {
        OrderHistoryAdapterItem item = items.get(position);
        return item.type;
    }
    public void fillItems(List<OrderHistoryAdapterItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
    class DateViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTv;
        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.date_purchased_name_text);
        }
        public void bind(DateItem item) {
            dateTv.setText(item.getDate());
        }
    }
    static class OrderViewHolder extends RecyclerView.ViewHolder {


        ImageView purchasedItemImage;
        TextView purchasedItemNameText;
        TextView purchasedItemQuantityText;
        TextView purchasedItemPriceText;
        OnItemClickListener onItemListener;


        public OrderViewHolder(@NonNull View itemView, OnItemClickListener listener) {


            super(itemView);
            this.purchasedItemImage = itemView.findViewById(R.id.purchased_item_child_rcv_item_image);
            this.purchasedItemNameText = itemView.findViewById(R.id.purchased_item_child_rcv_item_name);
            this.purchasedItemQuantityText = itemView.findViewById(R.id.purchased_item_child_rcv_item_quantity);
            this.purchasedItemPriceText = itemView.findViewById(R.id.purchased_item_child_rcv_item_price);
            this.onItemListener = listener;
        }
        public void bind(OrderItem item) {


            Picasso.get()
                    .load(item.getImageUrl())
                    .fit()
                    .centerCrop()
                    .into(purchasedItemImage);
            purchasedItemNameText.setText(item.getProduct());
            purchasedItemQuantityText.setText(item.getQuantity());
            purchasedItemPriceText.setText(item.getPrice());
            //TODO: need to fix this listener, getting null pointer exception
            itemView.setOnClickListener(v -> onItemListener.onItemClick( item));
            itemView.setOnClickListener(view -> onItemListener.onItemClick(item));
        }
    }
    public static abstract class OrderHistoryAdapterItem {
        private int type;
        protected OrderHistoryAdapterItem(int type) {
            this.type = type;
        }
        public int getType() {
            return type;
        }
    }
    public static class DateItem extends OrderHistoryAdapterItem {
        private String date;
        public DateItem(String date) {
            super(R.layout.item_rcv_order_history_date);
            this.date = date;
        }
        public String getDate() {
            return date;
        }
    }
    public static class OrderItem extends OrderHistoryAdapterItem {


        private String itemId;
        private String imageUrl;
        private String product;
        private String price;
        private String quantity;

        private String uId;

        public String getuId() {
            return uId;
        }

        public void setuId(String uId) {
            this.uId = uId;
        }

        public OrderItem(String itemId, String imageUrl, String product, String price, String quantity) {
            super(R.layout.purchased_item_card);
            this.itemId = itemId;
            this.imageUrl = imageUrl;
            this.product = product;
            this.price = price;
            this.quantity = quantity;
        }
        public static OrderItem createFrom(Item item, Resources resources) {
            return new OrderItem(item.getId(),
                    item.getImage(),
                    item.getName(),
                    NumberFormatter.formatPriceEuros(item.getPriceCents()),
                    resources.getString(R.string.quantity, item.getCustQuant()));
        }
        public String getImageUrl() {
            return imageUrl;
        }
        public String getProduct() {
            return product;
        }
        public String getPrice() {
            return price;
        }
        public String getQuantity() {
            return quantity;
        }
        public String getItemId() {
            return itemId;
        }
    }
    public interface OnItemClickListener{
        void onItemClick(OrderItem item);
    }
}