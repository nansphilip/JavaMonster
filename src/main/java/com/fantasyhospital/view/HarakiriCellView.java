package com.fantasyhospital.view;

import com.fantasyhospital.model.creatures.Doctor;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public class HarakiriCellView {

    public static void show(Doctor doctor) {
        Window ownerWindow = Stage.getWindows().stream()
                .filter(Window::isShowing)
                .findFirst()
                .orElse(null);

        Stage popup = new Stage();
        if (ownerWindow instanceof Stage ownerStage) {
            popup.initOwner(ownerStage);
            popup.initModality(Modality.WINDOW_MODAL);
        } else {
            popup.initModality(Modality.APPLICATION_MODAL);
        }

        popup.initStyle(StageStyle.TRANSPARENT);

        Image gif = new Image(HarakiriCellView.class.getResource("/images/gif/Harakiri.gif").toExternalForm());
        ImageView imageView = new ImageView(gif);
        imageView.setFitWidth(128);
        imageView.setFitHeight(128);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(doctor.getFullName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox root = new VBox(10, imageView, nameLabel);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(root);
        scene.setFill(null);

        popup.setScene(scene);
        popup.centerOnScreen();
        popup.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> popup.close());
        delay.play();
    }
}
