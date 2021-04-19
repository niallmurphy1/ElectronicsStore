package com.niall.electronicsstore.entities;


import java.util.ArrayList;

public class User {

    private String userId;
    private String email;
    private PaymentMethod paymentMethod;
    private Name name;
    private Address address;
    private AdminDetails adminDetails;
    private boolean isAdmin;
    private ArrayList<Item> userShopCart;
    private ArrayList<Item> userPurchasedItems;
    private ArrayList<PurchaseHistory> userPurchaseHistory;

    public User(){

    }



    private User(UserBuilder userBuilder) {
        this.email = userBuilder.email;
        this.paymentMethod = userBuilder.paymentMethod;
        this.name = userBuilder.name;
        this.address = userBuilder.address;
        this.adminDetails = userBuilder.adminDetails;
        this.isAdmin = userBuilder.isAdmin;
        this.userShopCart = userBuilder.userShopCart;
    }

    public static class UserBuilder {

        private String email;
        private PaymentMethod paymentMethod;
        private Name name;
        private Address address;
        private AdminDetails adminDetails;
        private boolean isAdmin;
        private ArrayList<Item> userShopCart;
        private ArrayList<Item> userPurchasedItems;

        public UserBuilder(String email, Name name, boolean isAdmin){
            this.email = email;
            this.name = name;
            this.isAdmin = isAdmin;
        }

        public UserBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public UserBuilder name(final Name name){
            this.name = name;
            return this;
        }

        public UserBuilder paymentMethod(final PaymentMethod paymentMethod){
            this.paymentMethod = paymentMethod;
            return this;
        }

        public UserBuilder address(final Address address){
            this.address = address;
            return this;
        }

        public UserBuilder adminDetails(final AdminDetails adminDetails){
            this.adminDetails = adminDetails;
            return this;
        }



        public UserBuilder isAdmin(final boolean isAdmin){
            this.isAdmin = isAdmin;
            return this;
        }

        public UserBuilder userShopCart(final ArrayList<Item> userShopCart){
            this.userShopCart = userShopCart;
            return this;
        }

        public UserBuilder userPurchasedItems(final ArrayList<Item> userPurchasedItems){
            this.userPurchasedItems = userPurchasedItems;
            return this;
        }
        private boolean isValidData(){

            return true;
        }

        public User build(){
            isValidData();
            return new User(this);
        }

    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Item> getUserPurchasedItems() {
        return userPurchasedItems;
    }

    public void setUserPurchasedItems(ArrayList<Item> userPurchasedItems) {
        this.userPurchasedItems = userPurchasedItems;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public AdminDetails getAdminDetails() {
        return adminDetails;
    }

    public void setAdminDetails(AdminDetails adminDetails) {
        this.adminDetails = adminDetails;
    }


    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public ArrayList<Item> getUserShopCart() {
        return userShopCart;
    }

    public void setUserShopCart(ArrayList<Item> userShopCart) {
        this.userShopCart = userShopCart;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", name=" + name +
                ", address=" + address +
                ", adminDetails=" + adminDetails +
                ", isAdmin=" + isAdmin +
                ", userShopCart=" + userShopCart +
                ", userPurchasedItems=" + userPurchasedItems +
                '}';
    }
}
