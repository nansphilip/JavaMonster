package com.fantasyhospital.controller;

import com.fantasyhospital.model.Hospital;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class HospitalStructureController implements Initializable {

    @FXML
    private Pane hospitalStructure;
    private StackPane welcomeContainer;
    @Setter
    private Pane waitingRoomView;
    @Setter
    @Getter
    private WaitingRoomController waitingRoomController;
    @FXML
    private Pane medicalServiceInclude;

    @FXML
    private Pane cryptViewInclude;
    @FXML
    private CryptViewController cryptViewIncludeController;

    @FXML
    private Pane quarantineViewInclude;
    @FXML
    private QuarantineViewController quarantineViewIncludeController;

    @FXML
    private Pane waitingRoomInclude;

    @FXML
    private WaitingRoomController waitingRoomIncludeController;

    @Getter
    private boolean gameStarted = false;
    @Getter
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

        // Ajuster le positionnement et la taille de la crypte
        if (cryptViewInclude != null) {
            // Position X: à droite avec une marge fixe
            cryptViewInclude.layoutXProperty().bind(
                    hospitalStructure.widthProperty().subtract(cryptViewInclude.prefWidthProperty()).subtract(20)
            );

            // Position Y: simplement une marge depuis le haut
            cryptViewInclude.layoutYProperty().set(20);

            // Limiter la taille de la crypte
            cryptViewInclude.prefWidthProperty().bind(hospitalStructure.widthProperty().multiply(0.18)); // Réduire à 18%
            cryptViewInclude.setPrefHeight(250);
        }

        if (quarantineViewInclude != null && cryptViewInclude != null) {
            // Position X : même que la crypte
            quarantineViewInclude.layoutXProperty().bind(
                    cryptViewInclude.layoutXProperty()
            );

            // Position Y : juste en dessous de la crypte avec un petit espacement
            quarantineViewInclude.layoutYProperty().bind(
                    cryptViewInclude.layoutYProperty().add(cryptViewInclude.prefHeightProperty()).add(20) // 10px d'espace
            );

            // Taille identique ou ajustée
            quarantineViewInclude.prefWidthProperty().bind(
                    cryptViewInclude.prefWidthProperty()
            );

            quarantineViewInclude.setPrefHeight(250);
        }

        // Positionnement et dimensionnement des services médicaux
        if (medicalServiceInclude != null) {
            medicalServiceInclude.layoutXProperty().bind(
                    Bindings.max(
                            Bindings.createDoubleBinding(() -> 290.0, hospitalStructure.widthProperty()),
                            Bindings.divide(
                                    Bindings.subtract(
                                            hospitalStructure.widthProperty(),
                                            medicalServiceInclude.prefWidthProperty()
                                    ).subtract(cryptViewInclude.prefWidthProperty().multiply(0.7)),
                                    2
                            )
                    )
            );

            medicalServiceInclude.layoutYProperty().set(10);

            medicalServiceInclude.prefWidthProperty().bind(hospitalStructure.widthProperty().multiply(0.55));
            medicalServiceInclude.prefHeightProperty().bind(hospitalStructure.heightProperty().multiply(0.3));
        }

        if (waitingRoomInclude != null) {
            waitingRoomInclude.prefWidthProperty().bind(hospitalStructure.widthProperty().multiply(0.3));
            waitingRoomInclude.prefHeightProperty().bind(hospitalStructure.heightProperty().multiply(0.9));
            waitingRoomInclude.layoutXProperty().bind(hospitalStructure.widthProperty().multiply(0.01));
            waitingRoomInclude.layoutYProperty().bind(hospitalStructure.heightProperty().multiply(0.05));
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

    /**
     * Transmet l'objet hôpital au contrôleur de la salle d'attente
     * pour afficher les créatures dans la salle d'attente
     *
     * @param hospital L'hôpital contenant les données des créatures
     */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
        if (waitingRoomController != null) {
            waitingRoomIncludeController.setHospital(hospital);
        }

        // Mise à jour de la vue de la crypte
        if (cryptViewIncludeController != null) {
            cryptViewIncludeController.setHospital(hospital);
        }

        // Mise à jour de la vue de la crypte
        if (quarantineViewIncludeController != null) {
            quarantineViewIncludeController.setHospital(hospital);
        }
    }

    /**
     * Met à jour l'affichage de la salle d'attente et de la crypte avec les données actuelles
     * Doit être appelée après chaque tour de jeu
     */
    public void updateWaitingRoom() {
        if (waitingRoomController != null && hospital != null) {
            waitingRoomController.updateWaitingRoom();
        }
    }

    /**
     * Met à jour l'affichage de la crypte avec les données actuelles
     */
    public void updateCrypt() {
        if (cryptViewIncludeController != null && hospital != null) {
            cryptViewIncludeController.updateCryptView();
        }
    }

    /**
     * Met à jour l'affichage de la crypte avec les données actuelles
     */
    public void updateQuarantine() {
        if (quarantineViewIncludeController != null && hospital != null) {
            quarantineViewIncludeController.updateQuarantineView();
        }
    }

}
