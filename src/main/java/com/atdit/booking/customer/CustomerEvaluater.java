package com.atdit.booking.customer;

import com.atdit.booking.Main;
import com.atdit.booking.financialdata.FinancialInformation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CustomerEvaluater {

    public final FinancialInformation financialInformation;
    public final Customer customer;


    public CustomerEvaluater(Customer customer) {
        this.customer = customer;
        this.financialInformation = customer.getFinancialInformation();
    }

    public boolean hasOptimalSchufaScore(){
        return this.financialInformation.getSchufa().getScore() > 0.975;
    }

    public boolean evalCustomer1(int journeyPrice){
        return this.financialInformation.getLiquidAssets() > journeyPrice * 0.2 &&
                financialInformation.getMonthlyAvailableMoney() > Main.MIN_MONTHLY_MONEY;
    }


    public void evaluateCustomerInfo() {
        String errorMessage = "Please fill out the following fields correctly:\n";
        boolean hasError = false;

        try {
            if (customer.getTitle() == null || customer.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            }
        } catch (IllegalArgumentException ex) {
            errorMessage += ex.getMessage() + "\n";
            hasError = true;
        }

        try {
            if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()) {
                throw new IllegalArgumentException("First name cannot be empty");
            }
        } catch (IllegalArgumentException ex) {
            errorMessage += ex.getMessage() + "\n";
            hasError = true;
        }

        try {
            if (customer.getName() == null || customer.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Last name cannot be empty");
            }
        } catch (IllegalArgumentException ex) {
            errorMessage += ex.getMessage() + "\n";
            hasError = true;
        }

        try {
            if (customer.getBirthdate() == null || customer.getBirthdate().trim().isEmpty()) {
                throw new IllegalArgumentException("Birthdate cannot be empty");
            }
        } catch (IllegalArgumentException ex) {
            errorMessage += ex.getMessage() + "\n";
            hasError = true;
        }

        try {
            if (customer.getCountry() == null || customer.getCountry().trim().isEmpty()) {
                throw new IllegalArgumentException("Country cannot be empty");
            }
        } catch (IllegalArgumentException ex) {
            errorMessage += ex.getMessage() + "\n";
            hasError = true;
        }

        try {
            if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
                throw new IllegalArgumentException("Address cannot be empty");
            }
        } catch (IllegalArgumentException ex) {
            errorMessage += ex.getMessage() + "\n";
            hasError = true;
        }

        try {
            if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
        } catch (IllegalArgumentException ex) {
            errorMessage += ex.getMessage() + "\n";
            hasError = true;
        }

        if (hasError) {
            throw new IllegalArgumentException(errorMessage);
        }
    }


    public void checkFirstName(String firstName) throws IllegalArgumentException {
        String nameRegex = "^[A-Za-z\\s-]+$";
        if (firstName == null || !firstName.matches(nameRegex)) {
            throw new IllegalArgumentException("Invalid first name format");
        }
    }

    public void checkName(String name) throws IllegalArgumentException {
        String nameRegex = "^[A-Za-z\\s-]+$";
        if (name == null || !name.matches(nameRegex)) {
            throw new IllegalArgumentException("Invalid last name format");
        }
    }



    public void checkBirthdate(String birthdate) throws IllegalArgumentException {

        String dateRegex = "\\d{4}-\\d{2}-\\d{2}";

        if (birthdate == null || !birthdate.matches(dateRegex)) {
            throw new IllegalArgumentException("Invalid birthdate format (use YYYY-MM-DD)");
        }

        LocalDate date = LocalDate.parse(birthdate);
        LocalDate now = LocalDate.now();

        if (date.isAfter(now)) {
            throw new IllegalArgumentException("Birthdate cannot be in the future");
        }
        if (ChronoUnit.YEARS.between(date, now) < 18) {
            throw new IllegalArgumentException("Customer must be at least 18 years old");
        }


    }

    public void checkCountry(String country) throws IllegalArgumentException {
        String countryRegex = "^[A-Za-z\\s-]+$";
        if (country == null || !country.matches(countryRegex)) {
            throw new IllegalArgumentException("Invalid birthdate format (use YYYY-MM-DD)");
        }
    }

    public void checkAddress(String address) {

        String addressRegex = "^[A-Za-z0-9\\s,.-]+$";
        if (address == null || !address.matches(addressRegex)) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
    }

    public void checkEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

}
