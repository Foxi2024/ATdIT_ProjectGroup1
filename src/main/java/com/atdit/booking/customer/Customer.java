package com.atdit.booking.customer;


import com.atdit.booking.Main;
import com.atdit.booking.financialdata.FinancialInformation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class Customer {

    private String firstName;
    private String name;
    private String country;
    private String birthdate;
    private int postalCode;
    private String city;
    private String streetname;
    private int houseNumber;
    private int hash;

    private FinancialInformation financialInformation;

    public Customer(String firstName, String name, String country, FinancialInformation financialInformation){

        this.firstName = firstName;
        this.name = name;
        this.country = country;
        this.hash = this.hashCode();
        this.financialInformation = financialInformation;

        this.addCustomerToDatabase();
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
            preparedStatement.setInt(5, this.postalCode);
            preparedStatement.setString(6, this.city);
            preparedStatement.setString(7, this.streetname);
            preparedStatement.setInt(8, this.houseNumber);
            preparedStatement.setInt(9, this.hash);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert Customer data into the database
        CustomerDatabase.executeUpdate(conn, query);
    }

    public FinancialInformation getFinancialInformation() {
        return financialInformation;
    }
}
