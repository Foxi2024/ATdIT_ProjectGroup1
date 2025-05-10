package com.atdit.booking.financialdata;

public class BankTransferDetails {

    private String iban;
    private String accountHolder;
    private String bankName;
    private String bicSwift;

    public void setBicSwift(String bicSwift) {
        this.bicSwift = bicSwift;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getIban() {
        return iban;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBicSwift() {
        return bicSwift;
    }
}
