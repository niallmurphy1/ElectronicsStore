package com.niall.electronicsstore.entities;


public class User {

    private String email;
    private PaymentMethod paymentMethod;
    private Name name;
    private Address address;
    private AdminDetails adminDetails;
    private PurchaseHistory purchaseHistory;

    public User(){

    }

    private User(UserBuilder userBuilder) {
        this.email = userBuilder.email;
        this.paymentMethod = userBuilder.paymentMethod;
        this.name = userBuilder.name;
        this.address = userBuilder.address;
        this.adminDetails = userBuilder.adminDetails;
        this.purchaseHistory = userBuilder.purchaseHistory;
    }

    public static class UserBuilder {

        private String email;
        private PaymentMethod paymentMethod;
        private Name name;
        private Address address;
        private AdminDetails adminDetails;
        private PurchaseHistory purchaseHistory;

        public UserBuilder(String email, Name name){
            this.email = email;
            this.name = name;
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

        public UserBuilder purchaseHistory(final PurchaseHistory purchaseHistory){
            this.purchaseHistory = purchaseHistory;
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

    public PurchaseHistory getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(PurchaseHistory purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", name=" + name +
                ", address=" + address +
                ", adminDetails=" + adminDetails +
                ", purchaseHistory=" + purchaseHistory +
                '}';
    }
}
