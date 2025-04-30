package com.atdit.booking.financialdata;

import com.atdit.booking.customer.Customer;

import java.util.ArrayList;

public class FinancialData {

    private final int avgNetIncome;
    private final int rent;
    private final int creditRates;
    private final int monthlyFixCost;
    private final int fixedAssets;
    private final int liquidAssets;
    private final int monthlyAvailableMoney;
    private final int minCostOfLiving;
    private final int schufaScore;
    private final int debt;
    private final int currentNetWorth;
    //public final Customer customer;

    public FinancialData(int avgNetIncome, int rent, int creditRates, int monthlyFixCost, int fixedAssets, int liquidAssets, int minCostOfLiving, int schufaScore, int debt) {

        this.avgNetIncome = avgNetIncome;
        this.rent = rent;
        this.creditRates = creditRates;
        this.monthlyFixCost = monthlyFixCost;
        this.fixedAssets = fixedAssets;
        this.liquidAssets = liquidAssets;
        this.minCostOfLiving = minCostOfLiving;
        this.schufaScore = schufaScore;
        this.debt = debt;

        this.monthlyAvailableMoney = avgNetIncome - monthlyFixCost - minCostOfLiving;
        this.currentNetWorth = fixedAssets + liquidAssets - debt;

    }
}
