package com.niall.electronicsstore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.adapters.OrderHistoryAdapter;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.entities.PurchaseHistory;
import com.niall.electronicsstore.entities.User;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AllUserPurchaseHistoryActivity extends AppCompatActivity implements OrderHistoryAdapter.OnItemClickListener {



    private DatabaseReference userRef;
    private ArrayList<User> allUsers;

    private ArrayList<PurchaseHistory> purchasedItems;
    private RecyclerView mainRecycler;
    private OrderHistoryAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_purchase_history);

        userRef = FirebaseDatabase.getInstance().getReference("User");

        allUsers = new ArrayList<>();
        getAllUsersPurchaseHistory();


        purchasedItems = new ArrayList<>();


        setUpRCV();
    }

    public void getAllUsersPurchaseHistory(){

        ArrayList<String> userIds = new ArrayList<>();
        allUsers.clear();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot keyNode: snapshot.getChildren()){

                    String userId = keyNode.getKey();

                    userIds.add(userId);
//                    assert user != null;
//                    user.setUserId(userId);
//                    allUsers.add(user);
                }
                Log.d(TAG, "onDataChange: user IDs: " + userIds.toString());


                getUserPurchases(userIds);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUserPurchases(ArrayList<String> userIds){

        //ArrayList<PurchaseHistory> purchasedItems = new ArrayList<>();
        //ArrayList<User> users = new ArrayList<>();
        purchasedItems.clear();

        for(String uId: userIds){

            userRef.child(uId).child("userPurchasedItems").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot keyNode: snapshot.getChildren()){

                        PurchaseHistory purchaseHistory = keyNode.child("userPurchasedItems").getValue(PurchaseHistory.class);

                        purchasedItems.add(purchaseHistory);

                    }

                    mainAdapter.fillItems(buildAdapterItems(purchasedItems));


                    //mainAdapter.notifyDataSetChanged();
                    //getUserDetails(userIds);


                    Log.d(TAG, "onDataChange: These are the purchase histories " + purchasedItems);

                   // Log.d(TAG, "onDataChange: These are the Users " + users);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public List<OrderHistoryAdapter.OrderHistoryAdapterItem> buildAdapterItems(List<PurchaseHistory> purchaseHistoryList) {
        ArrayList<OrderHistoryAdapter.OrderHistoryAdapterItem> orderHistoryAdapterItems = new ArrayList<>();
        for (PurchaseHistory purchaseHistory : purchaseHistoryList) {

            //TODO: find put why this is null
            orderHistoryAdapterItems.add(new OrderHistoryAdapter.DateItem(purchaseHistory.getDatePurchased()));
            for (Item item : purchaseHistory.getItemsPurchased()) {
                orderHistoryAdapterItems.add(OrderHistoryAdapter.OrderItem.createFrom(item, getResources()));
            }
        }
        return orderHistoryAdapterItems;
    }


    public void setUpRCV(){
        mainRecycler = findViewById(R.id.all_user_purchases_rcv);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        Log.d("TAG", "setUpRCV: The purchased Items: " +purchasedItems.toString());
        mainAdapter = new OrderHistoryAdapter(this);
        mainRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mainRecycler.setAdapter(mainAdapter);
    }



    @Override
    public void onItemClick(OrderHistoryAdapter.OrderItem item) {


    }
}