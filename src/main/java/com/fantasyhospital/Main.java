package com.fantasyhospital;

import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL welcomeFxml = getClass().getResource("/fxml/LogoView.fxml");
        if (welcomeFxml == null) {
            throw new RuntimeException("Fichier LogoView.fxml non trouvé");
        }

        Parent welcomeRoot = FXMLLoader.load(welcomeFxml);
        primaryStage.setTitle("Fantasy Hospital - Bienvenue");
        primaryStage.setScene(new Scene(welcomeRoot, 800, 600)); // Taille plus petite
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Fermeture de l'application...");
            Platform.exit();
            System.exit(0);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
