package com.fantasyhospital.view;

import com.fantasyhospital.controller.GridMedicalServiceController;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class CloseDoorCellView {
    public static void show(GridMedicalServiceController gridMedicalServiceController) {
        Image image = new Image(HarakiriCellView.class.getResource("/images/room/CloseDoor.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);


        VBox content = new VBox(10, imageView);
        content.setAlignment(Pos.CENTER);

        gridMedicalServiceController.getServiceView();
        gridMedicalServiceController.getServiceView().getChildren().add(content);
    }
}
