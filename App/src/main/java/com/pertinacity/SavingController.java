package com.pertinacity;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class SavingController {
  
  Button button = new Button();
  Text text = new Text();

  @FXML
  protected void fixEverything(){
    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
    Scene scene;
    
    try {
        scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage primaryStage = (Stage) text.getScene().getWindow();
        primaryStage.setScene(scene);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  }

}
