package com.atdit.booking.backend.financialdata.financial_information;

import java.util.ArrayList;

/**
 * Record representing a SCHUFA credit report, which contains personal information and credit history.
 * SCHUFA (Schutzgemeinschaft für allgemeine Kreditsicherung) is the main credit bureau in Germany.
 *
 * @param firstName   The first name of the person the report belongs to
 * @param lastName    The last name of the person the report belongs to
 * @param score      The SCHUFA score value, typically ranging from 0 to 100,
 *                   where higher values indicate better creditworthiness
 * @param creditList A list of credits associated with this person
 * @param issueDate  The date when this SCHUFA report was issued
 */
public record Schufaauskunft(String firstName, String lastName, float score, ArrayList<Credit> creditList, String issueDate) {

}