package com.niall.electronicsstore.singleton;
import com.niall.electronicsstore.decorator.Coupon;


public class CouponManager {

    private static CouponManager instance;

    private Coupon userCoupon;
    private Coupon adminCoupon;

    public static CouponManager getInstance() {
        if (instance == null) {
            instance = new CouponManager();
        }
        return instance;
    }

    public Coupon getUserCoupon() {
        return userCoupon;
    }
    public void setUserCoupon(Coupon userCoupon) {
        this.userCoupon = userCoupon;
    }
    public Coupon getAdminCoupon() {
        return adminCoupon;
    }
    public void setAdminCoupon(Coupon adminCoupon) {
        this.adminCoupon = adminCoupon;
    }
}