package com.atdit.booking.financialdata;

import com.atdit.booking.customer.CustomerDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FinancialInformation {

    private int avgNetIncome;
    private IncomeProof proofOfIncome;
    private int liquidAssets;
    private LiquidAsset proofOfLiquidAssets;
    private int monthlyFixCost;
    private int minCostOfLiving;
    private int monthlyAvailableMoney;
    private SchufaOverview schufa;


    public void setAvgNetIncome(int avgNetIncome) {

        if (avgNetIncome < 0) {
            throw new IllegalArgumentException("Average net income cannot be negative");
        }
        this.avgNetIncome = avgNetIncome;
        updateMonthlyAvailableMoney();
    }

    public void setProofOfLiquidAssets(LiquidAsset proofOfLiquidAssets){

        this.proofOfLiquidAssets = proofOfLiquidAssets;
    }


    public void setMonthlyFixCost(int monthlyFixCost) {
        this.monthlyFixCost = monthlyFixCost;
        updateMonthlyAvailableMoney();
    }

    public void setLiquidAssets(int liquidAssets) {
        this.liquidAssets = liquidAssets;
    }

    public void setMinCostOfLiving(int minCostOfLiving) {
        this.minCostOfLiving = minCostOfLiving;
        updateMonthlyAvailableMoney();
    }

    public void setSchufa(SchufaOverview schufa) {
        this.schufa = schufa;
    }

    public void setProofOfIncome(IncomeProof proofOfIncome) {
        this.proofOfIncome = proofOfIncome;
    }

    public void setMonthlyAvailableMoney(int monthlyAvailableMoney) {
        this.monthlyAvailableMoney = monthlyAvailableMoney;
    }

    private void updateMonthlyAvailableMoney() {
        this.monthlyAvailableMoney = this.avgNetIncome - this.monthlyFixCost - this.minCostOfLiving;
    }



    public int getAvgNetIncome() {
        return avgNetIncome;
    }

    public IncomeProof getProofOfIncome() {
        return proofOfIncome;
    }

    public LiquidAsset getProofOfLiquidAssets() {
        return proofOfLiquidAssets;
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

    public SchufaOverview getSchufa() {
        return schufa;
    }

    public int getMonthlyAvailableMoney() {
        return monthlyAvailableMoney;
    }

}