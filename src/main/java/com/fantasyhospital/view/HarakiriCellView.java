package com.fantasyhospital.view;

import com.fantasyhospital.controller.DoomController;
import com.fantasyhospital.model.creatures.Doctor;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class HarakiriCellView {

    public static void show(Doctor doctor, DoomController doomController) {
        Image gif = new Image(HarakiriCellView.class.getResource("/images/gif/Harakiri.gif").toExternalForm());
        ImageView imageView = new ImageView(gif);
        imageView.setFitWidth(128);
        imageView.setFitHeight(128);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(doctor.getFullName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox content = new VBox(10, imageView, nameLabel);
        content.setAlignment(Pos.CENTER);

        doomController.clearHarakiri();
        doomController.getHarakiriContainer().getChildren().add(content);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> doomController.clearHarakiri());
        delay.play();
    }
}
