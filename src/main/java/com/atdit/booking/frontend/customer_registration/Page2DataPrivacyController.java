package com.atdit.booking.frontend.customer_registration;

import com.atdit.booking.frontend.super_controller.Controller;
import com.atdit.booking.frontend.super_controller.Navigatable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the data privacy page (page 2) of the booking application.
 * This class handles the display and interaction with the data privacy agreement.
 * Implements Initializable for JavaFX initialization and Navigatable for navigation functionality.
 */
public class Page2DataPrivacyController extends Controller implements Initializable, Navigatable {

    /** Radio button for accepting the data privacy agreement */
    @FXML private RadioButton acceptButton;

    /** Button to continue to the next page */
    @FXML private Button continueButton;

    /** Text area containing the data privacy agreement */
    @FXML private TextArea contractTextArea;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up the data privacy agreement text in the text area.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        contractTextArea.setText("""
                Datenschutzerklärung für NEXSTAR TRAVEL – Raumfahrttourismus \s
                Stand: Mai 2025
                
                1. Allgemeine Hinweise \s
                Der Schutz Ihrer personenbezogenen Daten hat für NEXSTAR TRAVEL oberste Priorität. Wir behandeln Ihre Daten vertraulich und entsprechend der gesetzlichen Datenschutzvorschriften, insbesondere der Datenschutz-Grundverordnung (DSGVO), sowie dieser Datenschutzerklärung. \s
                Diese Erklärung informiert Sie darüber, welche personenbezogenen Daten wir im Rahmen unserer Geschäftsprozesse (z. B. bei der Buchung einer Raumfahrtreise) erheben, zu welchen Zwecken wir sie verarbeiten, welche Rechte Sie haben und wie Sie diese geltend machen können.
                
                2. Verantwortlicher im Sinne der DSGVO \s
                NEXSTAR TRAVEL GmbH \s
                Galaxisweg 42 \s
                12345 Orbit City \s
                Telefon: +49 123 456789 \s
                E-Mail: datenschutz@nexstar-travel.com \s
                Verantwortliche Ansprechperson: Data Protection Officer (DPO)
                
                3. Kategorien verarbeiteter Daten \s
                3.1 Stammdaten: Vor- und Nachname, Geburtsdatum, Staatsangehörigkeit, Ausweisdaten (Pass, Visum, Raumflugzulassung) \s
                3.2 Kontaktdaten: Adresse, Telefonnummer, E-Mail-Adresse \s
                3.3 Buchungs- und Reisedaten: Reiseziel, gewünschter Flugzeitraum, Tickettyp, Sonderwünsche, Sprache, medizinisch relevante Informationen, Notfallkontakte \s
                3.4 Zahlungsdaten: Bankverbindung, Kreditkartendaten (via Payment Provider), Rechnungsadresse \s
                3.5 Kommunikationsdaten: E-Mail-Korrespondenz, Supportanfragen, Chatverläufe \s
                3.6 Websitedaten: IP-Adresse, Datum/Uhrzeit des Zugriffs, Gerätetyp, Betriebssystem, Browsertyp, besuchte Seiten, Klickverhalten, Referrer-URL, Cookies und Tracking-IDs
                
                4. Zwecke und Rechtsgrundlagen der Verarbeitung \s
                Die Verarbeitung Ihrer Daten erfolgt zur Vertragserfüllung, zur medizinischen Prüfung, zur Zahlungsabwicklung, Kundenbetreuung, für Werbung mit Einwilligung sowie zur Verbesserung unserer Angebote.
                
                5. Empfänger Ihrer Daten \s
                Ihre Daten können an folgende Empfänger weitergegeben werden: Raumfahrtagenturen, Zahlungsdienstleister, IT-Provider und Behörden. Alle Empfänger unterliegen den Bestimmungen von Auftragsverarbeitungsverträgen gemäß Art. 28 DSGVO.
                
                6. Datenübermittlung in Drittländer \s
                Eine Übermittlung in Drittländer erfolgt nur bei Vorliegen eines Angemessenheitsbeschlusses, geeigneter Garantien oder Ihrer ausdrücklichen Einwilligung.
                
                7. Speicherdauer \s
                Die Speicherdauer richtet sich nach der jeweiligen Datenkategorie: \s
                – Buchungsdaten: bis zu 10 Jahre \s
                – Gesundheitsdaten: maximal 12 Monate \s
                – Kommunikationsdaten: bis zu 2 Jahre
                
                8. Cookies und Tracking-Technologien \s
                Tracking-Technologien und Cookies werden nur mit Ihrer ausdrücklichen Einwilligung verwendet. Beim ersten Besuch unserer Website informiert ein Cookie-Banner über die eingesetzten Tools (z. B. Google Analytics, Facebook Pixel).
                
                9. Ihre Datenschutzrechte \s
                Sie haben das Recht auf Auskunft, Berichtigung, Löschung, Einschränkung der Verarbeitung, Datenübertragbarkeit, Widerspruch und Widerruf Ihrer Einwilligung. Bitte wenden Sie sich dazu an unseren Datenschutzbeauftragten.
                
                10. Beschwerderecht bei der Aufsichtsbehörde \s
                Sie können sich jederzeit an den Landesdatenschutzbeauftragten Ihres Bundeslandes wenden, wenn Sie der Meinung sind, dass Ihre Rechte verletzt wurden.
                
                11. Datensicherheit \s
                Wir setzen moderne technische und organisatorische Maßnahmen ein: SSL-Verschlüsselung, Zugriffsbeschränkungen, regelmäßige Backups und Zwei-Faktor-Authentifizierung.
                
                12. Änderungen dieser Datenschutzerklärung \s
                Diese Datenschutzerklärung kann bei Änderungen unserer Dienstleistungen oder der Gesetzeslage angepasst werden. Die aktuelle Fassung finden Sie jederzeit unter: \s
                www.nexstar-travel.com/datenschutz
                """);
    }

    /**
     * Handles navigation to the next page when the continue button is clicked.
     * Only proceeds if the user has accepted the data privacy agreement.
     *
     * @param e The mouse event that triggered this method
     */
    @FXML
    @SuppressWarnings("unused")
    public void nextPage(MouseEvent e) {
        if (!acceptButton.isSelected()) {
            showError("Fehler", "Datenschutzerklärung nicht akzeptiert", "Bitte akzeptieren Sie die Datenschutzerklärung um fortzufahren.");
            return;
        }

        loadScene(e, "customer_registration/page_3_personal_information.fxml", "Persönliche Informationen");
    }

    /**
     * Handles navigation to the previous page.
     *
     * @param e The mouse event that triggered this method
     */
    @FXML
    @SuppressWarnings("unused")
    public void previousPage(MouseEvent e) {
        loadScene(e, "customer_registration/page_1_start_payment_process.fxml", "Start");
    }

    /**
     * Updates the state of the continue button based on whether the user has accepted the agreement.
     * The continue button is enabled only when the agreement is accepted.
     *
     * @param e The mouse event that triggered this method
     */
    @FXML
    @SuppressWarnings("unused")
    public void handleAcceptance(MouseEvent e) {
        continueButton.setDisable(!acceptButton.isSelected());
    }
}