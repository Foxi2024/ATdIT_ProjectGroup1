package com.atdit.booking.backend.financialdata.contracts;

/**
 * Represents a contract entity in the financial data system.
 * This class manages contract-related information including payment methods and contract details.
 */
public class Contract {

    /**
     * The payment method used for this contract.
     */
    private String paymentMethod;

    /**
     * The total amount of the contract and our journey, set as a constant value.
     * This value is fixed at 1,000,000 and cannot be modified.
     */
    public final double TOTAL_AMOUNT = 1_000_000;

    /**
     * Sets the payment method for this contract.
     *
     * @param paymentMethod the payment method to be set (e.g., "Credit Card", "Bank Transfer")
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Retrieves the current payment method of the contract.
     *
     * @return the payment method as a String
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Retrieves the contract text content.
     * This base implementation returns an empty string.
     * Subclasses should override this method to provide specific contract text.
     *
     * @return an empty String in this base implementation
     */
    public String getContractText(){
        // This method is intended to be overridden by subclasses to provide actual contract text.
        return "";

    }
}