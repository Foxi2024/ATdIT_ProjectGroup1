package com.atdit.booking.customer;


import com.atdit.booking.Main;
import com.atdit.booking.financialdata.FinancialInformation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class Customer {

    private final String firstName;
    private final String name;
    private final String nationality;
    private final String country;
    private final int hash;

    private FinancialInformation financialInformation;

    public Customer(String firstName, String name, String nationality, String country, int avgNetIncome, int monthlyFixCost, int minCostOfLiving, int netWorth, FinancialInformation financialInformation){

        this.firstName = firstName;
        this.name = name;
        this.nationality = nationality;
        this.country = country;
        this.hash = this.hashCode();
        this.financialInformation = financialInformation;

        addCustomerToDatabase(this);
    }


    public String getFirstName() {
        return firstName;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public String getCountry() {
        return country;
    }

    public int getHash() {
        return hash;
    }

    public FinancialInformation getFinancialInformation() {
        return financialInformation;
    }

    private static void addCustomerToDatabase(Customer customer){
        Connection conn = CustomerDatabase.getConn();

       // check if hash is already in the database
        String checkQuery = "SELECT COUNT(*) FROM customers WHERE hash = " + customer.hash;

        try (Statement statement = conn.createStatement()) {
            ResultSet result = statement.executeQuery(checkQuery);
            System.out.println("Query executed!");
            result.next();
            int count = result.getInt(1);
            if (count > 0) {
                return;
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }


        //first part of the query
        String query = """
        INSERT INTO customers (
        first_name,
        name,
        nationality,
        country,
        avg_net_income,
        monthly_fix_cost,
        net_worth,
        monthly_available_money,
        hash)
        VALUES(?, ?, ?, ?, ?, ?);
        """;


        //concat the customer details to a one string for the values part of the query
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, customer.firstName);
            preparedStatement.setString(2, customer.name);
            preparedStatement.setString(3, customer.nationality);
            preparedStatement.setString(4, customer.country);
            preparedStatement.setInt(9, customer.hash);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert Customer data into the database
        CustomerDatabase.executeUpdate(conn, query);
    }
}
