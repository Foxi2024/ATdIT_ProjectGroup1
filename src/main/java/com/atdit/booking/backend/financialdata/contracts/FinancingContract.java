package com.atdit.booking.backend.financialdata.contracts;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class represents a financing contract for space travel bookings.
 * It extends the base Contract class and handles all financial calculations
 * including interest rates, monthly payments and payment schedules.
 */
public class FinancingContract extends Contract {

    /** Fixed base interest rate of 5.0% */
    private final double INTEREST_RATE = 5.0;

    /** Required down payment as percentage (20%) of total amount */
    private final double DOWN_PAYMENT_PERCENTAGE = 0.2;

    /** Unique identifier for this contract */
    private int id;

    /** ID of the customer who signed this contract */
    private int customerId;

    /** Amount that is being financed (total amount minus down payment) */
    private final double financedAmount;

    /** Calculated monthly payment amount */
    private double monthlyPayment;

    /** Calculated interest rate based on financing duration */
    private double interestRate;

    /** Total amount including interest */
    private double amountWithInterest;

    /** Duration of financing in months */
    private int months;

    /** Initial down payment amount */
    private final double downPayment;

    /** Date when contract was signed */
    private LocalDateTime signedDate;

    /** Full contract text in German */
    private String contractText;

    /**
     * Constructor initializes a new financing contract with default 12 months duration.
     * Calculates down payment, financed amount and all related financial values.
     */
    public FinancingContract() {

        this.downPayment = super.TOTAL_AMOUNT * DOWN_PAYMENT_PERCENTAGE;
        this.financedAmount = super.TOTAL_AMOUNT - this.downPayment;
        this.months = 12;

        calculateInterestRate();
        calculateAmountWithInterest();
        calculateMonthlyPayment();
    }

    /**
     * Updates the financing duration and recalculates all financial values.
     * @param months New duration in months
     */
    public void setMonths(int months) {
        this.months = months;

        calculateInterestRate();
        calculateAmountWithInterest();
        calculateMonthlyPayment();
    }

    /**
     * @return The monthly payment amount
     */
    public double getMonthlyPayment() {
        return this.monthlyPayment;
    }

    /**
     * @return The total amount including interest
     */
    public double getAmountWithInterest() {
        return this.amountWithInterest;
    }

    /**
     * @return The financing duration in months
     */
    public int getMonths() {
        return months;
    }

    /**
     * Calculates the interest rate based on financing duration.
     * Base rate is 5.0% with 0.5% increase per year after first year.
     */
    public void calculateInterestRate() {
        this.interestRate = INTEREST_RATE + ((this.months / 12) - 1) * 0.5;
    }

    /**
     * Calculates the total amount including interest.
     */
    private void calculateAmountWithInterest() {
        this.amountWithInterest = (this.financedAmount) + (this.financedAmount * interestRate / 100);
    }

    /**
     * Calculates the monthly payment amount.
     * Sets payment to 0 if months is 0.
     */
    private void calculateMonthlyPayment() {
        if (months == 0) {
            this.monthlyPayment = 0;
            return;
        }
        this.monthlyPayment = amountWithInterest / months;
    }

    /**
     * @return The current interest rate
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * @return The amount being financed
     */
    public double getFinancedAmount() {
        return financedAmount;
    }

    /**
     * @return The down payment amount
     */
    public double getDownPayment() {
        return downPayment;
    }

    /**
     * Generates the complete contract text in German.
     * Includes all financing details, terms and conditions.
     * @return Formatted contract text as String
     */
    public String getContractText() {
        return """
    §1 Gegenstand der Vereinbarung

    Der Kunde beabsichtigt, eine kommerzielle Weltraumreise gemäß dem Angebot des Anbieters vom %s in Anspruch zu nehmen. Der Gesamtpreis der Reise beträgt %.2f €.

    Diese Vereinbarung regelt die Finanzierung dieses Betrags über eine individuell vereinbarte Laufzeit.

    §2 Finanzierungssumme und Zahlungsmodalitäten

    Der zu finanzierende Gesamtbetrag beträgt %.2f €.

    Die Finanzierung erfolgt in %d monatlichen Raten à %.2f €, beginnend ab dem %s.

    Ein effektiver Jahreszins von %.1f %% wird berechnet.

    Der Gesamtbetrag inklusive Zinsen beläuft sich auf %.2f €.

    §3 Zahlungsbedingungen

    Die monatliche Rate ist jeweils zum 15. eines Monats fällig.

    Zahlungen erfolgen per %s auf das Konto des Anbieters.

    Bei Zahlungsverzug fallen Verzugszinsen in gesetzlicher Höhe sowie ggf. Mahngebühren an.

    §4 Rücktritt und Stornierung

    Ein Rücktritt von der Reise ist bis 60 Tage vor Abflug gegen eine Gebühr von 15 %% des Reisepreises möglich.

    Bei späterem Rücktritt oder Nichterscheinen ist der volle Reisepreis fällig.

    Bereits geleistete Raten werden bei Rücktritt mit etwaigen Stornokosten verrechnet.

    §5 Gesundheitsvoraussetzungen und Genehmigungen

    Der Kunde verpflichtet sich, alle medizinischen und regulatorischen Anforderungen für die Teilnahme an der Reise zu erfüllen.

    Sollte eine behördliche Freigabe oder ein medizinischer Check nicht bestanden werden, kann die Reise auf einen späteren Zeitpunkt verschoben werden – zusätzliche Kosten trägt der Kunde.

    §6 Datenschutz und Datenverarbeitung

    Der Kunde erklärt sich mit der Verarbeitung seiner personenbezogenen Daten zur Durchführung dieser Vereinbarung und zur Kommunikation mit Partnern im Rahmen der Weltraummission einverstanden.

    §7 Sonstiges

    Änderungen und Ergänzungen dieser Vereinbarung bedürfen der Schriftform.

    Sollte eine Bestimmung dieser Vereinbarung unwirksam sein, bleibt die Wirksamkeit der übrigen Bestimmungen unberührt.

    Es gilt das Recht der Bundesrepublik Deutschland. Gerichtsstand ist Berlin.
    """.formatted(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                super.TOTAL_AMOUNT,
                super.TOTAL_AMOUNT,
                this.months,
                this.monthlyPayment,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                this.interestRate,
                this.amountWithInterest,
                super.getPaymentMethod()
        );

    }
}