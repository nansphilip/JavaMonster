package com.fantasyhospital.controller;

import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class QuarantineDetailsController {

    @FXML
    private Label creatureCount;

    @FXML
    private Label status;

    public void setQuarantine(Quarantine quarantine) {
        creatureCount.setText("Créatures en isolement: " + quarantine.getCreatures().size());
        status.setText("État: " + (quarantine.isHasServiceToClose() ? "En fermeture" : "Opérationnel"));
    }
}