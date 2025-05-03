package com.atdit.booking.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;
import com.atdit.booking.financialdata.FinancialInformation;
import com.atdit.booking.financialdata.FinancialInformationEvaluator;
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
import java.util.Map;
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
    private static final FinancialInformationEvaluator evaluator = new FinancialInformationEvaluator();
    private final Map<String, String> uploadedDocuments = new HashMap<>();
    private final FileChooser fileChooser = new FileChooser();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        setupUploadButton(incomeProofButton, "income", incomeStatusLabel);
        setupUploadButton(liquidAssetsProofButton, "liquidAssets", liquidAssetsStatusLabel);
        setupUploadButton(schufaButton, "schufa", schufaStatusLabel);

        setupStatusLabel(incomeStatusLabel, "income");
        setupStatusLabel(liquidAssetsStatusLabel, "liquidAssets");
        setupStatusLabel(schufaStatusLabel, "schufa");
    }

    @FXML
    public void nextPage(MouseEvent e) {

        if (validateUploads()) {
            saveDocuments();
            Stage stage = (Stage) continueButton.getScene().getWindow();
            Scene scene = getScene("page_6.fxml");
            stage.setScene(scene);
        }
    }

    @FXML
    public void previousPage(MouseEvent e) {

        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = getScene("page_4.fxml");
        stage.setScene(scene);
    }


    private void setupStatusLabel(Label label, String documentType) {

        label.setOnMouseClicked(e -> {
            if (uploadedDocuments.containsKey(documentType)) {
                uploadedDocuments.remove(documentType);
                label.setText("Not uploaded");
                label.setStyle("-fx-text-fill: black;");
            }
        });
    }

    private void setupUploadButton(Button button, String documentType, Label statusLabel) {

        button.setOnAction(e -> uploadDocument(documentType, statusLabel));
    }


    private void uploadDocument(String documentType, Label statusLabel) {

        File file = fileChooser.showOpenDialog((Stage) continueButton.getScene().getWindow());

        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                uploadedDocuments.put(documentType, content);

                if (!validateDocumentFormat(content, documentType)) {
                    statusLabel.setText("Invalid format (click to remove)");
                    statusLabel.setStyle("-fx-text-fill: red;");
                } else {
                    statusLabel.setText(file.getName() + " (click to remove)");
                    statusLabel.setStyle("-fx-text-fill: green; -fx-cursor: hand;");
                }

            } catch (IOException ex) {
                showError("File Error", "Could not read file", ex.getMessage());
            }
        }
    }


    private boolean validateUploads() {

        if (uploadedDocuments.containsKey("liquidAssets") && uploadedDocuments.containsKey("schufa")) {
            return true;
        }

        showError("Missing Documents", "Please upload required documents", "You need to upload proof of liquid assets and Schufa information.");
        return false;
    }

    private void saveDocuments() {
        try {
            String customerDir = "financial_customer_documents/" + Main.customer.hashCode();
            Files.createDirectories(new File(customerDir).toPath());
            for (Map.Entry<String, String> entry : uploadedDocuments.entrySet()) {
                Files.writeString(new File(customerDir + "/" + entry.getKey() + ".txt").toPath(),
                        entry.getValue());
            }
        } catch (IOException ex) {
            showError("Save Error", "Could not save documents", ex.getMessage());
        }
    }

    private boolean validateDocumentFormat(String content, String documentType) {
        String[] lines = content.split("\n");
        boolean hasRequiredFields = false;

        switch(documentType) {

            case "income":
                hasRequiredFields = lines.length == 4
                                    && content.contains("Monthly Net Income:")
                                    && content.contains("Employment Type:")
                                    && content.contains("Employer:")
                                    && content.contains("Employment Duration:");
                break;

            case "liquidAssets":
                hasRequiredFields = lines.length == 4
                                    && content.contains("Bank Account Balance:")
                                    && content.contains("IBAN:")
                                    && content.contains("Date Issued:")
                                    && content.contains("Description:");
                break;

            case "schufa":
                hasRequiredFields = lines.length == 7
                                    && content.contains("Schufa Score:")
                                    && content.contains("Total Credits:")
                                    && content.contains("Total Credit Sum:")
                                    && content.contains("Total Amount Payed:")
                                    && content.contains("Total Amount Owed:")
                                    && content.contains("Total Monthly Rate:")
                                    && content.contains("Date Issued:");

                break;
        }

        return hasRequiredFields;
    }

    public void cacheData() {

        evaluator.parseIncomeDocument(uploadedDocuments.get("income"));
        evaluator.parseLiquidAssetsDocument(uploadedDocuments.get("liquidAssets"));
        evaluator.parseSchufaDocument(uploadedDocuments.get("schufa"));



    }
}