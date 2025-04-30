package com.atdit.booking.financialdata;

public class BankAccount {

    private final int balance;
    private final String iban;

    public BankAccount(int balance, String iban) {
        this.balance = balance;
        this.iban = iban;
    }
}
