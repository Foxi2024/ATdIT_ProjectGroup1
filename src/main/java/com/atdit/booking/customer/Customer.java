package com.atdit.booking.customer;


import com.atdit.booking.Main;
import com.atdit.booking.financialdata.FinancialInformation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class Customer {

    private String title;
    private String firstName;
    private String name;
    private String country;
    private String birthdate;
    private String postalCode;
    private String city;
    private String streetname;
    private String houseNumber;
    private int hash;
    private String email;

    private FinancialInformation financialInformation;

    public Customer(){
        this.financialInformation = new FinancialInformation();

        this.title = "";
        this.firstName = "";
        this.name = "";
        this.country = "";
        this.birthdate = "";
        this.postalCode = "";
        this.city = "";
        this.streetname = "";
        this.houseNumber = "";
        this.email = "";
    }

    private void addCustomerToDatabase() {
        Connection conn = CustomerDatabase.getConn();

        // check if hash is already in the database
        String checkQuery = "SELECT COUNT(*) FROM customers WHERE hash = " + this.hash;

        try (Statement statement = conn.createStatement()) {
            ResultSet result = statement.executeQuery(checkQuery);
            System.out.println("Query executed!");
            result.next();
            int count = result.getInt(1);
            if (count > 0) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // first part of the query
        String query = """
        INSERT INTO customers (
        first_name,
        name,
        birthdate,
        country,
        postal_code,
        city,
        street_name,
        house_number,
        hash)
        VALUES(?,?,?,?,?,?,?,?,?,?);
        """;

        // concat the customer details to a one string for the values part of the query
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, this.firstName);
            preparedStatement.setString(2, this.name);
            preparedStatement.setString(3, this.birthdate);
            preparedStatement.setString(4, this.country);
            preparedStatement.setString(5, this.postalCode);
            preparedStatement.setString(6, this.city);
            preparedStatement.setString(7, this.streetname);
            preparedStatement.setString(8, this.houseNumber);
            preparedStatement.setInt(9, this.hash);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert Customer data into the database
        CustomerDatabase.executeUpdate(conn, query);
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

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public void setFinancialInformation(FinancialInformation financialInformation) {
        this.financialInformation = financialInformation;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCountry() {
        return country;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreetname() {
        return streetname;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public int getHash() {
        return hash;
    }

    public String getEmail() {
        return email;
    }

    public FinancialInformation getFinancialInformation() {
        return financialInformation;
    }
}
