package com.niall.electronicsstore.decorator;

public interface Coupon {

    String name = "You have applied a coupon: ";


    public abstract String code();

    public abstract double discount();



}
