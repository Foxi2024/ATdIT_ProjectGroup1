module com.atdit.booking {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires java.desktop;

    opens com.atdit.booking to javafx.fxml;
    exports com.atdit.booking;
    exports com.atdit.booking.backend.customer;
    opens com.atdit.booking.backend.customer to javafx.fxml;
    exports com.atdit.booking.frontend.super_controller;
    opens com.atdit.booking.frontend.super_controller to javafx.fxml;
    exports com.atdit.booking.backend.exceptions;
    opens com.atdit.booking.backend.exceptions to javafx.fxml;
    exports com.atdit.booking.backend.database;
    opens com.atdit.booking.backend.database to javafx.fxml;
    exports com.atdit.booking.backend.financialdata.contracts;
    opens com.atdit.booking.backend.financialdata.contracts to javafx.fxml;
    exports com.atdit.booking.frontend.customer_registration;
    opens com.atdit.booking.frontend.customer_registration to javafx.fxml;
    exports com.atdit.booking.frontend.payment_process;
    opens com.atdit.booking.frontend.payment_process to javafx.fxml;
    exports com.atdit.booking.frontend.payment_process.abstract_controllers;
    opens com.atdit.booking.frontend.payment_process.abstract_controllers to javafx.fxml;
    exports com.atdit.booking.frontend.payment_process.controllers;
    opens com.atdit.booking.frontend.payment_process.controllers to javafx.fxml;
    exports com.atdit.booking.frontend.customer_registration.controllers;
    opens com.atdit.booking.frontend.customer_registration.controllers to javafx.fxml;
}