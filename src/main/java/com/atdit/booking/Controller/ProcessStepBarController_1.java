package com.atdit.booking.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ProcessStepBarController_1 extends Controller implements Initializable {

    @FXML private HBox processStepBar1;
    @FXML private Label step1Label;
    @FXML private Label step2Label;
    @FXML private Label step3Label;
    @FXML private Label step4Label;
    @FXML private Label step5Label;

    private final String defaultStyle = "-fx-min-width: 150; -fx-alignment: center; -fx-font-size: 14; " +
            "-fx-padding: 10 20; -fx-border-color: #dee2e6; -fx-border-radius: 20; " +
            "-fx-background-radius: 20; -fx-background-color: transparent;";

    private final String activeStyle = "-fx-min-width: 150; -fx-alignment: center; -fx-font-size: 14; " +
            "-fx-padding: 10 20; -fx-border-color: #4CAF50; -fx-border-radius: 20; " +
            "-fx-background-radius: 20; -fx-background-color: #e8f5e9; -fx-text-fill: #2E7D32;";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCurrentStep("privacy");
    }

    public void setCurrentStep(String step) {

        step1Label.setStyle(defaultStyle);
        step2Label.setStyle(defaultStyle);
        step3Label.setStyle(defaultStyle);
        step4Label.setStyle(defaultStyle);
        step5Label.setStyle(defaultStyle);

        switch (step) {
            case "privacy" -> step1Label.setStyle(activeStyle);
            case "personal" -> step2Label.setStyle(activeStyle);
            case "financial" -> step3Label.setStyle(activeStyle);
            case "proof" -> step4Label.setStyle(activeStyle);
            case "account" -> step5Label.setStyle(activeStyle);
        }
    }

    private void resetAllStyles() {
        step1Label.setStyle(defaultStyle);
        step2Label.setStyle(defaultStyle);
        step3Label.setStyle(defaultStyle);
        step4Label.setStyle(defaultStyle);
        step5Label.setStyle(defaultStyle);
    }
}