package com.atdit.booking.financialdata;

import java.util.ArrayList;

public record Schufaauskunft(String firstName, String lastName, float score, ArrayList<Credit> creditList, String issueDate) {

}
