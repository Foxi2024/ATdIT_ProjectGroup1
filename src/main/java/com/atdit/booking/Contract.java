package com.atdit.booking;

public class Contract {

    private String paymentMethod;
    public final double TOTAL_AMOUNT = 1_000_000;

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