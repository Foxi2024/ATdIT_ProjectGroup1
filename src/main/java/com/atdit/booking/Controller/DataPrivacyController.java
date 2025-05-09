package com.atdit.booking.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;


public class DataPrivacyController extends Controller {

    @FXML private RadioButton acceptButton;
    @FXML private Button continueButton;


    @FXML
    public void nextPage(MouseEvent e){

        if(acceptButton.isSelected()){

            loadScene(e,"page_3.fxml", "Personal Information");
        }
    }

    public void previousPage(MouseEvent e){

        loadScene(e,"page_1.fxml", "Welcome");
    }

    @FXML
    public void acceptAgreement(MouseEvent e) {

        if(acceptButton.isSelected()){
            continueButton.setDisable(false);
        } else {
            continueButton.setDisable(true);
        }
    }

}
