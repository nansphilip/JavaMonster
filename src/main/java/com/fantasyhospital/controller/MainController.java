package com.fantasyhospital.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * JavaFX controller responsible for displaying the contents of the log file
 * (logs/app.log) in the text area of the graphical window.
 * <p>
 * This controller reads the log file when the view is initialized and displays
 * each line in the TextArea component named logConsole.
 * </p>
 */
public class MainController {

    @FXML
    private TextArea logConsole;

    @FXML
    public void initialize() {
        File logFile = new File("logs/app.log");

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
