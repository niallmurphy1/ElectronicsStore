package com.niall.electronicsstore.interpreter;

public class Pounds implements Expression{

    @Override
    public String euros(double price) {
        return Double.toString(price * 1.16);

    }

    @Override
    public String pounds(double price) {
        return Double.toString(price);

    }

    @Override
    public String dollarsUS(double price) {
        return Double.toString(price * 1.38);
    }

    @Override
    public String dollarsCAD(double price) {
        return Double.toString(price * 1.73);
    }

    @Override
    public String japaneseYen(double price) {
        return Double.toString(price * 150.34);
    }
}
