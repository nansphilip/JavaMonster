package com.fantasyhospital;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlLocation = getClass().getResource("/fxml/MainView.fxml");
        if (fxmlLocation == null) {
            throw new RuntimeException("Fichier FXML non trouvé : /fxml/MainView.fxml");
        }

        Parent root = FXMLLoader.load(fxmlLocation);
        primaryStage.setTitle("Fantasy Hospital - Console de Logs");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
