package com.niall.electronicsstore.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.adapters.OrderHistoryAdapter;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.entities.PurchaseHistory;
import com.niall.electronicsstore.entities.RatingReview;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserPurchaseHistoryActivity extends AppCompatActivity implements OrderHistoryAdapter.OnItemClickListener {
    private DatabaseReference userPurchaseHistoryRef;
    private FirebaseAuth mainAuth;
    private String userId;
    private ArrayList<PurchaseHistory> purchasedItems;
    private RecyclerView mainRecycler;
    private OrderHistoryAdapter mainAdapter;
    private ArrayList<Item> allItems;
    private DatabaseReference itemRef;


    private NumberPicker rating;
    private EditText review;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_purchase_history);
        mainAuth = FirebaseAuth.getInstance();
        mainAuth.getCurrentUser();
        userId = mainAuth.getUid();
        userPurchaseHistoryRef = FirebaseDatabase.getInstance().getReference("User").child(userId).child("userPurchasedItems");

        itemRef = FirebaseDatabase.getInstance().getReference("Item");
        retrieveItemsFromFirebase();
        getPurchaseHistoryFromFirebase();
        setUpRCV();
    }
    public void retrieveItemsFromFirebase(){
        allItems = new ArrayList<>();
        allItems.clear();
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    Item item = keyNode.getValue(Item.class);
                    assert item != null;
                    item.setId(keyNode.getKey());
                    allItems.add(item);
                }
                Log.d("TAG", "onDataChange: Firebase all Items: " + allItems.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void getPurchaseHistoryFromFirebase(){
        purchasedItems = new ArrayList<>();
        userPurchaseHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                purchasedItems.clear();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
//
                    PurchaseHistory purchaseHistoryItem = keyNode.getValue(PurchaseHistory.class);
                    purchasedItems.add(purchaseHistoryItem);
//
                }
                mainAdapter.fillItems(buildAdapterItems(purchasedItems));
                //adapter.notifyDataSetChanged();
                Log.d("TAG", "onDataChange: Purchased items: " + purchasedItems.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void rateItem(String itemName){
        Log.d("TAG", "rateItem: Method started ");
        String key = findItem(itemName).getId();
        //TODO dialog here with star rating and comment edittext
        RatingReview ratingReview = new RatingReview();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_rate_item, null);
        rating = dialogView.findViewById(R.id.rating_number_picker);
        rating.setValue(1);
        rating.setMinValue(1);
        rating.setMaxValue(5);
        review = dialogView.findViewById(R.id.rating_review_comment_edit_text);

        builder.setView(dialogView);
        builder.setTitle("Review");
        builder.setPositiveButton("Rate item", (dialog, which) -> {

            ratingReview.setRating(rating.getValue());

            if(review != null) {
                ratingReview.setReview(review.getText().toString());
            }else{
                ratingReview.setReview("No comment left");
            }


            if(!key.isEmpty() || key == null) {

                DatabaseReference ratingReviewRef = FirebaseDatabase.getInstance().getReference("Item").child(key).child("ratingReview");
                String rKey = ratingReviewRef.push().getKey();
                ratingReviewRef.child(rKey).setValue(ratingReview).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "onSuccess: Item " + itemName + " reviewed successfully: " + ratingReview.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: Rating review failed " + e.getLocalizedMessage());
                    }
                });
            }

            Toast.makeText(this, "Successfully rated item: " + itemName, Toast.LENGTH_SHORT).show();
        }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
    public void setUpRCV(){
        mainRecycler = findViewById(R.id.activity_user_purchased_items_rcv);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        //Log.d("TAG", "setUpRCV: The purchased Items: " +purchasedItems.toString());
        mainAdapter = new OrderHistoryAdapter(this);
        mainRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mainRecycler.setAdapter(mainAdapter);
    }
    public List<OrderHistoryAdapter.OrderHistoryAdapterItem> buildAdapterItems(List<PurchaseHistory> purchaseHistoryList) {
        ArrayList<OrderHistoryAdapter.OrderHistoryAdapterItem> orderHistoryAdapterItems = new ArrayList<>();
        for (PurchaseHistory purchaseHistory : purchaseHistoryList) {
            orderHistoryAdapterItems.add(new OrderHistoryAdapter.DateItem(purchaseHistory.getDatePurchased()));
            for (Item item : purchaseHistory.getItemsPurchased()) {
                orderHistoryAdapterItems.add(OrderHistoryAdapter.OrderItem.createFrom(item, getResources()));
            }
        }
        return orderHistoryAdapterItems;
    }
    @Override
    public void onItemClick(OrderHistoryAdapter.OrderItem item) {
        if (item != null) {
            Log.d("TAG", "onShopListItemClick: item clicked!");
            Snackbar.make(findViewById(R.id.root), "You clicked " + item.getProduct(), Snackbar.LENGTH_SHORT).show();
            rateItem(item.getProduct());
        }
    }
    private Item findItem(String name) {
        for (Item item : allItems ) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

}