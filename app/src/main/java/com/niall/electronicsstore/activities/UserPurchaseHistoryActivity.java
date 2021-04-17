package com.niall.electronicsstore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.adapters.ChildUserPurchasedItemsAdapter;
import com.niall.electronicsstore.adapters.UserPurchasedItemsAdapter;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.entities.PurchaseHistory;
import com.niall.electronicsstore.entities.RatingReview;

import java.util.ArrayList;

public class UserPurchaseHistoryActivity extends AppCompatActivity implements ChildUserPurchasedItemsAdapter.ViewHolder.OnItemListener {


    private DatabaseReference userPurchaseHistoryRef;
    private FirebaseAuth mainAuth;
    private String userId;
    private ArrayList<PurchaseHistory> purchasedItems;
    private RecyclerView mainRecycler;
    private UserPurchasedItemsAdapter mainAdapter;

    private ArrayList<Item> allItems;

    private DatabaseReference itemRef;


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

                //adapter.notifyDataSetChanged();

                Log.d("TAG", "onDataChange: Purchased items: " + purchasedItems.toString());

                setUpRCV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void rateItem(Item item){

        Log.d("TAG", "rateItem: Method started ");
        String key = "";
        //TODO dialog here with star rating and comment edittext
        RatingReview ratingReview = new RatingReview();

        ratingReview.setRating(5);

        ratingReview.setReview("meh");

        if(ratingReview.getReview().isEmpty() || ratingReview.getReview() == null){

            ratingReview.setReview("No comment left");
        }

        for(Item anItem: allItems){

            if(item.getName().equals(anItem.getName())){

                key = anItem.getId();
            }
        }

        if(!key.isEmpty() || key == null) {

            DatabaseReference ratingReviewRef = FirebaseDatabase.getInstance().getReference("Item").child(key).child("ratingReview");

            String rKey = ratingReviewRef.push().getKey();


            ratingReviewRef.child(rKey).setValue(ratingReview).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Log.d("TAG", "onSuccess: Item " + item.getName() + " reviewed successfully: " + ratingReview.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.e("TAG", "onFailure: Rating review failed " + e.getLocalizedMessage());
                }
            });
        }


    }


    public void setUpRCV(){

        mainRecycler = findViewById(R.id.activity_user_purchased_items_rcv);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        Log.d("TAG", "setUpRCV: The purchased Items: " +purchasedItems.toString());
        mainAdapter = new UserPurchasedItemsAdapter(purchasedItems);
        mainRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mainAdapter.setOnItemListener(this);
        mainRecycler.setAdapter(mainAdapter);


    }


    @Override
    public void onItemClick(Item item) {
        Log.d("TAG", "onShopListItemClick: item clicked!");
        Snackbar.make(this.getCurrentFocus(), "You clicked " + item.getName(), Snackbar.LENGTH_SHORT).show();

        rateItem(item);
    }


//    @Override
//    public void onShopListItemClick(View v, Item item) {
//
//        Log.d("TAG", "onShopListItemClick: item clicked!");
//        Snackbar.make(v, "You clicked " + item.getName(), Snackbar.LENGTH_SHORT).show();
//
//        rateItem(item);
//
//        //TODO: figure out how to call this method
//    }
//
//
//    @Override
//    public void onShopListItemClick(View v, int position) {
//
//    }
}