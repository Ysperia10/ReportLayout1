module com.example.report_layout {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.report_layout to javafx.fxml;
    exports com.example.report_layout;
    exports com.example.report_layout.model;
    opens com.example.report_layout.model to javafx.fxml;
    exports com.example.report_layout.view;
    opens com.example.report_layout.view to javafx.fxml;
}