package com.fantasyhospital.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DetailsCellView {

    public static void show(String title, Node content, int width, int height) {
        Stage detailStage = new Stage();
        detailStage.setTitle(title);

        VBox container = new VBox(10);
        container.setStyle("-fx-background-color: #ffffff; -fx-padding: 20;");
        container.getChildren().add(content);

        Scene scene = new Scene(container, width, height);
        detailStage.setScene(scene);
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.show();
    }
}
