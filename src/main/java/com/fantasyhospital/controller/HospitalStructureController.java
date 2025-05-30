package com.fantasyhospital.controller;

import com.fantasyhospital.model.Hospital;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class HospitalStructureController implements Initializable {

    @FXML
    private Pane hospitalStructure;
    private Label welcomeLabel;
    private Pane waitingRoomView;
    private WaitingRoomController waitingRoomController;
    private boolean gameStarted = false;
    private Hospital hospital;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Création du Label de bienvenue
        welcomeLabel = new Label("Bienvenue à l'Hôpital Fantastique !");
        welcomeLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        // Centrage du Label
        welcomeLabel.layoutXProperty().bind(
                hospitalStructure.widthProperty().subtract(welcomeLabel.widthProperty()).divide(2));
        welcomeLabel.layoutYProperty().bind(
                hospitalStructure.heightProperty().subtract(welcomeLabel.heightProperty()).divide(2));

        // Ajout du Label au Pane
        hospitalStructure.getChildren().add(welcomeLabel);

        // Préchargement de la vue de la salle d'attente pour l'utiliser plus tard
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/waitingRoomView.fxml"));
            waitingRoomView = loader.load();
            waitingRoomController = loader.getController();

            // La salle d'attente n'est pas ajoutée au départ
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hideWelcomeMessage() {
        if (welcomeLabel != null && hospitalStructure.getChildren().contains(welcomeLabel)) {
            hospitalStructure.getChildren().remove(welcomeLabel);
        }
    }

    public void startGame() {
        // Supprimer le message de bienvenue
        hideWelcomeMessage();

        // Afficher la salle d'attente
        if (waitingRoomView != null && !hospitalStructure.getChildren().contains(waitingRoomView)) {
            hospitalStructure.getChildren().add(waitingRoomView);
        }

        gameStarted = true;
    }

    public WaitingRoomController getWaitingRoomController() {
        return waitingRoomController;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Transmet l'objet hôpital au contrôleur de la salle d'attente
     * pour afficher les créatures dans la salle d'attente
     *
     * @param hospital L'hôpital contenant les données des créatures
     */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
        if (waitingRoomController != null) {
            waitingRoomController.setHospital(hospital);
        }
    }

    /**
     * Met à jour l'affichage de la salle d'attente avec les données actuelles
     * Doit être appelée après chaque tour de jeu
     */
    public void updateWaitingRoom() {
        if (waitingRoomController != null && hospital != null) {
            waitingRoomController.updateWaitingRoom();
        }
    }
}