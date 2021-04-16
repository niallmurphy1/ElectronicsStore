package com.niall.electronicsstore.interpreter;

public interface Expression {

    public abstract String euros(double price);

    public abstract String pounds(double price);

    public abstract String dollarsUS(double price);

    public abstract String dollarsCAD(double price);




}
