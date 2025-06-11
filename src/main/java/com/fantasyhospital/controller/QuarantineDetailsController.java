package com.fantasyhospital.controller;

import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

/**
 * Controller for displaying details of the Quarantine medical service.
 * It shows the number of creatures in quarantine and the status of the service.
 */
@Component
public class QuarantineDetailsController {

    /**
     * Label displaying the count of creatures in quarantine.
     */
    @FXML
    private Label creatureCount;

    /**
     * Label displaying the status of the quarantine service.
     */
    @FXML
    private Label status;

    /**
     * Initializes the controller after the FXML file has been loaded.
     * This method is called automatically by JavaFX.
     */
    public void setQuarantine(Quarantine quarantine) {
        creatureCount.setText("Créatures en isolement: " + quarantine.getCreatures().size());
        status.setText("État: " + (quarantine.isHasServiceToClose() ? "En fermeture" : "Opérationnel"));
    }
}