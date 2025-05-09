package com.atdit.booking;

import javafx.scene.image.WritableImage;
import java.time.LocalDateTime;

public class FinancingContract extends Contract {
    private int id;
    private int customerId;
    private double totalAmount;
    private double monthlyPayment;
    private double interestRate;
    private double amountWithInterest;
    private int months;
    private LocalDateTime signedDate;
    private String contractText;


    public FinancingContract() {

        setTotalAmount(super.TOTAL_AMOUNT);
        this.interestRate = 0;
        this.amountWithInterest = totalAmount + (totalAmount * interestRate / 100);
        this.months = 0;
        this.monthlyPayment = 0;
    }

    public void setMonths(int months) {
        this.months = months;
        this.monthlyPayment = amountWithInterest / months;
    }

    public double getMonthlyPayment() {
        return this.monthlyPayment;
    }

    public double getAmountWithInterest() {
        return this.amountWithInterest;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public int getMonths() {
        return months;
    }

    private void calculateAmountWithInterest() {
        this.amountWithInterest = totalAmount + (totalAmount * interestRate / 100);
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
        calculateAmountWithInterest();
        calculateMonthlyPayment();
    }

    private void calculateMonthlyPayment() {
        if (months == 0) {
            this.monthlyPayment = 0;
            return;
        }
        this.monthlyPayment = amountWithInterest / months;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        calculateAmountWithInterest();
        calculateMonthlyPayment();
    }
}