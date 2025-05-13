package com.atdit.booking.backend.customer;

import com.atdit.booking.backend.database.DatabaseService;
import com.atdit.booking.backend.exceptions.ValidationException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * This class is responsible for validating customer information.
 * It performs various checks on customer data to ensure its validity.
 */
public class CustomerEvaluator {

    /** The customer instance to be evaluated */
    public final Customer customer;

    /**
     * Constructor for CustomerEvaluator.
     *
     * @param customer The customer object to be evaluated
     */
    public CustomerEvaluator(Customer customer) {
        this.customer = customer;
    }

    /**
     * Evaluates all customer information by running various validation checks.
     * If any validation fails, it collects all error messages and throws a ValidationException.
     *
     * @throws ValidationException If any validation check fails
     */
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

    /**
     * Helper method to append validation errors to the error message.
     *
     * @param sb The StringBuilder containing the error messages
     * @param check The validation check to be performed
     * @return true if an error occurred, false otherwise
     */
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

    /**
     * Validates the customer's first name.
     * The first name must not be empty and must only contain letters, spaces, and hyphens.
     *
     * Regex pattern used:
     * - "^[A-Za-z\\s-]+$" - Matches letters (A-Z, a-z), spaces, and hyphens
     *
     * @param firstName The first name to validate
     * @throws IllegalArgumentException If the first name is invalid
     */
    public void checkFirstName(String firstName) throws IllegalArgumentException {
        String nameRegex = "^[A-Za-z\\s-]+$";

        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("- Kein Vorname angegeben");
        }
        if(!firstName.matches(nameRegex)){
            throw new IllegalArgumentException("- Vorname enthält ungültige Zeichen");
        }
    }

    /**
     * Validates the customer's last name.
     * The last name must not be empty and must only contain letters, spaces, and hyphens.
     *
     * Regex pattern used:
     * - "^[A-Za-z\\s-]+$" - Matches letters (A-Z, a-z), spaces, and hyphens
     *
     * @param name The last name to validate
     * @throws IllegalArgumentException If the last name is invalid
     */
    public void checkName(String name) throws IllegalArgumentException {
        String nameRegex = "^[A-Za-z\\s-]+$";
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("- Kein Nachname angegeben");
        }
        if(!name.matches(nameRegex)){
            throw new IllegalArgumentException("- Nachname enthält ungültige Zeichen");
        }
    }

    /**
     * Validates the customer's birthdate.
     * The date must be in YYYY-MM-DD format, must not be in the future,
     * and the customer must be at least 18 years old.
     *
     * Regex pattern used:
     * - "\\d{4}-\\d{2}-\\d{2}" - Matches dates in YYYY-MM-DD format
     *
     * @param birthdate The birthdate to validate
     * @throws IllegalArgumentException If the birthdate is invalid
     */
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

    /**
     * Validates the customer's country.
     * The country must not be empty and must only contain letters, spaces, and hyphens.
     *
     * Regex pattern used:
     * - "^[A-Za-z\\s-]+$" - Matches letters (A-Z, a-z), spaces, and hyphens
     *
     * @param country The country to validate
     * @throws IllegalArgumentException If the country is invalid
     */
    public void checkCountry(String country) throws IllegalArgumentException {
        String countryRegex = "^[A-Za-z\\s-]+$";
        if (country == null || country.isEmpty()) {
            throw new IllegalArgumentException("- Kein Land angegeben");
        }
        if (!country.matches(countryRegex)) {
            throw new IllegalArgumentException("- Land enthält ungültige Zeichen");
        }
    }

    /**
     * Validates the customer's address.
     * The address must not be empty.
     *
     * @param address The address to validate
     * @throws IllegalArgumentException If the address is invalid
     */
    public void checkAddress(String address) throws IllegalArgumentException{
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("- Keine Adresse angegeben");
        }
    }

    /**
     * Validates the customer's email address and checks if it exists in the database.
     * The email must not be empty and must match a basic email format.
     *
     * Regex pattern used:
     * - "^[A-Za-z0-9+_.-]+@(.+)$" - Matches valid email addresses with alphanumeric characters,
     *   plus signs, dots, and hyphens before @, followed by any domain
     *
     * @param email The email address to validate
     * @throws IllegalArgumentException If the email is invalid
     */
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