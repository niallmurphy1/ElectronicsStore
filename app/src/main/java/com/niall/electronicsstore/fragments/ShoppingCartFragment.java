package com.niall.electronicsstore.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.adapters.ShopCartItemAdapter;
import com.niall.electronicsstore.decorator.AdminCoupon;
import com.niall.electronicsstore.decorator.Coupon;
import com.niall.electronicsstore.decorator.HalfPrice;
import com.niall.electronicsstore.decorator.TenPercent;
import com.niall.electronicsstore.decorator.TwentyPercent;
import com.niall.electronicsstore.decorator.UserCoupon;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.entities.User;
import com.niall.electronicsstore.util.NumberFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class ShoppingCartFragment extends Fragment {


    private RecyclerView recyclerView;
    private ShopCartItemAdapter adapter;
    private List<Item> cartItems = new ArrayList<>();
    private List<Item> allItems = new ArrayList<>();

    private Button adminDiscountBtn;
    private Button confirmAndPayBtn;
    private Button applyCouponBtn;
    private TextView subtotalText;
    private TextView numItemsText;

    private int subtotalCents = 0;
    private NumberFormatter formatter;

    private Coupon userCoupon;


    private User currentUser;

    //Firebase variables
    private DatabaseReference itemRef;
    private DatabaseReference userCartRef;
    private DatabaseReference userRef;
    private FirebaseAuth mainAuth;
    private String userId;

    private boolean isAdmin;
    private EditText couponCodeEdit;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialise Firebase variables
        mainAuth = FirebaseAuth.getInstance();
        userId = mainAuth.getUid();

        userRef = FirebaseDatabase.getInstance().getReference("User");
        userCartRef = FirebaseDatabase.getInstance().getReference("User").child(userId).child("user-shopCart");
        itemRef = FirebaseDatabase.getInstance().getReference("Item");

        formatter = new NumberFormatter();
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

        applyCouponBtn = view.findViewById(R.id.apply_coupon_btn);
        confirmAndPayBtn = view.findViewById(R.id.confirm_and_pay_btn);


        applyCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCouponDialog();
            }
        });

        confirmAndPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmAndPay();
            }
        });
    }

    public void getUser() {

        Log.d(TAG, "getUser: " + userRef.child(userId).child("adminDetails").child("employeeID").toString());


        userRef.child(userId).child("adminDetails").child("employeeID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue() != null){
                    Log.d(TAG, "onDataChange: great success " + snapshot.getValue() );
                    isAdmin = true;
                }
                else{
                    Log.d(TAG, "onDataChange: User is an admin" + snapshot.getValue());
                    isAdmin = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e(TAG, "onCancelled: error in database: " + error);
            }
        });

    }
    private void openCouponDialog() {

        getUser();


        userCoupon = new UserCoupon();

        //TODO: add coupon codes and decorate coupon based on code, standard 10% off for admins, 0% for users, add code to

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()));
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_coupon_code,null);
        builder.setView(dialogView);
        builder.setTitle("Coupon");
        builder.setPositiveButton("Apply", (dialog, which) -> {

            couponCodeEdit = dialogView.findViewById(R.id.dialog_admin_discount_text);
            String couponCode = couponCodeEdit.getText().toString();


            switch(couponCode){

                case("USER10"):
                    userCoupon = new TenPercent(userCoupon);
                    Toast.makeText(getActivity(),"Coupon: " + userCoupon.getName() + " " + userCoupon.discount() * 100 + "% off!" , Toast.LENGTH_SHORT).show();

                    //TODO: get snackbars working here
                    //Snackbar.make(getView(), "Coupon: " + userCoupon.getName() + " " + userCoupon.discount() * 100 + "% off!", Snackbar.LENGTH_LONG)
                            //.setBackgroundTint(getResources().getColor(R.color.appLightBlue));
                    break;

                case("USER20"):
                    userCoupon = new TwentyPercent(userCoupon);
                    Toast.makeText(getActivity(),"Coupon: " + userCoupon.getName() + " " + userCoupon.discount() * 100 + "% off!" , Toast.LENGTH_SHORT).show();

                    break;


                case("USER50"):
                    userCoupon = new HalfPrice(userCoupon);
                    Toast.makeText(getActivity(),"Coupon: " + userCoupon.getName() + " " + userCoupon.discount() * 100 + "% off!" , Toast.LENGTH_SHORT).show();

                    break;

                default:
                    Toast.makeText(getActivity(), "Coupon code not recognised", Toast.LENGTH_SHORT).show();

                    break;

            }

            subtotalCents = subtotalCents - (int) (subtotalCents * (userCoupon.discount()));
            Log.d(TAG, "openCouponDialog: subtotalCents value: " + subtotalCents);

        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        couponCodeEdit = alertDialog.findViewById(R.id.dialog_admin_discount_text);
        adminDiscountBtn = alertDialog.findViewById(R.id.dialog_admin_discount_btn);


       // if(!isAdmin) couponCodeEdit.setVisibility(View.INVISIBLE);


        adminDiscountBtn.setOnClickListener(v -> {

            if(!isAdmin){

                //Create AdminCoupon object
                //apply discount to textview
                Coupon coupon = new AdminCoupon();

                Snackbar.make(getView(), "Admin coupon applied: " + coupon.discount() * 100 + "% off", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.appBlue)).show();

            }
            else{
                Snackbar.make(getView(), "You are not an admin", Snackbar.LENGTH_LONG).show();
            }

        });

    }

    //TODO: set all this up, apply discounts in dialog, visa ending in 0919...
    // 'purchase charged to employee account' for admins
    // clear shopping list in firebase, potential 'Purchased items rcv' like categories in child rcv shopping list categories?

    public void openConfirmPayDialog(){

    }

    public void getSubtotalAndNum(List<Item> items){

        int numItems = 0;



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
                    itemRef.child(allItem.getId()).child("stockLevel").setValue(allItem.getStockLevel() - cartItem.getCustQuant());
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