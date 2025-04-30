package com.atdit.booking.financialdata;

import com.atdit.booking.customer.CustomerDatabase;

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

        this.summedMonthlyRates = schufaauskunft.creditList().stream().mapToInt(Credit::monthlyPayment).sum();

        if(monthlyFixCost < rent + summedMonthlyRates){
            throw new IllegalArgumentException("Monthly fixed cost cannot be less than rent and minimum cost of living");
        }

        this.avgNetIncome = avgNetIncome;
        this.rent = rent;
        this.monthlyFixCost = monthlyFixCost;
        this.minCostOfLiving = minCostOfLiving;
        this.schufaauskunft = schufaauskunft;

        this.fixedAssets = fixedAssets.stream().mapToInt(FixedAsset::value).sum();
        this.liquidAssets = liquidAssets.stream().mapToInt(LiquidAsset::balance).sum();
        this.debt = schufaauskunft.creditList().stream().mapToInt(Credit::amountOwed).sum();

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

    //create a method to create a financialinformation table in the customer database
    public static void createFinancialInformationTable() {
        String sql = "CREATE TABLE IF NOT EXISTS financialinformation (\n"
                + " avgNetIncome INTEGER,\n"
                + " rent INTEGER,\n"
                + " monthlyFixCost INTEGER,\n"
                + " fixedAssets INTEGER,\n"
                + " liquidAssets INTEGER,\n"
                + " minCostOfLiving INTEGER,\n"
                + " schufaauskunft TEXT,\n"
                + " debt INTEGER,\n"
                + " monthlyAvailableMoney INTEGER\n"
                + ");";
        try (var stmt = CustomerDatabase.getConn().createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //INSERT INTO "main"."financialinformation" ("avgNetIncome", "rent", "monthlyFixCost", "fixedAssets", "liquidAssets", "minCostOfLiving", "debt", "monthlyAvailableMoney") VALUES (NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
    }

}