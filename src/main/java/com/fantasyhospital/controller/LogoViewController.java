package com.fantasyhospital.controller;

import com.fantasyhospital.config.FxmlView;
import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.util.PixelTypeWriter;
import jakarta.annotation.PostConstruct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class LogoViewController {

    @FXML
    private Text dialogueText;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private StackPane stackPane;

    private final StageManager stageManager;

    @Lazy
	public LogoViewController(StageManager stageManager) {this.stageManager = stageManager;}

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

	@FXML
    private void startGame(ActionEvent event) {
        stageManager.switchScene(FxmlView.MAIN);

        stageManager.switchToMaximizedMode();
    }
}
