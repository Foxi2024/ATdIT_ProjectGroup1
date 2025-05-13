package com.atdit.booking.backend.financialdata.contracts;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a one-time payment contract for space travel bookings.
 * This class extends the base Contract class and implements specific functionality
 * for contracts where the total amount is paid in a single transaction.
 */
public class OneTimePaymentContract extends Contract {

    /** The total amount to be paid for the space travel */
    private final double totalAmount;

    /**
     * Constructor initializes a new one-time payment contract.
     * Sets the total amount from the parent class's TOTAL_AMOUNT constant.
     */
    public OneTimePaymentContract() {
        this.totalAmount = super.TOTAL_AMOUNT;
    }

    /**
     * Generates the complete contract text in German language.
     * The contract includes sections about:
     * - Subject of the agreement
     * - Payment amount and modalities
     * - Payment terms
     * - Cancellation policy
     * - Health requirements and approvals
     * - Data protection
     * - Miscellaneous legal provisions
     *
     * @return A formatted string containing the complete contract text with
     *         automatically inserted current date, total amount (twice) and payment method
     */
    public String getContractText() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateIssued = now.format(formatter);

        return """
    §1 Gegenstand der Vereinbarung

    Der Kunde beabsichtigt, eine kommerzielle Weltraumreise gemäß dem Angebot des Anbieters vom %s in Anspruch zu nehmen. Der Gesamtpreis der Reise beträgt %.2f €.

    Diese Vereinbarung regelt die einmalige Zahlung des vollständigen Betrags.

    §2 Zahlungssumme und Zahlungsmodalitäten

    Der zu zahlende Gesamtbetrag beträgt %.2f €.

    Die Zahlung erfolgt als Einmalzahlung und ist sofort fällig.

    §3 Zahlungsbedingungen

    Die Zahlung erfolgt per %s auf das Konto des Anbieters.

    Bei Zahlungsverzug fallen Verzugszinsen in gesetzlicher Höhe sowie ggf. Mahngebühren an.

    §4 Rücktritt und Stornierung

    Ein Rücktritt von der Reise ist bis 60 Tage vor Abflug gegen eine Gebühr von 15 %% des Reisepreises möglich.

    Bei späterem Rücktritt oder Nichterscheinen ist der volle Reisepreis fällig.

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
                dateIssued,
                super.TOTAL_AMOUNT,
                super.TOTAL_AMOUNT,
                super.getPaymentMethod()
        );
    }


}