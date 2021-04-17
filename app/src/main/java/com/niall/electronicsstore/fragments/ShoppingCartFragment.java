package com.niall.electronicsstore.fragments;

import android.content.Intent;
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
import com.niall.electronicsstore.activities.UserPurchaseHistoryActivity;
import com.niall.electronicsstore.adapters.ShopCartItemAdapter;
import com.niall.electronicsstore.decorator.AdminCoupon;
import com.niall.electronicsstore.decorator.Coupon;
import com.niall.electronicsstore.decorator.HalfPrice;
import com.niall.electronicsstore.decorator.TenPercent;
import com.niall.electronicsstore.decorator.TwentyPercent;
import com.niall.electronicsstore.decorator.UserCoupon;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.entities.PurchaseHistory;
import com.niall.electronicsstore.util.NumberFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    //Firebase variables
    private DatabaseReference itemRef;
    private DatabaseReference userCartRef;
    private DatabaseReference userAdminCheckRef;
    private DatabaseReference userCardRef;
    private FirebaseAuth mainAuth;
    private String userId;
    private String userCardNo;
    private DatabaseReference userPurchasedItemsRef;


    //confirmPaymentDialog variables
    private TextView dialogTotalItemsText;
    private TextView dialogSubtotalText;
    private TextView dialogCouponDiscountPercentText;
    private TextView dialogTotalCostText;


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

        userAdminCheckRef = FirebaseDatabase.getInstance().getReference("User");
        userCardRef = FirebaseDatabase.getInstance().getReference("User");
        userCartRef = FirebaseDatabase.getInstance().getReference("User").child(userId).child("userShopCart");
        itemRef = FirebaseDatabase.getInstance().getReference("Item");
        userPurchasedItemsRef = FirebaseDatabase.getInstance().getReference("User").child(userId).child("userPurchasedItems");

        formatter = new NumberFormatter();

        getUser();
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

        Log.d(TAG, "getUser: " + userAdminCheckRef.child(userId).child("adminDetails").child("employeeID").toString());

        userAdminCheckRef.child(userId).child("adminDetails").child("employeeID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.getValue() != null) {
                    Log.d(TAG, "onDataChange: great success " + snapshot.getValue());
                    isAdmin = true;
                } else {
                    Log.d(TAG, "onDataChange: User is an admin" + snapshot.getValue());
                    isAdmin = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e(TAG, "onCancelled: error in database: " + error);
            }
        });


        userCardRef.child(userId).child("paymentMethod").child("cardNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.getValue() != null) {
                    Log.d(TAG, "onDataChange: great success, user card " + snapshot.getValue());
                    userCardNo = snapshot.getValue(String.class);
                } else {
                    Log.d(TAG, "onDataChange: User is an admin, no card in database: " + snapshot.getValue());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public String getCardDigits(String cardNo) {

        //Called if user is not admin
        return cardNo.substring(cardNo.length() - 4);
    }

    private void openCouponDialog() {


        if (cartItems.size() < 1) {

            Log.d(TAG, "openCouponDialog: Cart items < 1: " + cartItems.toString());
            Snackbar.make(Objects.requireNonNull(getView()), "You have no items in your cart, browse the catalogue to add items!", Snackbar.LENGTH_LONG).show();
        } else {
            userCoupon = new UserCoupon();

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()));
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_coupon_code, null);
            builder.setView(dialogView);
            builder.setTitle("Coupon");
            builder.setPositiveButton("Apply", (dialog, which) -> {

                couponCodeEdit = dialogView.findViewById(R.id.dialog_admin_discount_text);
                String couponCode = couponCodeEdit.getText().toString();


                switch (couponCode) {

                    case ("USER10"):
                        userCoupon = new TenPercent(userCoupon);
                        Toast.makeText(getActivity(), "Coupon: " + userCoupon.code() + " " + userCoupon.discount() * 100 + "% off!", Toast.LENGTH_SHORT).show();

                        //TODO: get snackbars working here
                        Snackbar.make(getView(), userCoupon.code() + " " + userCoupon.discount() * 100 + "% off!", Snackbar.LENGTH_LONG)
                                .setBackgroundTint(getResources().getColor(R.color.appLightBlue));
                        break;

                    case ("USER20"):
                        userCoupon = new TwentyPercent(userCoupon);
                        Toast.makeText(getActivity(), userCoupon.code() + " " + userCoupon.discount() * 100 + "% off!", Toast.LENGTH_SHORT).show();

                        break;


                    case ("USER50"):
                        userCoupon = new HalfPrice(userCoupon);
                        Toast.makeText(getActivity(), userCoupon.code() + " " + userCoupon.discount() * 100 + "% off!", Toast.LENGTH_SHORT).show();

                        break;

                    default:
                        Toast.makeText(getActivity(), "Coupon code not recognised", Toast.LENGTH_SHORT).show();

                        break;

                }

                //subtotalCents = subtotalCents - (int) (subtotalCents * (userCoupon.discount()));
                Log.d(TAG, "openCouponDialog: subtotalCents value: " + subtotalCents);

            });

            builder.setNegativeButton("Dismiss", (dialog, which) -> {

                dialog.dismiss();
            });

            AlertDialog alertDialog = builder.create();

            alertDialog.show();

            couponCodeEdit = alertDialog.findViewById(R.id.dialog_admin_discount_text);
            adminDiscountBtn = alertDialog.findViewById(R.id.dialog_admin_discount_btn);


            assert adminDiscountBtn != null;
            adminDiscountBtn.setOnClickListener(v -> {

                if (isAdmin) {

                    //Create AdminCoupon object
                    //apply discount to textview
                    Coupon coupon = new AdminCoupon();

                    Snackbar.make(Objects.requireNonNull(getView()), "Admin coupon applied: " + coupon.discount() * 100 + "% off", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.appBlue)).show();

                } else {
                    Snackbar.make(Objects.requireNonNull(getView()), "You are not an admin", Snackbar.LENGTH_LONG).show();
                }

            });
        }
    }

    public void getSubtotalAndNum(List<Item> items) {

        subtotalText.setText("");

        subtotalCents = 0;
        int numItems = 0;


        for (Item item : items) {
            subtotalCents += item.getPriceCents() * item.getCustQuant();
            numItems += item.getCustQuant();
        }
        subtotalText.setText("Subtotal: " + NumberFormatter.formatPriceEuros(subtotalCents));

        numItemsText.setText("Total items(s): " + numItems);
    }


    private void confirmAndPay() {

        //TODO: set up dialog, based on user isAdmin, rcv for products, total cost, txtView for coupons applied with discount, subtotal;
        // admin cost applied to 'admin account', stock taken away

        if (cartItems.size() < 1) {

            Log.d(TAG, "openCouponDialog: Cart items < 1: " + cartItems.toString());
            Snackbar.make(Objects.requireNonNull(getView()), "You have no items in your cart, browse the catalogue to add items!", Snackbar.LENGTH_LONG).show();
        } else {
            // do everything

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()));
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm_purchase, null);
            builder.setView(dialogView);

            dialogTotalItemsText = dialogView.findViewById(R.id.confirm_pay_dialog_total_items_text);
            dialogSubtotalText = dialogView.findViewById(R.id.confirm_pay_dialog_subtotal_text);
            dialogCouponDiscountPercentText = dialogView.findViewById(R.id.confirm_pay_dialog_coupon_discount_text);
            dialogTotalCostText = dialogView.findViewById(R.id.confirm_pay_dialog_total_cost_text);

            dialogTotalItemsText.setText("No. of item(s): " + cartItems.size());
            dialogSubtotalText.setText("Subtotal: " + NumberFormatter.formatPriceEuros(subtotalCents));

            //TODO: fix this to account for no coupon added and maths with discount
            if (userCoupon != null) {
                dialogCouponDiscountPercentText.setText(String.valueOf(userCoupon.discount()) + "%");
                dialogTotalCostText.setText((int) (((double) subtotalCents) * userCoupon.discount()) + " €");
            } else {
                dialogTotalCostText.setText(NumberFormatter.formatPriceEuros(subtotalCents));
            }


            //TODO: start previous purchased activity

            confirmAndPayBtn = dialogView.findViewById(R.id.confirm_pay_dialog_confirm_pay_btn);

            if (isAdmin) {
                confirmAndPayBtn.setText("Pay as admin");
            } else {
                confirmAndPayBtn.setText(String.format("Pay with card ending %s", getCardDigits(userCardNo)));
            }
            builder.setTitle("Confirm and Pay");
            builder.setNegativeButton("Dismiss", (dialog, which) -> {

                dialog.dismiss();
            });

            AlertDialog alertDialog = builder.create();

            alertDialog.show();

            confirmAndPayBtn.setOnClickListener(v -> {

                for (Item allItem : allItems) {

                    for (Item cartItem : cartItems) {
                        if (allItem.getName().equals(cartItem.getName())) {
                            itemRef.child(allItem.getId()).child("stockLevel").setValue(allItem.getStockLevel() - cartItem.getCustQuant());
                        } else if (allItem.getStockLevel() == 0 || cartItem.getCustQuant() > allItem.getStockLevel()) {
                            Toast.makeText(getActivity(), "Cannot complete payment, not enough items in stock!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                if (isAdmin) {
                    alertDialog.dismiss();
                    Toast.makeText(getActivity(), "Payment complete on work account", Toast.LENGTH_SHORT).show();

                } else {
                    alertDialog.dismiss();
                    Toast.makeText(getActivity(), "Payment completed with Visa ending: " + getCardDigits(userCardNo), Toast.LENGTH_SHORT).show();

                }
                addToPurchasedItems();

            });
        }
    }

    public void addToPurchasedItems() {

        PurchaseHistory purchaseHistory = new PurchaseHistory();
        String datePurchased;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate purchaseDate = LocalDate.now();
        datePurchased = dtf.format(purchaseDate);

        purchaseHistory.setDatePurchased(datePurchased);
        purchaseHistory.setItemsPurchased(cartItems);


        String key = userPurchasedItemsRef.push().getKey();

        userPurchasedItemsRef.child(key).setValue(purchaseHistory).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Item added to purchased, PurchaseHistory successfull: " + purchaseHistory.toString());
                clearShopList();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: PurchaseHistory failed " + e.getLocalizedMessage(), e );

                    }
                });
//        for (Item cartItem : cartItems)
//                String key = userPurchasedItemsRef.push().getKey();
//
//
//                assert key != null;
//
//                userPurchasedItemsRef.child(key).setValue(cartItem).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "onSuccess: Item added to purchased: " + cartItem.getName());
//
//                        clearShopList();
//
//                    }
//                });
//            }
    }


    public void addToAllPurchaseHistory() {

        //TODO: add the details of the order needed for
    }


    public void clearShopList() {
        userCartRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: User cart cuccessfully deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e(TAG, "onFailure: Cart not deleted " + e.getLocalizedMessage(), e);
            }
        });

        //TODO: get user purchased items
        startActivity(new Intent(getContext(), UserPurchaseHistoryActivity.class));
    }

    void retrieveItemsFromFirebase() {

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void retrieveCartFromFirebase() {

        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cartItems.clear();

                for (DataSnapshot keyNode : snapshot.getChildren()) {

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

    public void setUpRCV() {
        recyclerView = getView().findViewById(R.id.shop_cart_rcv);
        adapter = new ShopCartItemAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}