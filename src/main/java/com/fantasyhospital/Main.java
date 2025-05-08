package com.fantasyhospital;

import java.net.URL;

import com.fantasyhospital.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlLocation = getClass().getResource("/fxml/MainView.fxml");
        if (fxmlLocation == null) {
            throw new RuntimeException("Fichier FXML non trouvé : /fxml/MainView.fxml");
        }

        // Charger la scène FXML
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        // Obtenir le contrôleur de la vue FXML
        MainController controller = loader.getController();

        // Passer le Stage au contrôleur
        controller.setStage(primaryStage);

        // Configurer et afficher la scène
        primaryStage.setTitle("Fantasy Hospital - Console de Logs");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}