module com.group_2.booking {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.group_2.booking to javafx.fxml;
    exports com.group_2.booking;
}