package com.atdit.booking.frontend.Controller;


import com.atdit.booking.Main;
import com.atdit.booking.backend.customer.Customer;
import com.atdit.booking.backend.exceptions.EvaluationException;
import com.atdit.booking.backend.exceptions.ValidationException;
import com.atdit.booking.backend.financialdata.financial_information.FinancialInformation;
import com.atdit.booking.backend.financialdata.processing.FinancialInformationEvaluator;
import com.atdit.booking.backend.financialdata.processing.FinancialInformationParser;
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

public class Page5ProofFIController extends Controller implements Initializable, Navigatable, Cacheable {

    @FXML private Button incomeProofButton;
    @FXML private Button liquidAssetsProofButton;
    @FXML private Button schufaButton;
    @FXML private Button continueButton;
    @FXML private Button backButton;
    @FXML private Label incomeStatusLabel;
    @FXML private Label liquidAssetsStatusLabel;
    @FXML private Label schufaStatusLabel;




    private static final Customer currentCustomer = Main.customer;
    private static final FinancialInformation financialInfo = currentCustomer.getFinancialInformation();
    private static final FinancialInformationEvaluator evaluator = new FinancialInformationEvaluator(financialInfo);
    private static final FinancialInformationParser parser = new FinancialInformationParser();
    private final FileChooser fileChooser = new FileChooser();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //processStepBarController.setCurrentStep("proof");
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
            showError("Evaluierung fehlgeschlagen", "Die Validierung Ihrer Dokumente ist fehlgeschlagen.", ex.getMessage());
            return;
        }

        loadScene(e, "page_6.fxml", "Account erstellen");
    }


    @FXML
    public void previousPage(MouseEvent e) {

        loadScene(e, "page_4.fxml", "Nachweis finanzieller Angaben");
    }


    private void setupStatusLabel(Label label, String documentType) {

        label.setOnMouseClicked(e -> {
                    switch (documentType) {
                        case "income" -> financialInfo.setProofOfIncome(null);
                        case "liquidAssets" -> financialInfo.setProofOfLiquidAssets(null);
                        case "schufa" -> financialInfo.setSchufa(null);
                    }

                    label.setText("Nicht hochgeladen");
                    label.setStyle("-fx-text-fill: black;");
                }
        );
    }

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


    private String getDocumentContent() throws IOException{

        File file = fileChooser.showOpenDialog(continueButton.getScene().getWindow());
        if (file != null) {
            return Files.readString(file.toPath());
        }
        return null;
    }

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

    @Override
    public void cacheData() {
        // caching logic not needed
    }


}