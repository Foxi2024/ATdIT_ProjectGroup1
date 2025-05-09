package com.atdit.booking;

import java.time.LocalDateTime;

public class OneTimePaymentContract extends Contract{


    private double totalAmount;

    public OneTimePaymentContract() {
        this.totalAmount = super.TOTAL_AMOUNT;
    }


}
