package com.atdit.booking;

public class Contract {

    private String paymentMethod;
    public final int TOTAL_AMOUNT = 5000;

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getContractText(){
        return "";

    }
}