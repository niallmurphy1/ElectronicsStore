package com.niall.electronicsstore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

import static android.content.ContentValues.TAG;

public class AdminFragment extends Fragment {


    private DatabaseReference userRef;
    private ArrayList<User> allUsers;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userRef = FirebaseDatabase.getInstance().getReference("User");

        allUsers = new ArrayList<>();
        getAllUsersPurchaseHistory();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //TODO: view all purchase histories, maybe in an rcv with child rcvs, also update stock levels.
    // Give the command pattern a go here

    //TODO: get all users very difficult to get all users stuff



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

        ArrayList<Item> purchasedItems = new ArrayList<>();

        for(String uId: userIds){

            userRef.child(uId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                   // User aUser = snapshot.getValue(User.class);
                   String email = snapshot.child("email").getValue(String.class);


                    //Item item = snapshot.child("userPurchasedItems").getChildren();
//                   purchasedItems.add(item);



                   // Log.d(TAG, "onDataChange: Please work, the user: " + aUser.toString());
                    Log.d(TAG, "onDataChange: Purchased items: " + purchasedItems.toString());
                    Log.d(TAG, "onDataChange: Please work, the user: " + email.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}