package com.niall.electronicsstore.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.niall.electronicsstore.R;
import com.niall.electronicsstore.activities.AllUserPurchaseHistoryActivity;
import com.niall.electronicsstore.activities.EditStockActivity;
import com.niall.electronicsstore.activities.UserPurchaseHistoryActivity;
import com.niall.electronicsstore.entities.Item;
import com.niall.electronicsstore.entities.PurchaseHistory;
import com.niall.electronicsstore.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

import static android.content.ContentValues.TAG;

public class AdminFragment extends Fragment {


    private DatabaseReference userRef;
    private ArrayList<User> allUsers;

    private Button userPurchasesBtn;
    private Button editStockButton;

    private FirebaseAuth mainAuth;
    private boolean isAdmin;

    private String userId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userRef = FirebaseDatabase.getInstance().getReference("User");

        mainAuth = FirebaseAuth.getInstance();
        userId = mainAuth.getUid();

        allUsers = new ArrayList<>();

        getUser();
        // getAllUsersPurchaseHistory();

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

        userPurchasesBtn = getView().findViewById(R.id.admin_frag_view_user_purchases_btn);
        editStockButton = getView().findViewById(R.id.admin_frag_edit_stock_btn);


        userPurchasesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdmin) {
                    startActivity(new Intent(getContext(), AllUserPurchaseHistoryActivity.class));
                } else {

                    Snackbar.make(getView(), "Access denied, You are not an admin!", Snackbar.LENGTH_SHORT).show();
                }


            }
        });

        editStockButton.setOnClickListener(v -> {
            if (isAdmin) {
                startActivity(new Intent(getContext(), EditStockActivity.class));
            } else {
                Snackbar.make(getView(), "Access denied, You are not an admin!", Snackbar.LENGTH_SHORT).show();

            }
        });
    }


    public void getUser() {

        Log.d(TAG, "getUser: " + userRef.child(userId).child("adminDetails").child("employeeID").toString());

        userRef.child(userId).child("adminDetails").child("employeeID").addValueEventListener(new ValueEventListener() {
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



    }

}