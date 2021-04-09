package com.niall.electronicsstore.interpreter;

public class JapaneseYen implements Expression{

    @Override
    public String euros(double price) {
        return Double.toString(price * .0077);
    }

    @Override
    public String pounds(double price) {
        return Double.toString(price * .0067);
    }

    @Override
    public String dollarsUS(double price) {
        return Double.toString(price * .0092);
    }

    @Override
    public String dollarsCAD(double price) {
        return Double.toString(price * .012);
    }

    @Override
    public String japaneseYen(double price) {
        return Double.toString(price);
    }
}
