package com.fantasyhospital.util;

import java.io.FileWriter;
import java.io.IOException;

public class LogsUtils {

    /**
     * Efface le contenu du fichier de log
     */
    public static void clearLogFile() {
        try (FileWriter writer = new FileWriter("logs/app.log", false)) {
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression du fichier de log : " + e.getMessage());
        }
    }
}