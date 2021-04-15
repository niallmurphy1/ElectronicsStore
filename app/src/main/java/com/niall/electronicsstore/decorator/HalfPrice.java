package com.niall.electronicsstore.decorator;

public class HalfPrice extends CouponDecorator{

    Coupon coupon;

    public HalfPrice(Coupon coupon){
        super(coupon);
        this.coupon = coupon;
    }

    @Override
    public String getDescription() {
        return name + " + 50%!";
    }

    @Override
    public String code() {
        return coupon.code() + "50";
    }

    @Override
    public double discount() {
        return coupon.discount() + .50;
    }


}
