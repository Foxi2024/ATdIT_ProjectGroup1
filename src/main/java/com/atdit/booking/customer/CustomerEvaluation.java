package com.atdit.booking.customer;

import com.atdit.booking.Main;
import com.atdit.booking.financialdata.FinancialInformation;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomerEvaluation {

    public final FinancialInformation financialInformation;
    public final Customer customer;

    public CustomerEvaluation(Customer customer) {

        this.customer = customer;
        this.financialInformation = customer.getFinancialInformation();
    }

    public boolean hasOptimalSchufaScore(){

        return this.financialInformation.getSchufaauskunft().score() > 0.975;
    }

    public boolean evalCustomer1(int journeyPrice){

        return this.financialInformation.getCurrentNetWorth() * 0.2 > journeyPrice &&
                financialInformation.getMonthlyAvailableMoney() > Main.MIN_MONTHLY_MONEY &&
                hasOptimalSchufaScore();
    }

}
