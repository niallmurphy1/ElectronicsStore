package com.niall.electronicsstore.entities;

import java.util.Date;

public class PurchaseHistory {

    private Date datePurchased;
    private Item itemPurchased;


    public PurchaseHistory(){

    }

    private PurchaseHistory(PurchaseHistoryBuilder purchaseHistoryBuilder){
        this.datePurchased = purchaseHistoryBuilder.datePurchased;
        this.itemPurchased = purchaseHistoryBuilder.itemPurchased;

    }


    public static class PurchaseHistoryBuilder{

        private Date datePurchased;
        private Item itemPurchased;

        public PurchaseHistoryBuilder(Date datePurchased, Item itemPurchased){

            this.datePurchased = datePurchased;
            this.itemPurchased = itemPurchased;
        }

        public PurchaseHistory build(){
            return new PurchaseHistory(this);
        }
    }
}
