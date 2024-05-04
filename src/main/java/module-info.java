module com.example.shapeslider {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.shapeslider to javafx.fxml;
    exports com.example.shapeslider;
}