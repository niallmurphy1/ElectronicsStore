package com.niall.electronicsstore.entities;

public class Address {


    private String addressLine1;
    private String addressLine2;
    private String zipCode;
    private String city;
    private String country;

    public Address(){

    }

    private Address(AddressBuilder addressBuilder){
        this.addressLine1 = addressBuilder.addressLine1;
        this.addressLine2 = addressBuilder.addressLine2;
        this.zipCode = addressBuilder.zipCode;
        this.city = addressBuilder.city;
        this.country = addressBuilder.country;
    }

    public static class AddressBuilder{

        private String addressLine1;
        private String addressLine2;
        private String zipCode;
        private String city;
        private String country;

        public AddressBuilder(String addressLine1, String addressLine2, String zipCode, String city, String country) {
            this.addressLine1 = addressLine1;
            this.addressLine2 = addressLine2;
            this.zipCode = zipCode;
            this.city = city;
            this.country = country;
        }

        private boolean isValidData(){

            return true;
        }

        public Address build(){
            isValidData();
            return new Address(this);
        }

    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
