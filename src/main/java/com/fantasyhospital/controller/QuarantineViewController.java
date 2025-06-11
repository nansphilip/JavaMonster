package com.fantasyhospital.controller;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.view.CloseDoorCellView;
import com.fantasyhospital.view.QuarantineCellView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

/**
 * Controller for displaying the Quarantine medical service in the Fantasy Hospital application.
 * It manages the view of the quarantine service, including its layout and interaction with the hospital.
 */
@Component
public class QuarantineViewController {

    /**
     * FXML annotation to link the GridPane defined in the FXML file to this controller.
     * This grid pane will display the quarantine service.
     */
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
     * Configure the hospital and set the quarantine service if available.
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
     * Configure the quarantine service to be displayed.
     */
    public void setQuarantine(Quarantine quarantine) {
        this.quarantine = quarantine;
        updateQuarantineView();
    }

    /**
     * Update the quarantine view by clearing the existing content
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


    /**
     * Label displaying the crypt's temperature.
     */
    public void showCloseDoor(VBox closeServiceDoor) {
        CloseDoorCellView.show(closeServiceDoor);
    }
}
