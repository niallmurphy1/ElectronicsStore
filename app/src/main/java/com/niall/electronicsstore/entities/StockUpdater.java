package com.niall.electronicsstore.entities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class StockUpdater {

    private FirebaseAuth userAuth;
    private FirebaseDatabase dataRef;
    private int quantPurchased;


    public StockUpdater(FirebaseAuth userAuth, FirebaseDatabase dataRef, int quantPurchased) {
        this.userAuth = userAuth;
        this.dataRef = dataRef;
        this.quantPurchased = quantPurchased;
    }

    public void updateStock(){


    }

}
