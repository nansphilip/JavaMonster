package com.fantasyhospital.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

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
    public void initialize() {
        File logFile = new File("logs/app.log"); // Dossier logs à la racine de ton projet

        if (logFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logConsole.appendText(line + "\n");
                }
            } catch (IOException e) {
                logConsole.appendText("Erreur lors de la lecture du fichier de log : " + e.getMessage());
            }
        } else {
            logConsole.appendText("Fichier de log introuvable à : " + logFile.getAbsolutePath());
        }
    }
}
