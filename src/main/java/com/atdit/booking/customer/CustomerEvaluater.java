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

    public void evaluateCustomerInfo() throws IllegalArgumentException {
        StringBuilder errorMessage = new StringBuilder("Bitte korrigieren Sie folgende Fehler:\n");
        boolean hasError = false;

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

        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("- Kein Vorname angegeben");
        }
        if(!firstName.matches(nameRegex)){
            throw new IllegalArgumentException("- Vorname enthält ungültige Zeichen");
        }
    }

    public void checkName(String name) throws IllegalArgumentException {
        String nameRegex = "^[A-Za-z\\s-]+$";
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("- Kein Nachname angegeben");
        }
        if(!name.matches(nameRegex)){
            throw new IllegalArgumentException("- Nachname enthält ungültige Zeichen");
        }
    }


    public void checkBirthdate(String birthdate) throws IllegalArgumentException {

        String dateRegex = "\\d{4}-\\d{2}-\\d{2}";

        if (birthdate == null || birthdate.isEmpty()) {
            throw new IllegalArgumentException("- Kein Geburtsdatum angegeben");
        }

        if (!birthdate.matches(dateRegex) ) {
            throw new IllegalArgumentException("- Nicht zulässiges Datumsformat (DD/MM/YYYY erforderlich)");
        }

        LocalDate date = LocalDate.parse(birthdate);
        LocalDate now = LocalDate.now();

        if (date.isAfter(now)) {
            throw new IllegalArgumentException("- Geburtsdatum liegt in der Zukunft");
        }

        if (ChronoUnit.YEARS.between(date, now) < 18) {
            throw new IllegalArgumentException("- Sie müssen mindestens 18 Jahre alt sein");
        }

    }

    public void checkCountry(String country) throws IllegalArgumentException {
        String countryRegex = "^[A-Za-z\\s-]+$";
        if (country == null || country.isEmpty()) {
            throw new IllegalArgumentException("- Kein Land angegeben");
        }
        if (!country.matches(countryRegex)) {
            throw new IllegalArgumentException("- Land enthält ungültige Zeichen");
        }
    }

    public void checkAddress(String address) throws IllegalArgumentException{

        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("- Keine Adresse angegeben");
        }

    }

    public void checkEmail(String email) throws IllegalArgumentException{
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("- Keine E-Mail-Adresse angegeben");
        }
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("- E-Mail-Adresse ist ungültig");
        }
    }

}
