package com.fantasyhospital.controller;

import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.view.CloseDoorCellView;
import com.fantasyhospital.view.MedicalServiceCellView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controller for displaying a grid of medical services in the Fantasy Hospital application.
 * This controller manages the layout and interaction with medical service rooms.
 */
@Component
public class GridMedicalServiceController {

    private final StageManager stageManager;
    private Hospital hospital;
    private List<MedicalService> services;
    private DoomController doomController;

    /**
     * Constructor for GridMedicalServiceController.
     * Uses Spring's @Lazy annotation to delay initialization until the controller is needed.
     *
     * @param stageManager   the StageManager instance for managing stages
     * @param doomController the DoomController instance for handling doom-related actions
     */
    @Lazy
    public GridMedicalServiceController(StageManager stageManager, DoomController doomController) {
        this.stageManager = stageManager;
        this.doomController = doomController;
    }

    /**
     * FXML annotation to link the GridPane defined in the FXML file to this controller.
     */
    @FXML
    private GridPane gridPane;

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Sets up the grid pane and binds its width and height to its parent pane.
     */
    @FXML
    public void initialize() {
        gridPane.getChildren().clear();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> {
                    if (gridPane.getParent() != null) {
                        gridPane.prefWidthProperty().bind(((Pane) gridPane.getParent()).widthProperty().multiply(0.6));
                        gridPane.prefHeightProperty().bind(((Pane) gridPane.getParent()).heightProperty().multiply(0.5));
                    }
                });
            }
        });
    }

    /**
     * Sets the list of medical services and updates the grid pane to display them.
     *
     * @param medicalServices the list of MedicalService instances to display
     */
    public void setServices(List<MedicalService> medicalServices) {
        this.services = medicalServices;
        updateMedicalServices();
    }

    /**
     * Updates the grid pane to display the medical services.
     * This method clears the existing content and re-populates the grid with service views.
     */
    public void updateMedicalServices() {
        gridPane.getChildren().clear();

        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.CENTER);

        if (hospital != null && hospital.getServices() != null) {
            gridPane.widthProperty().addListener((obs, oldVal, newVal) -> {
                Platform.runLater(this::refreshLayout);
            });
            refreshLayout();
        }
    }

    /**
     * Refreshes the layout of the grid pane to ensure that exactly 3 services are displayed per row.
     * This method calculates the available width and adjusts the size of each service cell accordingly.
     */
    private void refreshLayout() {
        gridPane.getChildren().clear();

        final int SERVICES_PER_ROW = 3;

        double availableWidth = gridPane.getWidth();
        double spacing = 10;

        double cellWidth = (availableWidth - (spacing * (SERVICES_PER_ROW - 1))) / SERVICES_PER_ROW;

        cellWidth = Math.min(cellWidth, 210);

        double cellHeight = 250;

        int col = 0;
        int row = 0;

        for (Room room : hospital.getServices()) {
            if (room instanceof MedicalService service) {
                if (service.getName().equals("Crypte") || service.getName().equals("Room d'attente") ||
                        room instanceof Crypt || room instanceof Quarantine) {
                    continue;
                }

                Pane serviceView = MedicalServiceCellView.createView(service, hospital, stageManager, this, doomController);

                serviceView.setPrefWidth(cellWidth);
                serviceView.setMaxWidth(cellWidth);
                serviceView.setMinWidth(cellWidth);

                serviceView.setPrefHeight(cellHeight);
                serviceView.setMaxHeight(cellHeight);
                serviceView.setMinHeight(cellHeight);

                gridPane.add(serviceView, col, row);

                col++;
                if (col >= SERVICES_PER_ROW) {
                    col = 0;
                    row++;
                }
            }
        }
    }


    /**
     * Updates the list of medical services in the grid pane.
     * This method is called to refresh the displayed services when the hospital data changes.
     */
    public void updateServicesList() {
        if (hospital != null) {
            this.services = hospital.getMedicalServices();
            Platform.runLater(this::updateMedicalServices);
        }
    }

    /**
     * Sets the hospital and updates the medical services displayed in the grid pane.
     *
     * @param hospital the Hospital instance containing medical services
     */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
        if (hospital != null) {
            this.services = hospital.getMedicalServices();
            Platform.runLater(this::updateMedicalServices);
        }
    }

    /**
     * Returns the grid pane containing the medical service views.
     *
     * @return the GridPane instance
     */
    public Pane getServiceView() {
        return gridPane;
    }

    /**
     * Displays the close door view for the service door.
     *
     * @param closeServiceDoor the Pane where the close door view will be shown
     */
    public void showCloseDoor(Pane closeServiceDoor) {
        CloseDoorCellView.show(closeServiceDoor);
    }

    /**
     * Clear all services from grid for restart
     */
    public void clearServices() {
        Platform.runLater(() -> {
            gridPane.getChildren().clear();
            hospital = null;
            services = null;
        });
    }
}