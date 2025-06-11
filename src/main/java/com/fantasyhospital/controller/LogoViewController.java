package com.fantasyhospital.controller;

import com.fantasyhospital.config.FxmlView;
import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.util.PixelTypeWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Controller for the logo view, displaying a welcome message and background image.
 * Handles the initialization of the view and the start game action.
 */
@Component
public class LogoViewController {

    /**
     * Starts the game when the button is clicked.
     */
    @FXML
    private Text dialogueText;

    /**
     * FXML annotation to link the ImageView defined in the FXML file to this controller.
     */
    @FXML
    private ImageView backgroundImage;

    /**
     * FXML annotation to link the StackPane defined in the FXML file to this controller.
     */
    @FXML
    private StackPane stackPane;

    private final StageManager stageManager;

    /**
     * Constructor for LogoViewController.
     * Uses Spring's @Lazy annotation to delay initialization until the controller is needed.
     *
     * @param stageManager the StageManager instance for managing stages
     */
    @Lazy
    public LogoViewController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Sets up the dialogue text and background image.
     */
    @FXML
    public void initialize() {
        String message = "Salut la team c'est Sypher 13 !\n" +
                "Je vous souhaite la bienvenue \n dans Fantasy Hospital !\n" +
                "J'espère que vous allez apprécier \n ce jeu !\n" +
                "N'hésitez pas à me faire \n part de vos retours !";

        String fontPath = "/fonts/FantasyHospital.ttf";

        PixelTypeWriter.playTypingEffect(dialogueText, message, fontPath, 24, 50);

        backgroundImage.setImage(new Image(getClass().getResourceAsStream("/images/tiles/HospitalBackground.jpg")));
        backgroundImage.fitWidthProperty().bind(stackPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(stackPane.heightProperty());
    }

    /**
     * Starts the game when the button is clicked.
     * Switches the scene to the main view and sets the stage to maximized mode.
     *
     * @param event the ActionEvent triggered by the button click
     */
    @FXML
    private void startGame(ActionEvent event) {
        stageManager.switchScene(FxmlView.MAIN);

        stageManager.switchToMaximizedMode();
    }
}
