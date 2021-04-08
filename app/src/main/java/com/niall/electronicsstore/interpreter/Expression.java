package com.niall.electronicsstore.interpreter;

public abstract class Expression {

    public abstract String euros(double price);

    public abstract String pounds(double price);

    public abstract String dollarsUS(double price);

    public abstract String dollarsCAD(double price);

    public abstract String japaneseYen(double price);


}
