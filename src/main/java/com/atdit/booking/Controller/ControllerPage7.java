package com.atdit.booking.Controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class ControllerPage7 extends Controller {
    @FXML
    private RadioButton oneTimePaymentRadio;
    @FXML
    private RadioButton financingRadio;
    @FXML
    private ToggleGroup paymentMethodGroup;

    @FXML
    private void handleContinue() {
        Stage stage = (Stage) oneTimePaymentRadio.getScene().getWindow();
        Scene scene;

        if (oneTimePaymentRadio.isSelected()) {
            //scene = loadScene("page_8a.fxml");
            stage.setTitle("One-Time Payment ");
        } else {
            //scene = loadScene("page_8b.fxml");
            stage.setTitle("Financing");
        }
        //stage.setScene(scene);
    }
}