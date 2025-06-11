package com.fantasyhospital.controller;

import com.fantasyhospital.model.Hospital;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for managing the hospital structure, including the welcome screen,
 * waiting room, crypt, and quarantine views.
 * <p>
 * This controller initializes the hospital structure UI and manages the game state.
 * </p>
 */
@Component
public class HospitalStructureController implements Initializable {

    /**
     * The Pane representing the hospital structure in the UI.
     */
    @FXML
    private Pane hospitalStructure;
    private StackPane welcomeContainer;
    private Pane waitingRoomView;

    /**
     * Controller for the waiting room view.
     * This controller is responsible for managing the waiting room UI and interactions.
     */
    @Getter
    private WaitingRoomController waitingRoomController;

    /**
     * Initializes the waiting room view and its controller.
     */
    @FXML
    private Pane medicalServiceInclude;

    /**
     * Controller for the medical service view.
     * This controller manages the medical services available in the hospital.
     */
    @FXML
    private Pane cryptViewInclude;

    /**
     * Controller for the crypt view.
     * This controller manages the crypt medical service room.
     */
    @FXML
    private CryptViewController cryptViewIncludeController;

    /**
     * Controller for the quarantine view.
     * This controller manages the quarantine medical service room.
     */
    @FXML
    private Pane quarantineViewInclude;

    /**
     * Controller for the quarantine view.
     * This controller manages the quarantine medical service room.
     */
    @FXML
    private QuarantineViewController quarantineViewIncludeController;

    /**
     * Pane that includes the waiting room view.
     * This pane is used to display the waiting room in the hospital structure.
     */
    @FXML
    private Pane waitingRoomInclude;

    /**
     * Controller for the waiting room view.
     * This controller manages the waiting room UI and interactions.
     */
    @FXML
    private WaitingRoomController waitingRoomIncludeController;

    /**
     * Indicates whether the game has started.
     * This flag is used to track the game state.
     */
    @Getter
    private boolean gameStarted = false;

    /**
     * The hospital instance containing the game data.
     * This instance is used to manage the hospital's state and operations.
     */
    @Getter
    private Hospital hospital;

    /**
     * Initializes the hospital structure controller.
     * This method is called by the JavaFX framework after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        welcomeContainer = new StackPane();

        try {
            ImageView backgroundLogo = new ImageView(new Image(getClass().getResourceAsStream("/images/logo/Logo.jpg")));
            backgroundLogo.setPreserveRatio(false);
            backgroundLogo.fitWidthProperty().bind(hospitalStructure.widthProperty());
            backgroundLogo.fitHeightProperty().bind(hospitalStructure.heightProperty());

            welcomeContainer.getChildren().add(backgroundLogo);
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image de logo: " + e.getMessage());
        }


        Label welcomeText = new Label("Bienvenue à l'Hôpital Fantastique !");
        welcomeText.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: white; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        welcomeContainer.getChildren().add(welcomeText);

        welcomeContainer.prefWidthProperty().bind(hospitalStructure.widthProperty().multiply(0.3));
        welcomeContainer.prefHeightProperty().bind(hospitalStructure.heightProperty());

        welcomeContainer.setLayoutX(0);
        welcomeContainer.setLayoutY(0);

        hospitalStructure.getChildren().add(welcomeContainer);

        if (cryptViewInclude != null) {
            cryptViewInclude.layoutXProperty().bind(
                    hospitalStructure.widthProperty().subtract(cryptViewInclude.prefWidthProperty()).subtract(20)
            );

            cryptViewInclude.layoutYProperty().set(20);

            cryptViewInclude.prefWidthProperty().bind(hospitalStructure.widthProperty().multiply(0.18)); // Réduire à 18%
            cryptViewInclude.setPrefHeight(250);
        }

        if (quarantineViewInclude != null && cryptViewInclude != null) {
            quarantineViewInclude.layoutXProperty().bind(
                    cryptViewInclude.layoutXProperty()
            );

            quarantineViewInclude.layoutYProperty().bind(
                    cryptViewInclude.layoutYProperty().add(cryptViewInclude.prefHeightProperty()).add(20) // 10px d'espace
            );

            quarantineViewInclude.prefWidthProperty().bind(
                    cryptViewInclude.prefWidthProperty()
            );
            quarantineViewInclude.setPrefHeight(250);
        }

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

    /**
     * Displays the welcome message on the hospital structure.
     * This method is called to show the welcome screen when the application starts.
     */
    public void hideWelcomeMessage() {
        if (welcomeContainer != null && hospitalStructure.getChildren().contains(welcomeContainer)) {
            hospitalStructure.getChildren().remove(welcomeContainer);
        }
    }

    /**
     * Starts the game by hiding the welcome message and displaying the waiting room.
     * This method is called when the user is ready to start playing.
     */
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
     * sets the hospital instance and updates the waiting room, crypt, and quarantine views.
     */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
        if (waitingRoomController != null) {
            waitingRoomIncludeController.setHospital(hospital);
        }

        if (cryptViewIncludeController != null) {
            cryptViewIncludeController.setHospital(hospital);
        }

        if (quarantineViewIncludeController != null) {
            quarantineViewIncludeController.setHospital(hospital);
        }
    }


    public void updateWaitingRoom() {
        if (waitingRoomController != null && hospital != null) {
            waitingRoomController.updateWaitingRoom();
        }
    }

    /**
     * Updates the crypt view with the current hospital data.
     */
    public void updateCrypt() {
        if (cryptViewIncludeController != null && hospital != null) {
            cryptViewIncludeController.updateCryptView();
        }
    }

    /**
     * Updates the quarantine view with the current hospital data.
     */
    public void updateQuarantine() {
        if (quarantineViewIncludeController != null && hospital != null) {
            quarantineViewIncludeController.updateQuarantineView();
        }
    }

    /**
     * Clear hospital data and reset to welcome screen for restart
     */
    public void clearHospital() {
        gameStarted = false;
        hospital = null;

        Platform.runLater(() -> {
            if (welcomeContainer != null && !hospitalStructure.getChildren().contains(welcomeContainer)) {
                hospitalStructure.getChildren().add(welcomeContainer);
            }

            if (waitingRoomView != null && hospitalStructure.getChildren().contains(waitingRoomView)) {
                hospitalStructure.getChildren().remove(waitingRoomView);
            }
        });
    }

    /**
     * Reset application to welcome screen (used for restart functionality)
     */
    public void resetToWelcomeScreen() {
        gameStarted = false;
        hospital = null;

        com.fantasyhospital.util.Singleton.getInstance().clearAllData();

        Platform.runLater(() -> {
            if (welcomeContainer != null && !hospitalStructure.getChildren().contains(welcomeContainer)) {
                hospitalStructure.getChildren().add(welcomeContainer);
            }
            
            if (waitingRoomView != null && hospitalStructure.getChildren().contains(waitingRoomView)) {
                hospitalStructure.getChildren().remove(waitingRoomView);
            }
        });
    }
}
