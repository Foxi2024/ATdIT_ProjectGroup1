module com.atdit.booking {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires java.desktop;

    opens com.atdit.booking to javafx.fxml;
    exports com.atdit.booking;
    exports com.atdit.booking.backend.customer;
    opens com.atdit.booking.backend.customer to javafx.fxml;
    exports com.atdit.booking.frontend.Controller;
    opens com.atdit.booking.frontend.Controller to javafx.fxml;
    exports com.atdit.booking.backend.exceptions;
    opens com.atdit.booking.backend.exceptions to javafx.fxml;
    exports com.atdit.booking.backend.database;
    opens com.atdit.booking.backend.database to javafx.fxml;
    exports com.atdit.booking.backend.financialdata.contracts;
    opens com.atdit.booking.backend.financialdata.contracts to javafx.fxml;
}