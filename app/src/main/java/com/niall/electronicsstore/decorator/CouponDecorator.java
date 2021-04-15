package com.niall.electronicsstore.decorator;

public abstract class CouponDecorator implements Coupon{

    public abstract String getDescription();

    public CouponDecorator(Coupon coupon){

    }
}
