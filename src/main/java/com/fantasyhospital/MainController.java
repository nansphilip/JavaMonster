package com.fantasyhospital;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainController {
    @FXML private Button tab1;
    @FXML private Button tab2;
    @FXML private VBox content;

    @FXML
    public void initialize() {
        tab1.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        tab2.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        tab1.setOnAction(e -> {
            content.setStyle("-fx-background-color: #2196F3; -fx-background-radius: 10px; -fx-padding: 40px; margin: 20px;");
            tab1.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
            tab2.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        tab2.setOnAction(e -> {
            content.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 10px; -fx-padding: 40px; margin: 20px;");
            tab2.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            tab1.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        });
    }
} 