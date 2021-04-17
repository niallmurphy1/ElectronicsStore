package com.niall.electronicsstore.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.activities.UserPurchaseHistoryActivity;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.entities.PurchaseHistory;
import com.niall.electronicsstore.entities.RatingReview;

import java.util.ArrayList;
import java.util.List;

public class UserPurchasedItemsAdapter extends RecyclerView.Adapter<UserPurchasedItemsAdapter.ViewHolder> {


   private List<PurchaseHistory> purchaseHistoryItems;
   private ChildUserPurchasedItemsAdapter.ViewHolder.OnItemListener onItemListener;

   public UserPurchaseHistoryActivity userPurchaseHistoryActivity = new UserPurchaseHistoryActivity();

   public List<PurchaseHistory> getPurchaseHistoryItems() {
      return purchaseHistoryItems;
   }

   public void setPurchaseHistoryItems(List<PurchaseHistory> purchaseHistoryItems) {
      this.purchaseHistoryItems = purchaseHistoryItems;
   }

   public ChildUserPurchasedItemsAdapter.ViewHolder.OnItemListener getOnItemListener() {
      return onItemListener;
   }

   public void setOnItemListener(ChildUserPurchasedItemsAdapter.ViewHolder.OnItemListener onItemListener) {
      this.onItemListener = onItemListener;
   }

   public UserPurchasedItemsAdapter(List<PurchaseHistory> purchaseHistoryItems){
      this.purchaseHistoryItems = purchaseHistoryItems;
   }




   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
      View view = layoutInflater.inflate(R.layout.parent_rcv_date_purchased_item, parent, false);
      return new ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

      PurchaseHistory purchaseHistory = purchaseHistoryItems.get(position);
      String datePurchased = purchaseHistory.getDatePurchased();

      List<Item> purchasedItems = purchaseHistory.getItemsPurchased();

      holder.datePurchasedTextView.setText(datePurchased);

      ChildUserPurchasedItemsAdapter childUserPurchasedItemsAdapter = new ChildUserPurchasedItemsAdapter((ArrayList<Item>) purchasedItems);
     // childUserPurchasedItemsAdapter.setOnItemListener(this);

      holder.childRecycler.setAdapter(childUserPurchasedItemsAdapter);

   }

   @Override
   public int getItemCount() {
      return purchaseHistoryItems.size();
   }







   //TODO: create a rcv adapter for this, get user dates and
   // add date format to this, so that date is in rcv and


   //TODO: create this adapter with on click listener for 1-5 ratings


   class ViewHolder extends RecyclerView.ViewHolder{

      TextView datePurchasedTextView;
      RecyclerView childRecycler;

      public ViewHolder(@NonNull View itemView) {
         super(itemView);

         datePurchasedTextView = itemView.findViewById(R.id.date_purchased_name_text);
         childRecycler = itemView.findViewById(R.id.purchased_items_child_rcv);
      }
   }


   //TODO: doing this bollocks in the adapter cus I haven't figured out how to call the onClick method from the activity class cus I suck


}
