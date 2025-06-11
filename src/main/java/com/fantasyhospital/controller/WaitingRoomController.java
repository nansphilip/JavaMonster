package com.fantasyhospital.controller;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.view.CreatureCellView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controller for managing the waiting room in the hospital.
 * Displays a list of creatures currently waiting for treatment.
 */
@Component
public class WaitingRoomController {

    private Hospital hospital;

    /**
     * FXML annotation to link the ListView defined in the FXML file to this controller.
     */
    @FXML
    private ListView<Creature> waitingRoomListView;

    private final ObservableList<Creature> observableCreatures = FXCollections.observableArrayList();

    /**
     * Initializes the controller after its root element has been processed.
     * Binds the ListView to the observable list of creatures.
     */
    @FXML
    public void initialize() {
        // on lie la ListView à notre liste observable
        waitingRoomListView.setItems(observableCreatures);
        waitingRoomListView.setCellFactory(param -> new CreatureCellView(hospital));
    }

    /**
     * Sets the hospital instance for this controller and loads the waiting room creatures.
     *
     * @param hospital the hospital instance to set
     */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
        loadWaitingRoomCreatures();
    }

    /**
     * Loads the creatures currently in the waiting room from the hospital.
     * This method retrieves the list of creatures from the waiting room and updates the observable list.
     */
    private void loadWaitingRoomCreatures() {
        if (hospital != null) {
            Room waitingRoom = hospital.getWaitingRoom();
            if (waitingRoom != null) {
                List<Creature> creatures = waitingRoom.getCreatures();

                Platform.runLater(() -> {
                    observableCreatures.setAll(creatures);
                });
            }
        }
    }

    /**
     * Updates the waiting room by reloading the creatures from the hospital.
     * This method can be called to refresh the displayed list of creatures.
     */
    public void updateWaitingRoom() {
        loadWaitingRoomCreatures();
    }

    /**
     * Sets the stage for this controller and defines the action on close request.
     * This method is called to initialize the stage with the controller.
     *
     * @param stage the Stage instance to be set
     */
    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> Platform.exit());
    }

    /**
     * Clear waiting room for restart
     */
    public void clearWaitingRoom() {
        Platform.runLater(() -> {
            observableCreatures.clear();
            waitingRoomListView.refresh();
        });
    }
}
