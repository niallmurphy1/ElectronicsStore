package com.niall.electronicsstore.interpreter;

public class Euro implements Expression{
    @Override
    public String euros(double price) {
        return Double.toString(price);
    }

    @Override
    public String pounds(double price) {
        return Double.toString(price * .86);
    }

    @Override
    public String dollarsUS(double price) {
        return Double.toString(price * 1.19);

    }

    @Override
    public String dollarsCAD(double price) {
        return Double.toString(price * 1.50);
    }


}
