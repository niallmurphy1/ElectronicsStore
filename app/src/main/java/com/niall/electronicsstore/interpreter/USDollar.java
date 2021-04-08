package com.niall.electronicsstore.interpreter;

public class USDollar extends Expression{


    @Override
    public String euros(double price) {
        return Double.toString(price * .84);
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
        return Double.toString(price * 1.26);
    }

    @Override
    public String japaneseYen(double price) {
        return Double.toString(price * 109.21);
    }
}
