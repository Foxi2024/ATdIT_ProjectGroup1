package com.atdit.booking.Controller;

import com.atdit.booking.Navigatable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class Page2DataPrivacyController extends Controller implements Initializable, Navigatable {

    @FXML private RadioButton acceptButton;
    @FXML private Button continueButton;
    @FXML private ProcessStepBarController1 processStepBarController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //processStepBarController.setCurrentStep("privacy");
    }

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
