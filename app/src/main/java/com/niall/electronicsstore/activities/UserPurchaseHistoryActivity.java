package com.niall.electronicsstore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.entities.Item;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserPurchaseHistoryActivity extends AppCompatActivity {


    private DatabaseReference userPurchaseHistoryRef;
    private FirebaseAuth mainAuth;
    private String userId;
    private ArrayList<Item> purchasedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_purchase_history);

        mainAuth = FirebaseAuth.getInstance();
        mainAuth.getCurrentUser();
        userId = mainAuth.getUid();
        userPurchaseHistoryRef = FirebaseDatabase.getInstance().getReference("User").child(userId).child("user-purchasedItems");
    }



    public void getPurchaseHistoryFromFirebase(){

        purchasedItems = new ArrayList<>();

        userPurchaseHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                purchasedItems.clear();

                for (DataSnapshot keyNode : snapshot.getChildren()) {

                    Item item = keyNode.getValue(Item.class);
                    item.setId(keyNode.getKey());
                    purchasedItems.add(item);
                }

                //adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    //TODO: allow user to view their previous purchases and to rate the items they've purchased through a dialog in rcv
}