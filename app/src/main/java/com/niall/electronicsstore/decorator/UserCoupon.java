package com.niall.electronicsstore.decorator;

public class UserCoupon implements Coupon{

    String name;

    public UserCoupon(){

        name = "User Coupon";
    }

    @Override
    public String code() {
        return "USER";
    }

    @Override
    public double discount() {
        return 0;
    }


}
