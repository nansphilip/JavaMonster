package com.fantasyhospital.controller;

import com.fantasyhospital.util.LogUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Contrôleur JavaFX chargé d'afficher le contenu du fichier de log
 * (logs/app.log) dans la zone de texte de la fenêtre graphique.
 * <p>
 * Ce contrôleur lit le fichier de log à l'initialisation de la vue et affiche
 * chaque ligne dans le composant TextArea nommé logConsole.
 * </p>
 */
public class MainController {

    @FXML
    private TextArea logConsole;

    @FXML
    private ImageView creatureImage;

    private final Path logFilePath = Path.of("logs/app.log");
    private ScheduledExecutorService scheduler;
    private long lastLength = 0;

    @FXML
    public void initialize() {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (Files.exists(logFilePath)) {
                    long currentLength = Files.size(logFilePath);

                    if (currentLength != lastLength) {
                        String content = Files.readString(logFilePath);
                        lastLength = currentLength;

                        Platform.runLater(() -> {
                            logConsole.setText(content);
                            logConsole.positionCaret(content.length());
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS); // met à jour toutes les secondes
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        Platform.exit();  // Ferme toutes les ressources JavaFX
    }

    // Cette méthode permet d'écouter l'événement de fermeture de la fenêtre
    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> stop()); // Ajoute un gestionnaire pour la fermeture

    }

    public void clearLog(ActionEvent actionEvent) {
        LogUtils.clearLogFile(); // Appeler la méthode de nettoyage du fichier de log
        logConsole.setText(""); // Effacer également le contenu du TextArea
    }

    private void creatureImageLog(String logContent) {
        if (logContent.contains("Zombie")) {
            creatureImage.setImage(new Image(getClass().getResourceAsStream("/images/Zombie.PNG")));
        } else if (logContent.contains("Orque")) {
            creatureImage.setImage(new Image(getClass().getResourceAsStream("/images/Orque.PNG")));
//        } else {
//            // Une image par défaut ou vide
//            creatureImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Nain.png"))));
        }
    }
}
