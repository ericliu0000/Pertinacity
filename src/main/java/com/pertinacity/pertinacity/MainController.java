package com.pertinacity.pertinacity;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label welcomeText;

    private int counter = 0;

    @FXML
    protected void onHelloButtonClick() {
        counter++;
        welcomeText.setText("Welcome to JavaFX Application!" + counter);
    }
}