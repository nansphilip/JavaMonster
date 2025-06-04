package com.fantasyhospital.controller;

import com.fantasyhospital.model.Hospital;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

@Component
public class HospitalStructureController implements Initializable {

    @FXML
    private Pane hospitalStructure;
    private StackPane welcomeContainer;
    private Pane waitingRoomView;
    private WaitingRoomController waitingRoomController;
    private boolean gameStarted = false;
    private Hospital hospital;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Création d'un conteneur pour l'écran de bienvenue
        welcomeContainer = new StackPane();

        // Ajout de l'image en arrière-plan
        try {
            ImageView backgroundLogo = new ImageView(new Image(getClass().getResourceAsStream("/images/logo/Logo.jpg")));
            backgroundLogo.setPreserveRatio(false); // Permettre à l'image de s'adapter parfaitement

            // Lier les dimensions exactes de l'image à celles du conteneur hospitalStructure
            backgroundLogo.fitWidthProperty().bind(hospitalStructure.widthProperty());
            backgroundLogo.fitHeightProperty().bind(hospitalStructure.heightProperty());

            welcomeContainer.getChildren().add(backgroundLogo);
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image de logo: " + e.getMessage());
        }

        // Création du Label de bienvenue par-dessus l'image
        Label welcomeText = new Label("Bienvenue à l'Hôpital Fantastique !");
        welcomeText.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: white; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        welcomeContainer.getChildren().add(welcomeText);

        // Faire correspondre exactement le conteneur welcome avec hospitalStructure
        welcomeContainer.prefWidthProperty().bind(hospitalStructure.widthProperty().multiply(0.3));
        welcomeContainer.prefHeightProperty().bind(hospitalStructure.heightProperty());

        // Placer le conteneur exactement aux mêmes coordonnées que l'hospitalStructure
        welcomeContainer.setLayoutX(0);
        welcomeContainer.setLayoutY(0);

        // Ajout du conteneur au Pane
        hospitalStructure.getChildren().add(welcomeContainer);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/waitingRoomView.fxml"));
            waitingRoomView = loader.load();
            waitingRoomController = loader.getController();

            // Rendre la salle d'attente responsive
            waitingRoomView.prefWidthProperty().bind(hospitalStructure.widthProperty().multiply(0.3)); // 30% de la largeur
            waitingRoomView.prefHeightProperty().bind(hospitalStructure.heightProperty().multiply(0.9)); // 90% de la hauteur
            waitingRoomView.layoutXProperty().bind(hospitalStructure.widthProperty().multiply(0.01)); // 1% de marge à gauche
            waitingRoomView.layoutYProperty().bind(hospitalStructure.heightProperty().multiply(0.05)); // 5% de marge en haut

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hideWelcomeMessage() {
        if (welcomeContainer != null && hospitalStructure.getChildren().contains(welcomeContainer)) {
            hospitalStructure.getChildren().remove(welcomeContainer);
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

