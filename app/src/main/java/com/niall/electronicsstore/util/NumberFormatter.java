package com.niall.electronicsstore.util;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {


    public NumberFormatter(){

    }
    public static String formatPriceEuros(int priceCents){

        double price = priceCents / 100.00;

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE);

        return numberFormat.format(price);
    }
}
