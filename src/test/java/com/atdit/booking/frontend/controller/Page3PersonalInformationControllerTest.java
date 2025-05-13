package com.atdit.booking.frontend.controller;

import com.atdit.booking.CustomerRegistrationProcess;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.customer.CustomerEvaluator;

public class Page3PersonalInformationControllerTest {


    public final Customer customer = new Customer();
    public final CustomerEvaluator evaluator = new CustomerEvaluator(customer);



    testCustomer("dajd")


    public void testCustomer(){

        customer.setTitle("Herr");
        customer.setFirstName("");

        customer.setFirstname("...");

        evaluator.evaluateCustomerInfo();

        customer.set







    }

}
