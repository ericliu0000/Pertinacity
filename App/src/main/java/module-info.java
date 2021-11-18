module com.pertinacity.pertinacity {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    

    requires org.controlsfx.controls;

    opens com.pertinacity.pertinacity to javafx.fxml;
    exports com.pertinacity.pertinacity;
}