package com.atdit.booking.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ProcessStepBarController extends Controller implements Initializable {

    @FXML private HBox processStepBar;
    @FXML private Label paymentSelectionLabel;
    @FXML private Label paymentMethodLabel;
    @FXML private Label contractLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCurrentStep("payment_selection");
    }


    public void setCurrentStep(String step) {
        String defaultStyle = "-fx-min-width: 150; -fx-alignment: center; -fx-font-size: 14; -fx-padding: 10 20; -fx-border-color: #dee2e6; -fx-border-radius: 20; -fx-background-radius: 20; -fx-background-color: transparent;";
        String activeStyle = "-fx-min-width: 150; -fx-alignment: center; -fx-font-size: 14; -fx-padding: 10 20; -fx-border-color: #4CAF50; -fx-border-radius: 20; -fx-background-radius: 20; -fx-background-color: #e8f5e9; -fx-text-fill: #2E7D32;";

        paymentSelectionLabel.setStyle(defaultStyle);
        paymentMethodLabel.setStyle(defaultStyle);
        contractLabel.setStyle(defaultStyle);

        switch (step) {
            case "payment_selection" -> paymentSelectionLabel.setStyle(activeStyle);
            case "payment_method" -> paymentMethodLabel.setStyle(activeStyle);
            case "contract" -> contractLabel.setStyle(activeStyle);
        }
    }

}