package com.atdit.booking.frontend.customer_registration.controllers;

import com.atdit.booking.backend.Resources;
import com.atdit.booking.backend.exceptions.EvaluationException;
import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;
import com.atdit.booking.backend.financialdata.processing.FinancialInformationEvaluator;
import com.atdit.booking.backend.financialdata.processing.FinancialInformationParser;
import com.atdit.booking.frontend.abstract_controller.Cacheable;
import com.atdit.booking.frontend.abstract_controller.Controller;
import com.atdit.booking.frontend.abstract_controller.Navigatable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

/**
 * Controller class for managing financial information proof uploads in a booking system.
 * This class handles the UI interactions for uploading and validating financial documents
 * like income proof, liquid assets proof and SCHUFA information.
 */
public class Page5ProofFIController extends Controller implements Initializable, Navigatable, Cacheable {

    /**
     * JavaFX UI elements injected from FXML
     * These elements are used to interact with the user interface for financial proof uploads
     */
    @FXML private Button incomeProofButton;
    @FXML private Button liquidAssetsProofButton;
    @FXML private Button schufaButton;
    @FXML private Button continueButton;
    @FXML private Label incomeStatusLabel;
    @FXML private Label liquidAssetsStatusLabel;
    @FXML private Label schufaStatusLabel;

    /**
     * Holds the customer's financial information
     */
    private FinancialInformation financialInfo;
    /**
     * Evaluator for financial information and documents
     */
    private FinancialInformationEvaluator evaluator;
    /**
     * Parser for financial document contents
     */
    private FinancialInformationParser parser;
    /**
     * File chooser for selecting documents from the user's system
     */
    private  FileChooser fileChooser;

    /**
     * Initializes the controller. Sets up file chooser, restores data and configures UI elements.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        financialInfo = Resources.financialInformation;
        evaluator = Resources.financialInformationEvaluator;

        parser = new FinancialInformationParser();
        fileChooser = new FileChooser();


        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        restoreData();

        setupUploadButton(incomeProofButton, "income", incomeStatusLabel);
        setupUploadButton(liquidAssetsProofButton, "liquidAssets", liquidAssetsStatusLabel);
        setupUploadButton(schufaButton, "schufa", schufaStatusLabel);

        setupStatusLabel(incomeStatusLabel, "income");
        setupStatusLabel(liquidAssetsStatusLabel, "liquidAssets");
        setupStatusLabel(schufaStatusLabel, "schufa");

    }

    /**
     * Handles navigation to the next page after validating and evaluating uploads.
     *
     * @param e MouseEvent that triggered the navigation
     */
    @FXML
    
    public void nextPage(MouseEvent e) {
        try {
            evaluator.validateUploads();
            evaluator.evaluateUploads();
        }
        catch (ValidationException ex) {
            showError("Validierung fehlgeschlagen", "Die Validierung Ihrer Dokumente ist fehlgeschlagen.", ex.getMessage());
            return;
        }
        catch (EvaluationException ex) {
            showError("Evaluierung fehlgeschlagen", "Die Evaluierung Ihrer Dokumente ist fehlgeschlagen.", ex.getMessage());
            return;
        }

        loadScene(e, "customer_registration/page_6_create_account.fxml", "Account erstellen");
    }

    /**
     * Handles navigation to the previous page.
     *
     * @param e MouseEvent that triggered the navigation
     */
    @FXML
    
    public void previousPage(MouseEvent e) {
        loadScene(e, "customer_registration/page_4_financial_info_declaration.fxml", "Finanzielle Angaben");
    }

    /**
     * Sets up a status label with click handling to remove uploaded documents.
     *
     * @param label The status label to configure
     * @param documentType The type of document ("income", "liquidAssets", or "schufa")
     */
    
    private void setupStatusLabel(Label label, String documentType) {

        label.setOnMouseClicked(e -> {
                    switch (documentType) {
                        case "income" -> financialInfo.setProofOfIncome(null);
                        case "liquidAssets" -> financialInfo.setProofOfLiquidAssets(null);
                        case "schufa" -> financialInfo.setSchufa(null);
                    }

                    label.setText("Nicht hochgeladen");
                    label.setStyle("-fx-text-fill: #f9e79f;");
                }
        );
    }

    /**
     * Configures an upload button with document handling functionality.
     *
     * @param button The button to configure
     * @param documentType The type of document to upload
     * @param statusLabel The label showing the upload status
     */
    
    private void setupUploadButton(Button button, String documentType, Label statusLabel) {

        button.setOnAction(e -> {
            try{
                uploadDocument(documentType, statusLabel, getDocumentContent());
            } catch (IOException ex) {
                showError("Fehler beim hochladen", "Beim hochladen Ihrer Dokumente ist ein Fehler aufgetreten.", ex.getMessage());
                statusLabel.setText("Fehler im Dateiinhalt (klicken zum entfernen)");
                statusLabel.setStyle("-fx-text-fill: red; -fx-cursor: hand;");
            }
        });
    }

    /**
     * Opens a file chooser dialog and reads the selected file's content.
     *
     * @return The content of the selected file as String, or null if no file was selected
     * @throws IOException If there's an error reading the file
     */
    private String getDocumentContent() throws IOException{

        File file = fileChooser.showOpenDialog(continueButton.getScene().getWindow());
        if (file != null) {
            return Files.readString(file.toPath());
        }
        return null;
    }

    /**
     * Processes and validates an uploaded document.
     *
     * @param documentType The type of document being uploaded
     * @param statusLabel The label to show the upload status
     * @param content The content of the uploaded document
     */
    private void uploadDocument(String documentType, Label statusLabel, String content){

        try {
            evaluator.validateDocumentFormat(content, documentType);
            evaluator.validateDocumentDate(content);
        }
        catch(ValidationException | EvaluationException ex) {
            statusLabel.setText(ex.getMessage() + " (klicken zum Entfernen");
            statusLabel.setStyle("-fx-text-fill: red; -fx-cursor: hand;");
            return;
        }

        statusLabel.setText("Valides Dokument (klicken zum Entfernen)");
        statusLabel.setStyle("-fx-text-fill: green; -fx-cursor: hand;");

        switch (documentType) {
            case "income" -> financialInfo.setProofOfIncome(parser.parseIncomeDocument(content));
            case "liquidAssets" -> financialInfo.setProofOfLiquidAssets(parser.parseLiquidAssetsDocument(content));
            case "schufa" -> financialInfo.setSchufa(parser.parseSchufaDocument(content));
        }


    }

    /**
     * Restores previously uploaded document data and updates UI accordingly.
     */
    public void restoreData() {

        if (financialInfo.getProofOfIncome() != null) {
            incomeStatusLabel.setText("Valides Dokument (klicken zu entfernen)");
            incomeStatusLabel.setStyle("-fx-text-fill: green; -fx-cursor: hand;");
        }

        if (financialInfo.getProofOfLiquidAssets() != null) {
            liquidAssetsStatusLabel.setText("Valides Dokument (klicken zu entfernen)");
            liquidAssetsStatusLabel.setStyle("-fx-text-fill: green; -fx-cursor: hand;");
        }

        if (financialInfo.getSchufa() != null) {
            schufaStatusLabel.setText("Valides Dokument (klicken zu entfernen)");
            schufaStatusLabel.setStyle("-fx-text-fill: green; -fx-cursor: hand;");
        }
    }

    /**
     * Implementation of Cacheable interface method.
     * No caching logic needed for this controller.
     */
    @Override
    public void cacheData() {
        // caching logic not needed
    }

}