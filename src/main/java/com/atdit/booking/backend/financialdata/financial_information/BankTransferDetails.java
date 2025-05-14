package com.atdit.booking.backend.financialdata.financial_information;

/**
 * A class representing bank transfer details containing essential banking information.
 * This class stores and manages information needed for bank transfers such as IBAN,
 * account holder name, bank name, and BIC/SWIFT code.
 */
public class BankTransferDetails {

    /**
     * The International Bank Account Number (IBAN) of the bank account.
     * Stored in uppercase format.
     */
    private String iban;

    /**
     * The name of the account holder/owner.
     */
    private String accountHolder;

    /**
     * The name of the bank where the account is held.
     */
    private String bankName;

    /**
     * The Bank Identifier Code (BIC) or SWIFT code of the bank.
     * Stored in uppercase format.
     */
    private String bicSwift;

    /**
     * Sets the BIC/SWIFT code of the bank.
     * The code is automatically converted to uppercase.
     *
     * @param bicSwift the BIC/SWIFT code to set
     */
    public void setBicSwift(String bicSwift) {

        if(bicSwift != null) {
            this.bicSwift = bicSwift.toUpperCase();
        }

    }

    /**
     * Sets the name of the bank.
     *
     * @param bankName the bank name to set
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * Sets the name of the account holder.
     *
     * @param accountHolder the account holder name to set
     */
    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    /**
     * Sets the IBAN of the bank account.
     * Automatically converted to all uppercase.
     *
     * @param iban the IBAN to set
     */
    public void setIban(String iban) {
        if(iban != null){
            this.iban = iban.toUpperCase();
        }

    }

    /**
     * Returns the IBAN of the bank account.
     *
     * @return the IBAN in uppercase format
     */
    public String getIban() {
        return iban;
    }

    /**
     * Returns the name of the account holder.
     *
     * @return the account holder's name
     */
    public String getAccountHolder() {
        return accountHolder;
    }

    /**
     * Returns the name of the bank.
     *
     * @return the bank name
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * Returns the BIC/SWIFT code of the bank.
     *
     * @return the BIC/SWIFT code in uppercase format
     */
    public String getBicSwift() {
        return bicSwift;
    }
}