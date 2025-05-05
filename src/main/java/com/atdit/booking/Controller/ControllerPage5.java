package com.atdit.booking.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;
import com.atdit.booking.customer.CustomerDatabase;
import com.atdit.booking.financialdata.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ControllerPage5 extends Controller implements Initializable {

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
    private static final CustomerDatabase db = new CustomerDatabase(currentCustomer);
    private final FileChooser fileChooser = new FileChooser();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        restoreFormData();

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
        } catch (IllegalArgumentException ex) {
            showError("Validation Error", "Validation Error in your declarations", ex.getMessage());
            return;
        }

        try {
            evaluator.evaluateUploads();
        } catch (IllegalArgumentException ex) {
            showError("Evaluation Error", "Evaluation Error in your declarations", ex.getMessage());
            return;
        }

        //db.saveCustomerInDatabase();

        loadScene(e, "page_7.fxml", "Placeholder");
    }


    @FXML
    public void previousPage(MouseEvent e) {

        loadScene(e, "page_4.fxml", "Financial Information");
    }


    private void setupStatusLabel(Label label, String documentType) {

        label.setOnMouseClicked(e -> {
                    switch (documentType) {
                        case "income" -> financialInfo.setProofOfIncome(null);
                        case "liquidAssets" -> financialInfo.setProofOfLiquidAssets(null);
                        case "schufa" -> financialInfo.setSchufa(null);
                    }

                    label.setText("Not uploaded");
                    label.setStyle("-fx-text-fill: black;");
                }
        );
    }

    private void setupUploadButton(Button button, String documentType, Label statusLabel) {

        button.setOnAction(e -> uploadDocument(documentType, statusLabel, getDocumentContent()));
    }


    private String getDocumentContent() {

        File file = fileChooser.showOpenDialog((Stage) continueButton.getScene().getWindow());
        if (file != null) {
            try {
                return Files.readString(file.toPath());
            } catch (IOException ex) {
                showError("File Error", "Cannot read file", ex.getMessage());
            }
        }
        return null;
    }

    private void uploadDocument(String documentType, Label statusLabel, String content) {

        try {
            if (!evaluator.validateDocumentFormat(content, documentType)) {
                statusLabel.setText("Invalid format (click to remove)");
                statusLabel.setStyle("-fx-text-fill: red; -fx-cursor: hand;");

            } else if (!evaluator.validateDocumentDate(content)) {

                statusLabel.setText("Document too old (max " + FinancialInformationEvaluator.MAX_DOCUMENT_AGE_DAYS + " days) (click to remove)");
                statusLabel.setStyle("-fx-text-fill: red; -fx-cursor: hand;");

            } else {
                statusLabel.setText("Valid File (click to remove)");
                statusLabel.setStyle("-fx-text-fill: green; -fx-cursor: hand;");

                switch (documentType) {
                    case "income" -> financialInfo.setProofOfIncome(parser.parseIncomeDocument(content));
                    case "liquidAssets" -> financialInfo.setProofOfLiquidAssets(parser.parseLiquidAssetsDocument(content));
                    case "schufa" -> financialInfo.setSchufa(parser.parseSchufaDocument(content));
                }

            }

        } catch (IllegalArgumentException ex) {
            showError("File Error", "Error with file content", ex.getMessage());
            statusLabel.setText("Error with file content (click to remove)");
            statusLabel.setStyle("-fx-text-fill: red; -fx-cursor: hand;");
        }

    }


    private void restoreFormData() {

        if (financialInfo.getProofOfIncome() != null) {
            incomeStatusLabel.setText("Valid File (click to remove)");
            incomeStatusLabel.setStyle("-fx-text-fill: green; -fx-cursor: hand;");
        }

        if (financialInfo.getProofOfLiquidAssets() != null) {
            liquidAssetsStatusLabel.setText("Valid File (click to remove");
            liquidAssetsStatusLabel.setStyle("-fx-text-fill: green; -fx-cursor: hand;");
        }

        if (financialInfo.getSchufa() != null) {
            schufaStatusLabel.setText("Valid File (click to remove)");
            schufaStatusLabel.setStyle("-fx-text-fill: green; -fx-cursor: hand;");
        }
    }


}