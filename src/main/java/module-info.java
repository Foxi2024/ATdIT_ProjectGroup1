module com.atdit.booking {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;


    opens com.atdit.booking to javafx.fxml;
    exports com.atdit.booking;
    exports com.atdit.booking.customer;
    opens com.atdit.booking.customer to javafx.fxml;
}