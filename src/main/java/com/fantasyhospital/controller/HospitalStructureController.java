package com.fantasyhospital.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class HospitalStructureController implements Initializable {

    @FXML
    private Pane hospitalStructure;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Création du Label
        Label constructionLabel = new Label("Hôpital en cours de construction");
        constructionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Centrage du Label
        constructionLabel.layoutXProperty().bind(
                hospitalStructure.widthProperty().subtract(constructionLabel.widthProperty()).divide(2));
        constructionLabel.layoutYProperty().bind(
                hospitalStructure.heightProperty().subtract(constructionLabel.heightProperty()).divide(2));

        // Ajout du Label au Pane
        hospitalStructure.getChildren().add(constructionLabel);
    }
}