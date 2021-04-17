package com.niall.electronicsstore.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseHistory {


    private String datePurchased;
    private List itemsPurchased;


    public PurchaseHistory(){

    }

    public PurchaseHistory(String datePurchased, List<Item> itemsPurchased) {
        this.datePurchased = datePurchased;
        this.itemsPurchased = itemsPurchased;
    }

    public List getItemsPurchased() {
        return itemsPurchased;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }

    public void setItemsPurchased(List<Item> itemsPurchased) {
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
