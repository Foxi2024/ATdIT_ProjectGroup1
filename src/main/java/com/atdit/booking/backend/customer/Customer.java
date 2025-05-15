package com.atdit.booking.backend.customer;


import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;


/**
 * The Customer class represents a customer in the booking system.
 * It contains personal information about the customer as well as financial data.
 *
 * <p>This class provides getter and setter methods to manipulate the attributes
 * and a toString method to represent the customer information as a string.</p>
 */
public class Customer {

    /**
     * The title of the customer (e.g., Mr., Ms., Dr.).
     */
    private String title;

    /**
     * The first lastName of the customer.
     */
    private String firstName;

    /**
     * The last lastName of the customer.
     */
    private String lastName;

    /**
     * The country where the customer resides.
     */
    private String country;

    /**
     * The birthdate of the customer in the format "YYYY-MM-DD".
     */
    private String birthdate;

    /**
     * The address of the customer.
     */
    private String address;

    /**
     * The email address of the customer.
     */
    private String email;

    /**
     * Financial information of the customer.
     */
    private FinancialInformation financialInformation;

    /**
     * Default constructor that creates a new Customer object with empty attributes.
     * Also initializes a new FinancialInformation object.
     */
    public Customer(){

        this.financialInformation = new FinancialInformation();

        this.title = "";
        this.firstName = "";
        this.lastName = "";
        this.birthdate = "";
        this.country = "";
        this.address = "";
        this.email = "";
    }


    /**
     * Sets the title of the customer.
     *
     * @param title The title of the customer.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Sets the first lastName of the customer.
     *
     * @param firstName The first lastName of the customer.
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * Sets the last lastName of the customer.
     *
     * @param lastName The last lastName of the customer.
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Sets the country of the customer.
     *
     * @param country The country of the customer.
     */
    public void setCountry(String country) { this.country = country; }

    /**
     * Sets the birthdate of the customer.
     *
     * @param birthdate The birthdate of the customer in the format "YYYY-MM-DD".
     */
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    /**
     * Sets the address of the customer.
     *
     * @param address The address of the customer.
     */
    public void setAddress(String address) { this.address = address; }

    /**
     * Sets the email address of the customer.
     *
     * @param email The email address of the customer.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Sets the financial information of the customer.
     *
     * @param financialInformation A FinancialInformation object containing the customer's financial data.
     */
    public void setFinancialInformation(FinancialInformation financialInformation) { this.financialInformation = financialInformation; }

    /**
     * Returns the title of the customer.
     *
     * @return The title of the customer.
     */
    public String getTitle() { return title; }

    /**
     * Returns the first lastName of the customer.
     *
     * @return The first lastName of the customer.
     */
    public String getFirstName() { return firstName; }

    /**
     * Returns the last lastName of the customer.
     *
     * @return The last lastName of the customer.
     */
    public String getLastName() { return lastName; }

    /**
     * Returns the birthdate of the customer.
     *
     * @return The birthdate of the customer in the format "YYYY-MM-DD".
     */
    public String getBirthdate() { return birthdate; }

    /**
     * Returns the country of the customer.
     *
     * @return The country of the customer.
     */
    public String getCountry() { return country; }

    /**
     * Returns the address of the customer.
     *
     * @return The address of the customer.
     */
    public String getAddress() { return address; }

    /**
     * Returns the email address of the customer.
     *
     * @return The email address of the customer.
     */
    public String getEmail() { return email; }

    /**
     * Returns the financial information of the customer.
     *
     * @return A FinancialInformation object containing the customer's financial data.
     */
    public FinancialInformation getFinancialInformation() { return financialInformation; }

    /**
     * Returns a string representation of the customer.
     *
     * @return A string representation of the customer with all attributes.
     */
    @Override
    public String toString() {
        return "Customer{" +
                "financialInformation=" + financialInformation +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", country='" + country + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}