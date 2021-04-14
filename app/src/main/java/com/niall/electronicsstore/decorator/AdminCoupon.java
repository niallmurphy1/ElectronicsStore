package com.niall.electronicsstore.decorator;

public class AdminCoupon extends Coupon{

    public AdminCoupon(){

        name = "Admin discount";
    }

    @Override
    public String code() {
        return  "ADMIN";
    }

    @Override
    public double discount() {
        return .10;
    }
}
