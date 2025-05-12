package com.atdit.booking.backend.customer;

import com.atdit.booking.backend.database.DatabaseService;
import com.atdit.booking.backend.exceptions.ValidationException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CustomerEvaluator {

    public final Customer customer;


    public CustomerEvaluator(Customer customer) {
        this.customer = customer;
    }


    public void evaluateCustomerInfo() throws ValidationException{
        StringBuilder errorMessage = new StringBuilder("Bitte korrigieren Sie folgende Fehler:\n");

        boolean hasError = false;

        hasError |= appendError(errorMessage, () -> checkFirstName(customer.getFirstName()));
        hasError |= appendError(errorMessage, () -> checkName(customer.getName()));
        hasError |= appendError(errorMessage, () -> checkBirthdate(customer.getBirthdate()));
        hasError |= appendError(errorMessage, () -> checkCountry(customer.getCountry()));
        hasError |= appendError(errorMessage, () -> checkAddress(customer.getAddress()));
        hasError |= appendError(errorMessage, () -> checkEmail(customer.getEmail()));

        if (hasError) {
            throw new ValidationException(errorMessage.toString());
        }
    }

    private boolean appendError(StringBuilder sb, Runnable check) {

        try {
            check.run();
        }
        catch (IllegalArgumentException ex) {
            sb.append(ex.getMessage()).append("\n");
            return true;
        }
        return false;
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

        try {
            DatabaseService db = new DatabaseService();
            db.checkIfCustomerIsInDatabase(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
