package com.fantasyhospital.view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * CloseDoorCellView is a utility class that provides a method to display a close door image
 * in a given Pane. It creates an ImageView with the close door image and adds it to the Pane.
 */
public class CloseDoorCellView {

    /**
     * Displays a close door image in the provided Pane.
     *
     * @param closePane the Pane where the close door image will be displayed
     */
    public static void show(Pane closePane) {

        closePane.getChildren().clear();

        Image image = new Image(CloseDoorCellView.class.getResource("/images/room/CloseDoor.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);


        VBox content = new VBox(10, imageView);
        content.setAlignment(Pos.CENTER);

        content.prefWidthProperty().bind(closePane.widthProperty());
        content.prefHeightProperty().bind(closePane.heightProperty());

        closePane.getChildren().add(content);
    }
}
