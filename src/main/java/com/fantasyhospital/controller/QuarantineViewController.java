package com.fantasyhospital.controller;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.view.CloseDoorCellView;
import com.fantasyhospital.view.QuarantineCellView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class QuarantineViewController {

    @FXML
    private GridPane quarantineGridPane;

    private Quarantine quarantine;

    /**
     * Initialize quarantine service
     */
    @FXML
    public void initialize() {

    }

    /**
     * Configure l'hôpital et trouve la quarantaine
     *
     * @param hospital l'hôpital contenant la quarantaine
     */
    public void setHospital(Hospital hospital) {
        if (hospital != null && hospital.getServices() != null) {
            hospital.getServices().stream()
                    .filter(room -> room instanceof Quarantine)
                    .map(room -> (Quarantine) room)
                    .findFirst()
                    .ifPresent(this::setQuarantine);
        }
    }

    /**
     * Configure la crypte et met à jour l'affichage
     *
     * @param quarantine la crypte à afficher
     */
    public void setQuarantine(Quarantine quarantine) {
        this.quarantine = quarantine;
        updateQuarantineView();
    }

    /**
     * Met à jour l'affichage de la crypte
     */
    public void updateQuarantineView() {
        Platform.runLater(() -> {
            if (quarantineGridPane == null || quarantine == null) return;

            quarantineGridPane.getChildren().clear();

            QuarantineCellView view = new QuarantineCellView(quarantine, quarantine.getDoctors(), this);
            VBox viewContent = view.render();

            quarantineGridPane.add(viewContent, 0, 0);
        });
    }

    public void showCloseDoor(VBox closeServiceDoor) {
        CloseDoorCellView.show(closeServiceDoor);
    }
}
