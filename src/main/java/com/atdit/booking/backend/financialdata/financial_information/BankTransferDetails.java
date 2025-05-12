package com.atdit.booking.backend.financialdata.financial_information;

public class BankTransferDetails {

    private String iban;
    private String accountHolder;
    private String bankName;
    private String bicSwift;




    public void setBicSwift(String bicSwift) {
        this.bicSwift = bicSwift.toUpperCase();
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public void setIban(String iban) {
        this.iban = iban.toUpperCase();
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
