package com.atdit.booking.financialdata;

import com.atdit.booking.customer.Customer;
import com.atdit.booking.customer.CustomerDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class FinancialInformation {

    private int avgNetIncome;
    private int rent;
    private int monthlyFixCost;
    private int fixedAssets;
    private int liquidAssets;
    private int minCostOfLiving;
    private Schufaauskunft schufaauskunft;
    private int debt;
    private int monthlyAvailableMoney;
    private int currentNetWorth;
    private int summedMonthlyRates;


    public void setAvgNetIncome(int avgNetIncome) {
        if (avgNetIncome < 0) {
            throw new IllegalArgumentException("Average net income cannot be negative");
        }
        this.avgNetIncome = avgNetIncome;
        updateDerivedValues();
    }

    public void setRent(int rent) {
        if (rent < 0) {
            throw new IllegalArgumentException("Rent cannot be negative");
        }
        this.rent = rent;
        updateDerivedValues();
    }

    public void setMonthlyFixCost(int monthlyFixCost) {
        if (monthlyFixCost < 0) {
            throw new IllegalArgumentException("Monthly fixed cost cannot be negative");
        }
        if (monthlyFixCost < rent + summedMonthlyRates) {
            throw new IllegalArgumentException("Monthly fixed cost cannot be less than rent and minimum cost of living");
        }
        this.monthlyFixCost = monthlyFixCost;
        updateDerivedValues();
    }

    public void setFixedAssets(int fixedAssets) {
        this.fixedAssets = fixedAssets;
        updateNetWorth();
    }

    public void setLiquidAssets(int liquidAssets) {
        this.liquidAssets = liquidAssets;
        updateNetWorth();
    }

    public void setMinCostOfLiving(int minCostOfLiving) {
        if (minCostOfLiving < 0) {
            throw new IllegalArgumentException("Minimum cost of living cannot be negative");
        }
        this.minCostOfLiving = minCostOfLiving;
        updateDerivedValues();
    }

    public void setSchufaauskunft(Schufaauskunft schufaauskunft) {
        if (schufaauskunft == null) {
            throw new IllegalArgumentException("Schufaauskunft cannot be null");
        }
        this.schufaauskunft = schufaauskunft;
        this.summedMonthlyRates = schufaauskunft.creditList().stream().mapToInt(Credit::monthlyPayment).sum();
        this.debt = schufaauskunft.creditList().isEmpty() ? 0 :
                schufaauskunft.creditList().stream().mapToInt(Credit::amountOwed).sum();
        updateDerivedValues();
    }

    private void updateDerivedValues() {
        this.monthlyAvailableMoney = avgNetIncome - monthlyFixCost - minCostOfLiving;
        updateNetWorth();
    }

    private void updateNetWorth() {
        this.currentNetWorth = this.fixedAssets + this.liquidAssets - this.debt;
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

    public int getFixedAssets() {
        return fixedAssets;
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

    public int getCurrentNetWorth() {
        return currentNetWorth;
    }

    public int getSummedMonthlyRates() {
        return summedMonthlyRates;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FinancialInformation) obj;
        return this.avgNetIncome == that.avgNetIncome &&
                this.rent == that.rent &&
                this.monthlyFixCost == that.monthlyFixCost &&
                this.fixedAssets == that.fixedAssets &&
                this.liquidAssets == that.liquidAssets &&
                this.minCostOfLiving == that.minCostOfLiving &&
                Objects.equals(this.schufaauskunft, that.schufaauskunft) &&
                this.debt == that.debt &&
                this.monthlyAvailableMoney == that.monthlyAvailableMoney;
    }

    @Override
    public int hashCode() {
        return Objects.hash(avgNetIncome, rent, monthlyFixCost, fixedAssets, liquidAssets, minCostOfLiving, schufaauskunft, debt, monthlyAvailableMoney);
    }

    @Override
    public String toString() {
        return "FinancialInformation[" +
                "avgNetIncome=" + avgNetIncome + ", " +
                "rent=" + rent + ", " +
                "monthlyFixCost=" + monthlyFixCost + ", " +
                "fixedAssets=" + fixedAssets + ", " +
                "liquidAssets=" + liquidAssets + ", " +
                "minCostOfLiving=" + minCostOfLiving + ", " +
                "schufaauskunft=" + schufaauskunft + ", " +
                "debt=" + debt + ", " +
                "monthlyAvailableMoney=" + monthlyAvailableMoney + ']';
    }

    public void addFinancialInformationToDatabase(){
        Connection conn = CustomerDatabase.getConn();

        String query = "INSERT INTO financialinformation ('avgNetIncome', 'rent', 'monthlyFixCost', 'fixedAssets', 'liquidAssets', 'minCostOfLiving', 'debt', 'monthlyAvailableMoney') VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        //concat the customer details to a one string for the values part of the query
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, this.avgNetIncome);
            preparedStatement.setInt(2, this.rent);
            preparedStatement.setInt(3, this.monthlyFixCost);
            preparedStatement.setInt(4, this.fixedAssets);
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