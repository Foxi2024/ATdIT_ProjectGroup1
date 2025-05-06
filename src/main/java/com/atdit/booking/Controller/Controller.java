package com.atdit.booking.Controller;

import com.atdit.booking.Excpetions.PageLoadingError;
import com.atdit.booking.Main;
import com.atdit.booking.customer.Customer;
import com.atdit.booking.excpetions.PageLoadException;
import com.atdit.booking.financialdata.FinancialInformation;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;



public abstract class Controller {

    public void loadScene(Event event, String fxml, String title) {

        try {
            Stage stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(Controller.class.getResource("/com/atdit/booking/" + fxml));
            Scene scene = new Scene(loader.load());
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            PageLoadingError.showPageLoadingError(e.getMessage());
        }
    }

    public void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showPageLoadingError(String errorMessage) {
        showError("Error", "Error loading page", "Could not load the next page. Try again or close the application. \n" + errorMessage);
    }

}
