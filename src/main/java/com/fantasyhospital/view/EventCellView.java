package com.fantasyhospital.view;

import com.fantasyhospital.controller.DoomController;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class EventCellView {

    public static void showHarakiri(Doctor doctor, DoomController doomController) {
        Image gif = new Image(EventCellView.class.getResource("/images/gif/Harakiri.gif").toExternalForm());
        ImageView imageView = new ImageView(gif);
        imageView.setFitWidth(128);
        imageView.setFitHeight(128);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(doctor.getFullName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox content = new VBox(10, imageView, nameLabel);
        content.setAlignment(Pos.CENTER);

        doomController.clearDoomBox();
        doomController.getDoomBox().getChildren().add(content);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> doomController.clearDoomBox());
        delay.play();
    }

    public static void showServiceClose(MedicalService medicalService, DoomController doomController) {
        Image gif = new Image(EventCellView.class.getResource("/images/gif/MedicalServiceBomb.gif").toExternalForm());
        ImageView imageView = new ImageView(gif);
        imageView.setFitWidth(128);
        imageView.setFitHeight(128);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(medicalService.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox content = new VBox(10, imageView, nameLabel);
        content.setAlignment(Pos.CENTER);

        doomController.clearDoomBox();
        doomController.getDoomBox().getChildren().add(content);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> doomController.clearDoomBox());
        delay.play();
    }
}
