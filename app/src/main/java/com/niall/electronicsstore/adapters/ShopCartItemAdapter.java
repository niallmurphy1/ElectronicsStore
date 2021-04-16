package com.niall.electronicsstore.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.entities.Item;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopCartItemAdapter extends RecyclerView.Adapter<ShopCartItemAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Item> cartItems;

    public FirebaseAuth fAuth = FirebaseAuth.getInstance();
    public FirebaseUser fUser = fAuth.getCurrentUser();
    final String userId = fUser.getUid();
    private DatabaseReference userCartRef;


    public ShopCartItemAdapter() {


    }

    public ShopCartItemAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        cartItems = new ArrayList<>();

    }

    public List<Item> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Item> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.shop_cart_item_card, parent, false);
        return new ViewHolder(v);
    }

    public void addQuant(Item cartItem) {

        userCartRef = FirebaseDatabase.getInstance().getReference("User").child(userId).child("userShopCart");

        userCartRef.child(cartItem.getId()).child("custQuant").setValue(cartItem.getCustQuant() + 1);


    }

    public void removeQuant(Item cartItem) {

        userCartRef = FirebaseDatabase.getInstance().getReference("User").child(userId).child("userShopCart");

        if (cartItem.getCustQuant() > 1) {
            userCartRef.child(cartItem.getId()).child("custQuant").setValue(cartItem.getCustQuant() - 1);

        }


    }

    public void removeFromFirebase(Item cartItem) {

        userCartRef = FirebaseDatabase.getInstance().getReference("User").child(userId).child("userShopCart");


        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot keyNode : snapshot.getChildren()) {


                    if (keyNode.getKey().equals(cartItem.getId())) {

                        Log.d("TAG", "onDataChange: product to be deleted: " + userCartRef.child(keyNode.getKey()));

                        userCartRef.child(keyNode.getKey()).removeValue();


                        Log.d("TAG", "onDataChange: Item removed: " + userCartRef.child(keyNode.getKey()));
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(cartItems.get(position));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle object removed from firebase and rcv, with confirmation dialog

                Toast.makeText(v.getContext(), "Delete button clicked on : " + cartItems.get(position).getName(), Toast.LENGTH_SHORT).show();

                removeFromFirebase(cartItems.get(position));

            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //handle 1 less quantity, except in the case of quantity = 1, then call on delete method, change text view for quant and subtotal

                removeQuant(cartItems.get(position));

            }
        });

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //handle one more quantity, update Firebase and textviews
                //OR handle it after proceedToCheckout btn is clicked

                addQuant(cartItems.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView productTitle;
        TextView productPrice;
        ImageView productImage;

        ImageButton deleteButton;
        ImageButton removeButton;
        ImageButton addButton;

        TextView productQuantText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.shop_cart_rcv_title_text_field);
            productImage = itemView.findViewById(R.id.shop_cart_rcv_product_image);
            productPrice = itemView.findViewById(R.id.shop_cart_rcv_item_price);

            deleteButton = itemView.findViewById(R.id.shop_cart_rcv_delete_quant_btn);
            removeButton = itemView.findViewById(R.id.shop_cart_rcv_remove_quant_btn);
            addButton = itemView.findViewById(R.id.shop_cart_rcv_add_quant_btn);
            productQuantText = itemView.findViewById(R.id.shop_cart_rcv_item_quant_text);
        }

        public String formatPricePounds(double price) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE);
            return formatter.format(price);

        }

        public void setData(Item item) {
            Picasso.get()
                    .load(item.getImage())
                    .fit()
                    .centerCrop()
                    .into(productImage);
            productTitle.setText(item.getName());
            productPrice.setText(formatPricePounds((item.getPriceCents()) / 100.00));
            productQuantText.setText(item.getCustQuant() + "");


        }
    }

}
