package com.atdit.booking.backend.financialdata.financial_information;

/**
 * This class represents financial information of a person or entity.
 * It contains details about income, assets, costs and credit rating (SCHUFA).
 */
public class FinancialInformation {

    /** Average net income per month */
    private int avgNetIncome;
    /** Proof document for the stated income */
    private IncomeProof proofOfIncome;
    /** Amount of liquid assets (easily convertible to cash) */
    private int liquidAssets;
    /** Proof document for the stated liquid assets */
    private LiquidAsset proofOfLiquidAssets;
    /** Fixed monthly costs (e.g. rent, insurance) */
    private int monthlyFixCost;
    /** Minimum cost of living per month */
    private int minCostOfLiving;
    /** Money available per month after deducting fixed costs and minimum living costs */
    private int monthlyAvailableMoney;
    /** Overview of the SCHUFA credit rating */
    private SchufaOverview schufa;

    /**
     * Sets the average net income and updates the monthly available money.
     * @param avgNetIncome The average net income to set
     * @throws IllegalArgumentException if avgNetIncome is negative
     */
    public void setAvgNetIncome(int avgNetIncome) {
        if (avgNetIncome < 0) {
            throw new IllegalArgumentException("Average net income cannot be negative");
        }
        this.avgNetIncome = avgNetIncome;
        updateMonthlyAvailableMoney();
    }

    /**
     * Sets the proof document for liquid assets.
     * @param proofOfLiquidAssets The proof document to set
     */
    public void setProofOfLiquidAssets(LiquidAsset proofOfLiquidAssets){
        this.proofOfLiquidAssets = proofOfLiquidAssets;
    }

    /**
     * Sets the monthly fixed costs and updates the monthly available money.
     * @param monthlyFixCost The monthly fixed costs to set
     */
    public void setMonthlyFixCost(int monthlyFixCost) {
        this.monthlyFixCost = monthlyFixCost;
        updateMonthlyAvailableMoney();
    }

    /**
     * Sets the amount of liquid assets.
     * @param liquidAssets The amount of liquid assets to set
     */
    public void setLiquidAssets(int liquidAssets) {
        this.liquidAssets = liquidAssets;
    }

    /**
     * Sets the minimum cost of living and updates the monthly available money.
     * @param minCostOfLiving The minimum cost of living to set
     */
    public void setMinCostOfLiving(int minCostOfLiving) {
        this.minCostOfLiving = minCostOfLiving;
        updateMonthlyAvailableMoney();
    }

    /**
     * Sets the SCHUFA credit rating overview.
     * @param schufa The SCHUFA overview to set
     */
    public void setSchufa(SchufaOverview schufa) {
        this.schufa = schufa;
    }

    /**
     * Sets the proof of income document.
     * @param proofOfIncome The proof of income document to set
     */
    public void setProofOfIncome(IncomeProof proofOfIncome) {
        this.proofOfIncome = proofOfIncome;
    }

    /**
     * Sets the monthly available money directly.
     * @param monthlyAvailableMoney The monthly available money to set
     */
    public void setMonthlyAvailableMoney(int monthlyAvailableMoney) {
        this.monthlyAvailableMoney = monthlyAvailableMoney;
    }

    /**
     * Updates the monthly available money based on income, fixed costs and minimum living costs.
     */
    private void updateMonthlyAvailableMoney() {
        this.monthlyAvailableMoney = this.avgNetIncome - this.monthlyFixCost - this.minCostOfLiving;
    }

    /**
     * @return The average net income
     */
    public int getAvgNetIncome() {
        return avgNetIncome;
    }

    /**
     * @return The proof of income document
     */
    public IncomeProof getProofOfIncome() {
        return proofOfIncome;
    }

    /**
     * @return The proof of liquid assets document
     */
    public LiquidAsset getProofOfLiquidAssets() {
        return proofOfLiquidAssets;
    }

    /**
     * @return The monthly fixed costs
     */
    public int getMonthlyFixCost() {
        return monthlyFixCost;
    }

    /**
     * @return The amount of liquid assets
     */
    public int getLiquidAssets() {
        return liquidAssets;
    }

    /**
     * @return The minimum cost of living
     */
    public int getMinCostOfLiving() {
        return minCostOfLiving;
    }

    /**
     * @return The SCHUFA credit rating overview
     */
    public SchufaOverview getSchufa() {
        return schufa;
    }

    /**
     * @return The monthly available money
     */
    public int getMonthlyAvailableMoney() {
        return monthlyAvailableMoney;
    }
}