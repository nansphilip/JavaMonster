package com.fantasyhospital;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import com.fantasyhospital.MessageCard;
import com.fantasyhospital.CustomButton;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Header avec menu
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10);
        header.setStyle("-fx-background-color: #222; -fx-background-radius: 10px; -fx-padding: 10px;");

        CustomButton tab1 = new CustomButton("Onglet Bleu");
        CustomButton tab2 = new CustomButton("Onglet Vert");
        tab1.setStyle(tab1.getStyle() + "-fx-background-color: #2196F3;"); // bleu par défaut
        tab2.setStyle(tab2.getStyle() + "-fx-background-color: #4CAF50;");

        header.getChildren().addAll(tab1, tab2);

        // Contenu principal
        VBox content = new VBox();
        content.setMinHeight(120);
        content.setStyle("-fx-background-color: #2196F3; -fx-background-radius: 10px; -fx-padding: 40px; margin: 20px;");

        // Gestion du changement d'onglet
        tab1.setOnAction(e -> {
            content.setStyle("-fx-background-color: #2196F3; -fx-background-radius: 10px; -fx-padding: 40px; margin: 20px;");
            tab1.setStyle(tab1.getStyle().replace("-fx-background-color: #4CAF50;", "-fx-background-color: #2196F3;"));
            tab2.setStyle(tab2.getStyle().replace("-fx-background-color: #2196F3;", "-fx-background-color: #4CAF50;"));
        });
        tab2.setOnAction(e -> {
            content.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 10px; -fx-padding: 40px; margin: 20px;");
            tab2.setStyle(tab2.getStyle().replace("-fx-background-color: #2196F3;", "-fx-background-color: #4CAF50;"));
            tab1.setStyle(tab1.getStyle().replace("-fx-background-color: #4CAF50;", "-fx-background-color: #2196F3;"));
        });

        VBox root = new VBox(20, header, content);
        root.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 30px;");

        Scene scene = new Scene(root, 400, 250);
        primaryStage.setTitle("Fenêtre JavaFX avec Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
