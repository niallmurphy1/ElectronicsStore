package com.niall.electronicsstore.interpreter;

public class Euro extends Expression{
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

    @Override
    public String japaneseYen(double price) {
        return Double.toString(price * 129.81);
    }
}
