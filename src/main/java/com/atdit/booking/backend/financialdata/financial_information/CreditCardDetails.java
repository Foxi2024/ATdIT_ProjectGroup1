package com.atdit.booking.backend.financialdata.financial_information;

/**
 * Class representing credit card details for payment processing.
 * This class stores sensitive credit card information including card number,
 * expiry date and CVV security code.
 */
public class CreditCardDetails {

    /** The credit card number */
    private String cardNumber;

    /** The expiry date of the credit card */
    private String expiryDate;

    /** The CVV security code of the credit card */
    private String cvv;

    /**
     * Gets the credit card number.
     * @return the credit card number as a String
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Gets the expiry date of the credit card.
     * @return the expiry date as a String
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Gets the CVV security code of the credit card.
     * @return the CVV code as a String
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * Sets the credit card number.
     * @param cardNumber the credit card number to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Sets the expiry date of the credit card.
     * @param expiryDate the expiry date to set
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Sets the CVV security code of the credit card.
     * @param cvv the CVV code to set
     */
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}