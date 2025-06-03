package com.fantasyhospital.view;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public class HarakiriCellView {

    public static void show() {
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
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        StackPane root = new StackPane(imageView);
        root.setStyle("-fx-background-color: transparent;");
        Scene scene = new Scene(root);
        scene.setFill(null);

        popup.setScene(scene);
        popup.centerOnScreen();
        popup.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> popup.close());
        delay.play();
    }
}
