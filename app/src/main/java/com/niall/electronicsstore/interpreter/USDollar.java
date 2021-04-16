package com.niall.electronicsstore.interpreter;

public class USDollar implements Expression{


    @Override
    public String euros(double price) {
        return Double.toString(price / 1.19);
    }

    @Override
    public String pounds(double price) {
        return Double.toString(price * .73);
    }

    @Override
    public String dollarsUS(double price) {
        return Double.toString(price);
    }

    @Override
    public String dollarsCAD(double price) {
        return Double.toString(price / .79);
    }


}
