package com.niall.electronicsstore.interpreter;

public class CADDollar implements Expression{

    @Override
    public String euros(double price) {
        return Double.toString(price * .67);
    }

    @Override
    public String pounds(double price) {
        return Double.toString(price * .58);

    }

    @Override
    public String dollarsUS(double price) {
        return Double.toString(price * .79);
    }

    @Override
    public String dollarsCAD(double price) {
        return Double.toString(price);

    }

}
