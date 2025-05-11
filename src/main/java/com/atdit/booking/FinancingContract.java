package com.atdit.booking;

import javafx.scene.image.WritableImage;
import java.time.LocalDateTime;

public class FinancingContract extends Contract {

    private final double INTEREST_RATE = 5.0;
    private final double DOWN_PAYMENT_PERCENTAGE = 0.2;

    private int id;
    private int customerId;
    private double financedAmount;
    private double monthlyPayment;
    private double interestRate;
    private double amountWithInterest;
    private int months;
    private double downPayment;
    private LocalDateTime signedDate;
    private String contractText;


    public FinancingContract() {

        this.downPayment = super.TOTAL_AMOUNT * DOWN_PAYMENT_PERCENTAGE;
        this.financedAmount = super.TOTAL_AMOUNT - this.downPayment;
        this.months = 12;

        calculateInterestRate();
        calculateAmountWithInterest();
        calculateMonthlyPayment();
    }

    public void setMonths(int months) {
        this.months = months;

        calculateInterestRate();
        calculateAmountWithInterest();
        calculateMonthlyPayment();
    }

    public double getMonthlyPayment() {
        return this.monthlyPayment;
    }

    public double getAmountWithInterest() {
        return this.amountWithInterest;
    }


    public int getMonths() {
        return months;
    }

    public void calculateInterestRate() {
        this.interestRate = INTEREST_RATE + ((this.months / 12) - 1) * 0.5;
    }

    private void calculateAmountWithInterest() {
        this.amountWithInterest = (this.financedAmount) + (this.financedAmount * interestRate / 100);
    }


    private void calculateMonthlyPayment() {
        if (months == 0) {
            this.monthlyPayment = 0;
            return;
        }
        this.monthlyPayment = amountWithInterest / months;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getFinancedAmount() {
        return financedAmount;
    }

    public double getDownPayment() {
        return downPayment;
    }

    public String getContractText() {
        return """
                §1 Gegenstand der Vereinbarung
                
                Der Kunde beabsichtigt, eine kommerzielle Weltraumreise gemäß dem Angebot des Anbieters vom [Datum] in Anspruch zu nehmen. Der Gesamtpreis der Reise beträgt [z. B. 375.000 €].
                
                Diese Vereinbarung regelt die Finanzierung dieses Betrags über eine individuell vereinbarte Laufzeit.
                
                §2 Finanzierungssumme und Zahlungsmodalitäten
                
                Der zu finanzierende Gesamtbetrag beträgt [Betrag in €].
                
                Die Finanzierung erfolgt in [z. B. 24] monatlichen Raten à [z. B. 15.625 €], beginnend ab dem [Datum].
                
                Ein effektiver Jahreszins von [z. B. 3,5 %] wird berechnet.
                
                Der Gesamtbetrag inklusive Zinsen beläuft sich auf [Gesamtbetrag in €].
                
                §3 Zahlungsbedingungen
                
                Die monatliche Rate ist jeweils zum [z. B. 15.] eines Monats fällig.
                
                Zahlungen erfolgen per SEPA-Lastschrift oder Überweisung auf das Konto des Anbieters.
                
                Bei Zahlungsverzug fallen Verzugszinsen in gesetzlicher Höhe sowie ggf. Mahngebühren an.
                
                §4 Rücktritt und Stornierung
                
                Ein Rücktritt von der Reise ist bis [z. B. 60 Tage] vor Abflug gegen eine Gebühr von [z. B. 15 % des Reisepreises] möglich.
                
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
                
                Es gilt das Recht der Bundesrepublik Deutschland. Gerichtsstand ist [z. B. Berlin].
                """;
    }
}