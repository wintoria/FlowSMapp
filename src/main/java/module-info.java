module com.wiktoria.flow.flowsmapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.wiktoria.flow.flowsmapp to javafx.fxml;
    exports com.wiktoria.flow.flowsmapp;
}