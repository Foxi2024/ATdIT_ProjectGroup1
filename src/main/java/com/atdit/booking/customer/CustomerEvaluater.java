package com.atdit.booking.customer;

import com.atdit.booking.Main;
import com.atdit.booking.financialdata.FinancialInformation;

public class CustomerEvaluater {

    public final FinancialInformation financialInformation;
    public final Customer customer;


    public CustomerEvaluater(Customer customer) {
        this.customer = customer;
        this.financialInformation = customer.getFinancialInformation();
    }

    public boolean hasOptimalSchufaScore(){
        return this.financialInformation.getSchufaauskunft().score() > 0.975;
    }

    public boolean evalCustomer1(int journeyPrice){
        return this.financialInformation.getLiquidAssets() > journeyPrice * 0.2 &&
                financialInformation.getMonthlyAvailableMoney() > Main.MIN_MONTHLY_MONEY;
    }

}
