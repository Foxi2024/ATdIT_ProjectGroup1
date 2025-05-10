package com.atdit.booking;

import javafx.scene.image.WritableImage;
import java.time.LocalDateTime;

public class FinancingContract extends Contract {

    private final double INTEREST_RATE = 5.0;
    private final double DOWN_PAYMENT_PERCENTAGE = 0.2;

    private int id;
    private int customerId;
    private double financedAmount;
    private double monthlyPayment;
    private double interestRate;
    private double amountWithInterest;
    private int months;
    private double downPayment;
    private LocalDateTime signedDate;
    private String contractText;


    public FinancingContract() {

        this.downPayment = super.TOTAL_AMOUNT * DOWN_PAYMENT_PERCENTAGE;
        this.financedAmount = super.TOTAL_AMOUNT - this.downPayment;
        this.months = 12;

        calculateInterestRate();
        calculateAmountWithInterest();
        calculateMonthlyPayment();
    }

    public void setMonths(int months) {
        this.months = months;

        calculateInterestRate();
        calculateAmountWithInterest();
        calculateMonthlyPayment();
    }

    public double getMonthlyPayment() {
        return this.monthlyPayment;
    }

    public double getAmountWithInterest() {
        return this.amountWithInterest;
    }


    public int getMonths() {
        return months;
    }

    public void calculateInterestRate() {
        this.interestRate = INTEREST_RATE + ((this.months / 12) - 1) * 0.5;
    }

    private void calculateAmountWithInterest() {
        this.amountWithInterest = (this.financedAmount) + (this.financedAmount * interestRate / 100);
    }


    private void calculateMonthlyPayment() {
        if (months == 0) {
            this.monthlyPayment = 0;
            return;
        }
        this.monthlyPayment = amountWithInterest / months;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getFinancedAmount() {
        return financedAmount;
    }

    public double getDownPayment() {
        return downPayment;
    }
}