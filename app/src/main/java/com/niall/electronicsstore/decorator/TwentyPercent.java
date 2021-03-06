package com.niall.electronicsstore.decorator;

public class TwentyPercent extends CouponDecorator{

    Coupon coupon;

    public TwentyPercent(Coupon coupon){
        super(coupon);
        this.coupon = coupon;
    }

    @Override
    public String getDescription() {
        return name + " + 20%!";
    }

    @Override
    public String code() {
        return coupon.code() + "20";
    }

    @Override
    public double discount() {
        return coupon.discount() + .20;
    }

}
