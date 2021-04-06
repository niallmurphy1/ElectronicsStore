package com.niall.electronicsstore.entities;

public class PaymentMethod {

    private String nameOnCard;
    private String cardNumber;
    private String securityCode;
    private int expMonth;
    private int expYear;

    private PaymentMethod(PaymentMethodBuilder paymentMethodBuilder){
        this.nameOnCard = paymentMethodBuilder.nameOnCard;
        this.cardNumber = paymentMethodBuilder.cardNumber;
        this.securityCode = paymentMethodBuilder.securityCode;
        this.expMonth = paymentMethodBuilder.expMonth;
        this.expYear = paymentMethodBuilder.expYear;
    }


    public static class PaymentMethodBuilder{

        private String nameOnCard;
        private String cardNumber;
        private String securityCode;
        private int expMonth;
        private int expYear;


        public PaymentMethodBuilder(String nameOnCard, String cardNumber, String securityCode, int expMonth, int expYear) {
            this.nameOnCard = nameOnCard;
            this.cardNumber = cardNumber;
            this.securityCode = securityCode;
            this.expMonth = expMonth;
            this.expYear = expYear;
        }

        public PaymentMethod build(){
            return new PaymentMethod(this);
        }
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public int getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(int expMonth) {
        this.expMonth = expMonth;
    }

    public int getExpYear() {
        return expYear;
    }

    public void setExpYear(int expYear) {
        this.expYear = expYear;
    }



    @Override
    public String toString() {
        return "PaymentMethod{" +
                "nameOnCard='" + nameOnCard + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", securityCode='" + securityCode + '\'' +
                ", expMonth=" + expMonth +
                ", expYear=" + expYear +
                '}';
    }
}
