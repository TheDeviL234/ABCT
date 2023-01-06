module com.example.abct {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.abct to javafx.fxml;
    exports com.example.abct;
}