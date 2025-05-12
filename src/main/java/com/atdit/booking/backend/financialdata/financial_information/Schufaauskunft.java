package com.atdit.booking.backend.financialdata.financial_information;

import java.util.ArrayList;

public record Schufaauskunft(String firstName, String lastName, float score, ArrayList<Credit> creditList, String issueDate) {

}
