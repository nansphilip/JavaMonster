package com.fantasyhospital.view;

import com.fantasyhospital.util.ImageUtils;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CloseDoorCellView {
    public static void show(Pane closePane) {
        closePane.getChildren().clear();

        ImageView imageView = ImageUtils.createImageView("/images/room/CloseDoor.png", 100, 100);

        VBox content = new VBox(10, imageView);
        content.setAlignment(Pos.CENTER);
        content.prefWidthProperty().bind(closePane.widthProperty());
        content.prefHeightProperty().bind(closePane.heightProperty());

        closePane.getChildren().add(content);
    }
}