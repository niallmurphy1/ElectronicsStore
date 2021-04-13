package com.niall.electronicsstore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.adapters.ShopCartItemAdapter;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.util.NumberFormatter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;


public class ShoppingCartFragment extends Fragment {


    private RecyclerView recyclerView;
    private ShopCartItemAdapter adapter;
    private List<Item> cartItems = new ArrayList<>();
    private List<Item> allItems = new ArrayList<>();

    private Button confirmAndPayBtn;
    private TextView subtotalText;
    private TextView numItemsText;

    private NumberFormatter formatter;

    //Firebase variables
    private DatabaseReference itemRef;
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

        formatter = new NumberFormatter();

        userCartRef = FirebaseDatabase.getInstance().getReference("User").child(userId).child("user-shopCart");
        itemRef = FirebaseDatabase.getInstance().getReference("Item");


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

        subtotalText = view.findViewById(R.id.shop_cart_subtotal_text);
        numItemsText = view.findViewById(R.id.shop_cart_num_items);

        retrieveItemsFromFirebase();
        retrieveCartFromFirebase();

        confirmAndPayBtn = view.findViewById(R.id.confirm_and_pay_btn);

        confirmAndPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmAndPay();
            }
        });
    }

    public void getSubtotalAndNum(List<Item> items){

        int numItems = 0;
        int subtotalCents = 0;


        for (Item item : items) {


            subtotalCents += item.getPriceCents() * item.getCustQuant();


            numItems += item.getCustQuant();
        }
        subtotalText.setText("Subtotal: " +formatter.formatPriceEuros(subtotalCents));

        numItemsText.setText("Total items(s): " + numItems);
    }


    private void confirmAndPay() {

        for(Item allItem: allItems){

            for(Item cartItem: cartItems){
                if(allItem.getName().equals(cartItem.getName())){
                    itemRef.child(allItem.getId()).child("stocklevel").setValue(allItem.getStockLevel() - cartItem.getCustQuant());
                }
            }
        }

        //start confirmation screen activity


    }

    void retrieveItemsFromFirebase(){

       allItems.clear();

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot keyNode: snapshot.getChildren()){

                    Item item = keyNode.getValue(Item.class);
                    assert item != null;
                    item.setId(keyNode.getKey());
                    allItems.add(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void retrieveCartFromFirebase(){

        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cartItems.clear();

                for(DataSnapshot keyNode: snapshot.getChildren()){

                    Item item = keyNode.getValue(Item.class);
                    assert item != null;
                    item.setId(keyNode.getKey());
                    cartItems.add(item);
                }

                adapter.setCartItems(cartItems);
                adapter.notifyDataSetChanged();

                getSubtotalAndNum(cartItems);




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
            recyclerView.setAdapter(adapter);


    }
}