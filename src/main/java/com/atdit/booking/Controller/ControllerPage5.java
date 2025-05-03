package com.atdit.booking.Controller;

import com.atdit.booking.Main;
import com.atdit.booking.financialdata.FinancialDocumentEvaluator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    @FXML private Button fixedAssetsProofButton;
    @FXML private Button schufaButton;
    @FXML private Button continueButton;
    @FXML private Button backButton;

    @FXML private Label incomeStatusLabel;
    @FXML private Label liquidAssetsStatusLabel;
    @FXML private Label fixedAssetsStatusLabel;
    @FXML private Label schufaStatusLabel;

    private final Map<String, String> uploadedDocuments = new HashMap<>();
    private final FileChooser fileChooser = new FileChooser();
    private final FinancialDocumentEvaluator evaluator = new FinancialDocumentEvaluator();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        setupButtons();
    }


    private void setupButtons() {
        setupUploadButton(incomeProofButton, incomeStatusLabel, "income");
        setupUploadButton(liquidAssetsProofButton, liquidAssetsStatusLabel, "liquidAssets");
        setupUploadButton(fixedAssetsProofButton, fixedAssetsStatusLabel, "fixedAssets");
        setupUploadButton(schufaButton, schufaStatusLabel, "schufa");
    }

    private void setupUploadButton(Button button, Label statusLabel, String documentType) {
        button.setOnAction(e -> uploadDocument(documentType, statusLabel));
    }

    private void uploadDocument(String documentType, Label statusLabel) {
        Stage stage = (Stage) continueButton.getScene().getWindow();
        fileChooser.setTitle("Select " + documentType + " proof document");

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                String content = Files.readString(file.toPath());

                // Evaluate document before accepting it
                if (evaluator.evaluateDocument(documentType, content)) {
                    uploadedDocuments.put(documentType, content);
                    statusLabel.setText("Validated & Uploaded");
                    statusLabel.setStyle("-fx-text-fill: green;");
                } else {
                    showError("Validation Error",
                            "Document format invalid",
                            evaluator.getErrorLog());
                }
            } catch (IOException ex) {
                showError("File Error", "Could not read file", ex.getMessage());
            }
        }
    }

    @FXML
    public void nextPage(MouseEvent e) {
        if (validateUploads()) {
            saveDocuments();
            Stage stage = (Stage) continueButton.getScene().getWindow();
            Scene scene = getScene("page_6.fxml");
            stage.setTitle("Next Step");
            stage.setScene(scene);
        }
    }

    @FXML
    public void previousPage(MouseEvent e) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = getScene("page_4.fxml");
        stage.setTitle("Financial Information");
        stage.setScene(scene);
    }

    private boolean validateUploads() {
        if (uploadedDocuments.size() < 4) {
            showError(
                    "Missing Documents",
                    "Please upload all required documents",
                    "You need to upload proof of income, assets, and Schufa information."
            );
            return false;
        }
        return true;
    }

    private void saveDocuments() {
        try {
            // Create a directory for the customer's documents if it doesn't exist
            String customerDir = "documents/" + Main.customer.getHash();
            Files.createDirectories(new File(customerDir).toPath());

            // Save each document
            for (Map.Entry<String, String> entry : uploadedDocuments.entrySet()) {
                String filePath = customerDir + "/" + entry.getKey() + ".txt";
                Files.writeString(new File(filePath).toPath(), entry.getValue());
            }
        } catch (IOException ex) {
            showError("Save Error", "Could not save documents", ex.getMessage());
        }
    }
}