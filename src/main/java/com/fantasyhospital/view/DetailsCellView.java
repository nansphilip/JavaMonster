package com.fantasyhospital.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * DetailsCellView is a utility class that provides a method to display detailed information in a modal window.
 * It allows you to show a title and content in a specified width and height.
 */
public class DetailsCellView {

    /**
     * Displays a modal window with the specified title, content, width, and height.
     *
     * @param title  the title of the modal window
     * @param content the content to be displayed in the modal window
     * @param width  the width of the modal window
     * @param height the height of the modal window
     */
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
