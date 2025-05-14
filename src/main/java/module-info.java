module com.atdit.booking {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires java.desktop;

    opens com.atdit.booking to javafx.fxml;
    exports com.atdit.booking;

    // Backend
    exports com.atdit.booking.backend.customer;
    opens com.atdit.booking.backend.customer to javafx.fxml;

    exports com.atdit.booking.backend.exceptions;
    opens com.atdit.booking.backend.exceptions to javafx.fxml;

    exports com.atdit.booking.backend.database;
    opens com.atdit.booking.backend.database to javafx.fxml;

    exports com.atdit.booking.backend.financialdata.contracts;
    opens com.atdit.booking.backend.financialdata.contracts to javafx.fxml;

    // Frontend
    exports com.atdit.booking.frontend.super_controller;
    opens com.atdit.booking.frontend.super_controller to javafx.fxml;

    exports com.atdit.booking.frontend.payment_process.abstract_controllers;
    opens com.atdit.booking.frontend.payment_process.abstract_controllers to javafx.fxml;

    exports com.atdit.booking.frontend.payment_process.controllers;
    opens com.atdit.booking.frontend.payment_process.controllers to javafx.fxml;

    exports com.atdit.booking.frontend.customer_registration.controllers;
    opens com.atdit.booking.frontend.customer_registration.controllers to javafx.fxml;

    exports com.atdit.booking.applications;
    opens com.atdit.booking.applications to javafx.fxml;
}