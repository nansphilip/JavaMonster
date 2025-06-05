package com.fantasyhospital.controller;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.view.CryptCellView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CryptViewController {

    @FXML
    private GridPane cryptGridPane;

    private Crypt crypt;
    private final Random random = new Random();

    /**
     * Initialise la vue de la crypte
     */
    @FXML
    public void initialize() {
        // Initialisation de base de la grille
//        if (cryptGridPane != null) {
//            cryptGridPane.getChildren().clear();
//            cryptGridPane.setStyle("-fx-background-color: #202020; -fx-border-color: #444444; -fx-border-width: 1px; -fx-background-image: url('/images/tiles/FloorCrypt.png')");
//        }
    }

    /**
     * Configure l'hôpital et trouve la crypte
     * @param hospital l'hôpital contenant la crypte
     */
    public void setHospital(Hospital hospital) {
        // Rechercher la crypte dans les services de l'hôpital
        if (hospital != null && hospital.getServices() != null) {
            hospital.getServices().stream()
                .filter(room -> room instanceof Crypt)
                .map(room -> (Crypt) room)
                .findFirst()
                .ifPresent(this::setCrypt);
        }
    }

    /**
     * Configure la crypte et met à jour l'affichage
     * @param crypt la crypte à afficher
     */
    public void setCrypt(Crypt crypt) {
        this.crypt = crypt;

        updateCryptView();
    }

    /**
     * Sélectionne aléatoirement une image de lit pour la crypte
     * @return le chemin de l'image sélectionnée
     */
    private String getRandomCryptBedImage() {
        // Images de lits spécifiques à la crypte avec une ambiance plus sombre/froide
        String[] options = {
            "/images/room/BedBones.png",  // Lit avec des os pour l'ambiance crypte
            "/images/room/Bed.png"        // Lit standard en alternative
        };
        return options[random.nextInt(options.length)];
    }

    /**
     * Met à jour l'affichage de la crypte
     */
    public void updateCryptView() {
        Platform.runLater(() -> {
            if (cryptGridPane == null || crypt == null) return;

            cryptGridPane.getChildren().clear();

            // Appel à ta vue custom
            CryptCellView view = new CryptCellView(crypt,crypt.getDoctors());
            VBox viewContent = view.render();

            cryptGridPane.add(viewContent, 0, 0);
        });
    }


}
