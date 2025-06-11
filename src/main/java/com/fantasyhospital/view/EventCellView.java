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

/**
 * EventCellView is a utility class that provides methods to display specific events in the DoomController's doom box.
 * It shows animations and labels for events like a doctor committing harakiri or a medical service closing.
 */
public class EventCellView {

    /**
     * Displays a harakiri event for a doctor in the DoomController's doom box.
     *
     * @param doctor        the doctor who is committing harakiri
     * @param doomController the DoomController instance to update the doom box
     */
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

    /**
     * Displays a medical service closing event in the DoomController's doom box.
     *
     * @param medicalService the medical service that is closing
     * @param doomController  the DoomController instance to update the doom box
     */
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
