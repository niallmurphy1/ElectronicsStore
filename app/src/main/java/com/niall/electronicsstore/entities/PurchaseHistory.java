package com.niall.electronicsstore.entities;

import java.util.ArrayList;

public class PurchaseHistory {


    private String userPurchaseName;
    private String datePurchased;
    private ArrayList<Item> itemsPurchased;


    public PurchaseHistory(){

    }

    public PurchaseHistory(String datePurchased, ArrayList<Item> itemsPurchased) {
        this.datePurchased = datePurchased;
        this.itemsPurchased = itemsPurchased;
    }

    public String getUserPurchaseName() {
        return userPurchaseName;
    }

    public void setUserPurchaseName(String userPurchaseName) {
        this.userPurchaseName = userPurchaseName;
    }

    public ArrayList<Item> getItemsPurchased() {
        return itemsPurchased;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }

    public void setItemsPurchased(ArrayList<Item> itemsPurchased) {
        this.itemsPurchased = itemsPurchased;
    }

    @Override
    public String toString() {
        return "PurchaseHistory{" +
                "datePurchased=" + datePurchased +
                ", itemsPurchased=" + itemsPurchased +
                '}';
    }
}
