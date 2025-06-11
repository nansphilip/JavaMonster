package com.fantasyhospital.util;

import java.io.FileWriter;
import java.io.IOException;

public class LogsUtils {

    /**
     * Erases the log file by opening it in write mode and clearing its contents.
     */
    public static void clearLogFile() {
        try (FileWriter writer = new FileWriter("logs/app.log", false)) {
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression du fichier de log : " + e.getMessage());
        }
    }
}