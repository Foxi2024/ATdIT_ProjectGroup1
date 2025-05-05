package com.atdit.booking.customer;

import com.atdit.booking.Main;
import com.atdit.booking.financialdata.FinancialInformation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CustomerEvaluater {

    public final Customer customer;


    public CustomerEvaluater(Customer customer) {
        this.customer = customer;
    }

    public void evaluateCustomerInfo() {
        StringBuilder errorMessage = new StringBuilder("Please fill out the following fields correctly:\n");
        boolean hasError = false;

        // Title validation
        if (customer.getTitle() == null || (!customer.getTitle().equals("Mr") && !customer.getTitle().equals("Ms"))) {
            errorMessage.append("Please select a valid title (Mr/Ms)\n");
            hasError = true;
        }

        try {
            checkFirstName(customer.getFirstName());
        } catch (IllegalArgumentException e) {
            errorMessage.append(e.getMessage()).append("\n");
            hasError = true;
        }

        try {
            checkName(customer.getName());
        } catch (IllegalArgumentException e) {
            errorMessage.append(e.getMessage()).append("\n");
            hasError = true;
        }

        try {
            checkBirthdate(customer.getBirthdate());
        } catch (IllegalArgumentException e) {
            errorMessage.append(e.getMessage()).append("\n");
            hasError = true;
        }

        try {
            checkCountry(customer.getCountry());
        } catch (IllegalArgumentException e) {
            errorMessage.append(e.getMessage()).append("\n");
            hasError = true;
        }

        try {
            checkAddress(customer.getAddress());
        } catch (IllegalArgumentException e) {
            errorMessage.append(e.getMessage()).append("\n");
            hasError = true;
        }

        try {
            checkEmail(customer.getEmail());
        } catch (IllegalArgumentException e) {
            errorMessage.append(e.getMessage()).append("\n");
            hasError = true;
        }

        if (hasError) {
            throw new IllegalArgumentException(errorMessage.toString());
        }
    }



    public void checkFirstName(String firstName) throws IllegalArgumentException {
        String nameRegex = "^[A-Za-z\\s-]+$";
        if (firstName == null || !firstName.matches(nameRegex)) {
            throw new IllegalArgumentException("- Invalid format for first name");
        }
    }

    public void checkName(String name) throws IllegalArgumentException {
        String nameRegex = "^[A-Za-z\\s-]+$";
        if (name == null || !name.matches(nameRegex)) {
            throw new IllegalArgumentException("- Invalid format for last name");
        }
    }


    public void checkBirthdate(String birthdate) throws IllegalArgumentException {

        String dateRegex = "\\d{4}-\\d{2}-\\d{2}";

        if (birthdate == null || !birthdate.matches(dateRegex)) {
            throw new IllegalArgumentException("- Invalid birthdate format (use YYYY-MM-DD)");
        }

        LocalDate date = LocalDate.parse(birthdate);
        LocalDate now = LocalDate.now();

        if (date.isAfter(now)) {
            throw new IllegalArgumentException("- Birthdate cannot be in the future");
        }
        if (ChronoUnit.YEARS.between(date, now) < 18) {
            throw new IllegalArgumentException("- Customer must be at least 18 years old");
        }

    }

    public void checkCountry(String country) throws IllegalArgumentException {
        String countryRegex = "^[A-Za-z\\s-]+$";
        if (country == null || !country.matches(countryRegex)) {
            throw new IllegalArgumentException("- Invalid format for country");
        }
    }

    public void checkAddress(String address) {

        String addressRegex = "^[A-Za-z0-9\\s,.-]+$";
        if (address == null || !address.matches(addressRegex)) {
            throw new IllegalArgumentException("- Address cannot be empty");
        }
    }

    public void checkEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || !email.matches(emailRegex)) {
            throw new IllegalArgumentException("- Invalid email format");
        }
    }

}
