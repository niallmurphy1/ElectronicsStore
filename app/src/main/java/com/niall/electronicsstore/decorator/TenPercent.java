package com.niall.electronicsstore.decorator;

public class TenPercent extends CouponDecorator{

    Coupon coupon;

    public TenPercent(Coupon coupon){
        this.coupon = coupon;
    }

    @Override
    public String getDescription() {
        return coupon.getName() + " + 10%!";
    }

    @Override
    public String code() {
        return coupon.code() + "10";
    }

    @Override
    public double discount() {
        return coupon.discount() + .10;
    }
}
