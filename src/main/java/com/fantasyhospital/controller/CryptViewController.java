package com.fantasyhospital.controller;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.view.CloseDoorCellView;
import com.fantasyhospital.view.CryptCellView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

/**
 * Controller for the Crypt view in the Fantasy Hospital application.
 * This controller manages the display and interaction with the Crypt medical service room.
 */
@Component
public class CryptViewController {


    /**
     * The GridPane that contains the crypt view.
     */
    @FXML
    private GridPane cryptGridPane;

    private Crypt crypt;

    /**
     * Initializes the CryptViewController.
     * This method is called by the JavaFX framework after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Sets the hospital and retrieves the crypt from its services.
     * This method is called to initialize the controller with the hospital context.
     *
     * @param hospital the hospital containing the crypt service
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
     * Sets the crypt instance and updates the view.
     *
     * @param crypt the Crypt instance to set
     */
    public void setCrypt(Crypt crypt) {
        this.crypt = crypt;

        updateCryptView();
    }

    /**
     * Updates the crypt view by clearing the existing content and rendering the new view.
     * This method is called whenever the crypt data changes.
     */
    public void updateCryptView() {
        Platform.runLater(() -> {
            if (cryptGridPane == null || crypt == null) return;

            cryptGridPane.getChildren().clear();


            CryptCellView view = new CryptCellView(crypt,crypt.getDoctors(), this);
            VBox viewContent = view.render();

            cryptGridPane.add(viewContent, 0, 0);
        });
    }

    /**
     * Displays the close door view for the crypt.
     * This method is called to show the close door interface when needed.
     *
     * @param closeServiceDoor the VBox containing the close door view
     */
    public void showCloseDoor(VBox closeServiceDoor) {
        CloseDoorCellView.show(closeServiceDoor);
    }
}
