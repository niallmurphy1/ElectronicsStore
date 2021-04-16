package com.niall.electronicsstore.util;

import com.niall.electronicsstore.interpreter.CADDollar;
import com.niall.electronicsstore.interpreter.Euro;
import com.niall.electronicsstore.interpreter.Expression;
import com.niall.electronicsstore.interpreter.Pounds;
import com.niall.electronicsstore.interpreter.USDollar;

public class ExpressionUtil {

    public static Expression forCode(String code) {
        Expression expression = null;
        switch (code) {
            case "Euro":
                expression = new Euro();
                break;
            case "Pound": {
                expression = new Pounds();
                break;
            }
            case "CAD Dollar":
                expression = new CADDollar();
                break;

            case "US Dollar":
                expression = new USDollar();
                break;
        }
        return expression;
    }
}
