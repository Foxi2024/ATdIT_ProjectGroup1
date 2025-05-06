package com.atdit.booking.Controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerPage7 extends Controller {
    @FXML
    private RadioButton oneTimePaymentRadio;
    @FXML
    private RadioButton financingRadio;
    @FXML
    private ToggleGroup paymentMethodGroup;

    @FXML
    private void handleContinue(MouseEvent e) {

        if (oneTimePaymentRadio.isSelected()) {
            loadScene(e, "page_8a.fxml", "One-Time Payment");
        } else {
            loadScene(e, "page_8b.fxml", "Financing");
        }

        //stage.setScene(scene);
    }

    @FXML
    public void previousPage(MouseEvent e) {
        loadScene(e, "page_6.fxml", "Page 6");
    }
}