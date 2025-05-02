package com.atdit.booking.financialdata;

import com.atdit.booking.customer.Customer;
import com.atdit.booking.customer.CustomerDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public final class FinancialInformation {


    private final int avgNetIncome;
    private final int rent;
    private final int monthlyFixCost;
    private final int fixedAssets;
    private final int liquidAssets;
    private final int minCostOfLiving;
    private final Schufaauskunft schufaauskunft;
    private final int debt;
    private final int monthlyAvailableMoney;
    private final int currentNetWorth;
    private final int summedMonthlyRates;


    public FinancialInformation(int avgNetIncome,
                                int rent,
                                int monthlyFixCost,
                                ArrayList<FixedAsset> fixedAssets,
                                ArrayList<LiquidAsset> liquidAssets,
                                int minCostOfLiving,
                                Schufaauskunft schufaauskunft,
                                int monthlyAvailableMoney) {

        if (avgNetIncome < 0) {
            throw new IllegalArgumentException("Average net income cannot be negative");
        }

        if (rent < 0) {
            throw new IllegalArgumentException("Rent cannot be negative");
        }

        if (monthlyFixCost < 0) {
            throw new IllegalArgumentException("Monthly fixed cost cannot be negative");
        }

        if (minCostOfLiving < 0) {
            throw new IllegalArgumentException("Minimum cost of living cannot be negative");
        }

        if (fixedAssets == null) {
            throw new IllegalArgumentException("Fixed assets cannot be null");
        }

        if (liquidAssets == null) {
            throw new IllegalArgumentException("Liquid assets cannot be null");
        }

        if (schufaauskunft == null) {
            throw new IllegalArgumentException("Schufaauskunft cannot be null");
        }

        if (monthlyAvailableMoney < 0) {
            throw new IllegalArgumentException("Monthly available money cannot be negative");
        }


        this.summedMonthlyRates = schufaauskunft.creditList().stream().mapToInt(Credit::monthlyPayment).sum();

        if(monthlyFixCost < rent + summedMonthlyRates){
            throw new IllegalArgumentException("Monthly fixed cost cannot be less than rent and minimum cost of living");
        }

        this.avgNetIncome = avgNetIncome;
        this.rent = rent;
        this.monthlyFixCost = monthlyFixCost;
        this.minCostOfLiving = minCostOfLiving;
        this.schufaauskunft = schufaauskunft;

        if(fixedAssets.isEmpty()){
            this.fixedAssets = 0;
        } else {
            this.fixedAssets = fixedAssets.stream().mapToInt(FixedAsset::value).sum();
        }

        if(liquidAssets.isEmpty()){
            this.liquidAssets = 0;
        } else {
            this.liquidAssets = liquidAssets.stream().mapToInt(LiquidAsset::balance).sum();
        }

        if(schufaauskunft.creditList().isEmpty()){
            this.debt = 0;
        } else {
            this.debt = schufaauskunft.creditList().stream().mapToInt(Credit::amountOwed).sum();
        }

        this.monthlyAvailableMoney = avgNetIncome - monthlyFixCost - minCostOfLiving;
        this.currentNetWorth = this.fixedAssets + this.liquidAssets - debt;
    }


    public int getCurrentNetWorth() {
        return fixedAssets + liquidAssets - debt;
    }

    public int avgNetIncome() {
        return avgNetIncome;
    }

    public int rent() {
        return rent;
    }

    public int monthlyFixCost() {
        return monthlyFixCost;
    }

    public int fixedAssets() {
        return fixedAssets;
    }

    public int liquidAssets() {
        return liquidAssets;
    }

    public int minCostOfLiving() {
        return minCostOfLiving;
    }

    public Schufaauskunft schufaauskunft() {
        return schufaauskunft;
    }

    public int debt() {
        return debt;
    }

    public int monthlyAvailableMoney() {
        return monthlyAvailableMoney;
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