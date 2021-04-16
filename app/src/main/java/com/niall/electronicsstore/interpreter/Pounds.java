package com.niall.electronicsstore.interpreter;

public class Pounds implements Expression{

    @Override
    public String euros(double price) {
        return Double.toString(price / .86);
    }
    @Override
    public String pounds(double price) {
        return Double.toString(price);
    }

    @Override
    public String dollarsUS(double price) {
        return Double.toString(price / .73);
    }

    @Override
    public String dollarsCAD(double price) {
        return Double.toString(price / .58);
    }


}
