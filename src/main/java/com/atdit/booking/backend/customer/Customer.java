package com.atdit.booking.backend.customer;


import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;


public class Customer {

    private String title;
    private String firstName;
    private String name;
    private String country;
    private String birthdate;
    private String address;
    private String email;

    private FinancialInformation financialInformation;

    public Customer(){

        this.financialInformation = new FinancialInformation();

        this.title = "";
        this.firstName = "";
        this.name = "";
        this.birthdate = "";
        this.country = "";
        this.address = "";
        this.email = "";
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFinancialInformation(FinancialInformation financialInformation) {
        this.financialInformation = financialInformation;
    }

    public String getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getName() {
        return name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public FinancialInformation getFinancialInformation() {
        return financialInformation;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "financialInformation=" + financialInformation +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", country='" + country + '\'' +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
