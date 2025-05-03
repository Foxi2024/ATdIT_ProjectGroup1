package com.atdit.booking.financialdata;

import com.atdit.booking.customer.CustomerDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;

public class FinancialInformation {

    private int avgNetIncome;
    private int proofOfIncome;
    private int liquidAssets;
    private int proofOfLiquidAssets;
    private int rent;
    private int monthlyFixCost;
    private int minCostOfLiving;
    private int debt;
    private int monthlyAvailableMoney;
    private int summedMonthlyRates;
    private Schufaauskunft schufaauskunft;


    public void setAvgNetIncome(int avgNetIncome) {
        if (avgNetIncome < 0) {
            throw new IllegalArgumentException("Average net income cannot be negative");
        }
        this.avgNetIncome = avgNetIncome;
        updateMonthlyAvailableMoney();
    }

    public void setRent(int rent) {
        if (rent < 0) {
            throw new IllegalArgumentException("Rent cannot be negative");
        }
        this.rent = rent;
        updateMonthlyAvailableMoney();
    }

    public void setMonthlyFixCost(int monthlyFixCost) {
        if (monthlyFixCost < 0) {
            throw new IllegalArgumentException("Monthly fixed cost cannot be negative");
        }
        if (monthlyFixCost < rent + summedMonthlyRates) {
            throw new IllegalArgumentException("Monthly fixed cost cannot be less than rent and minimum cost of living");
        }
        this.monthlyFixCost = monthlyFixCost;
        updateMonthlyAvailableMoney();
    }

    public void setLiquidAssets(int liquidAssets) {
        this.liquidAssets = liquidAssets;
    }

    public void setMinCostOfLiving(int minCostOfLiving) {
        if (minCostOfLiving < 0) {
            throw new IllegalArgumentException("Minimum cost of living cannot be negative");
        }
        this.minCostOfLiving = minCostOfLiving;
        updateMonthlyAvailableMoney();
    }

    public void setSchufaauskunft(Schufaauskunft schufaauskunft) {
        if (schufaauskunft == null) {
            throw new IllegalArgumentException("Schufaauskunft cannot be null");
        }
        this.schufaauskunft = schufaauskunft;
        this.summedMonthlyRates = schufaauskunft.creditList().stream().mapToInt(Credit::monthlyPayment).sum();
        this.debt = schufaauskunft.creditList().isEmpty() ? 0 : schufaauskunft.creditList().stream().mapToInt(Credit::amountOwed).sum();
        updateMonthlyAvailableMoney();
    }

    private void updateMonthlyAvailableMoney() {
        this.monthlyAvailableMoney = this.avgNetIncome - this.monthlyFixCost - this.minCostOfLiving;
    }



    public int getAvgNetIncome() {
        return avgNetIncome;
    }

    public int getRent() {
        return rent;
    }

    public int getMonthlyFixCost() {
        return monthlyFixCost;
    }

    public int getLiquidAssets() {
        return liquidAssets;
    }

    public int getMinCostOfLiving() {
        return minCostOfLiving;
    }

    public Schufaauskunft getSchufaauskunft() {
        return schufaauskunft;
    }

    public int getDebt() {
        return debt;
    }

    public int getMonthlyAvailableMoney() {
        return monthlyAvailableMoney;
    }

    public int getSummedMonthlyRates() {
        return summedMonthlyRates;
    }


    public void addFinancialInformationToDatabase(){
        Connection conn = CustomerDatabase.getConn();

        String query = "INSERT INTO financialinformation ('avgNetIncome', 'rent', 'monthlyFixCost', 'fixedAssets', 'liquidAssets', 'minCostOfLiving', 'debt', 'monthlyAvailableMoney') VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        //concat the customer details to a one string for the values part of the query
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, this.avgNetIncome);
            preparedStatement.setInt(2, this.rent);
            preparedStatement.setInt(3, this.monthlyFixCost);
            preparedStatement.setInt(5, this.liquidAssets);
            preparedStatement.setInt(6, this.minCostOfLiving);
            preparedStatement.setInt(7, this.debt);
            preparedStatement.setInt(8, this.monthlyAvailableMoney);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}