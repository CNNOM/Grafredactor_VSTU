module com.example.grafredactor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens com.example.grafredactor to javafx.fxml;
    exports com.example.grafredactor;
}