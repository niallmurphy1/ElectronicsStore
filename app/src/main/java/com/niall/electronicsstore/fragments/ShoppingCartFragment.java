package com.niall.electronicsstore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.adapters.ShopCartItemAdapter;
import com.niall.electronicsstore.entities.Item;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCartFragment extends Fragment {


    private RecyclerView recyclerView;
    private ShopCartItemAdapter adapter;
    private List<Item> cartItems = new ArrayList<>();

    //Firebase variables
    private DatabaseReference userCartRef;
    private FirebaseAuth mainAuth;
    private String userId;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //initialise Firebase variables
        mainAuth = FirebaseAuth.getInstance();
        userId = mainAuth.getUid();

        userCartRef = FirebaseDatabase.getInstance().getReference("User").child(userId).child("user-shopCart");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpRCV();

        retrieveCartFromFirebase();
    }

    public void retrieveCartFromFirebase(){

        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cartItems.clear();

                for(DataSnapshot keyNode: snapshot.getChildren()){

                    Item item = keyNode.getValue(Item.class);
                    item.setId(keyNode.getKey());
                    cartItems.add(item);
                }

                adapter.setCartItems(cartItems);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void setUpRCV(){

            recyclerView = getView().findViewById(R.id.shop_cart_rcv);
            adapter = new ShopCartItemAdapter(getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            //adapter.addItems(items);
            recyclerView.setAdapter(adapter);


    }
}