package com.niall.electronicsstore.decorator;

public abstract class Coupon {

    String name = "You have applied a coupon: ";

    public String getName() {
        return name;
    }

    public abstract String code();

    public abstract double discount();

}
