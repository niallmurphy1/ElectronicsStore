package com.niall.electronicsstore.decorator;

public class AdminCoupon implements Coupon{

    String name;

    public AdminCoupon(){

        name = "Admin discount";
    }

    @Override
    public String code() {
        return  "ADMIN";
    }

    @Override
    public double discount() {
        return .30;
    }


}
