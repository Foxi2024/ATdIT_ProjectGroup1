module com.atdit.booking {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.atdit.booking to javafx.fxml;
    exports com.atdit.booking;
}