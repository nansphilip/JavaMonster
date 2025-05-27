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

@Component
public class WaitingRoomController {

    private Hospital hospital;

    @FXML
    private ListView<Creature> waitingRoomListView;

    private final ObservableList<Creature> observableCreatures = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // on lie la ListView à notre liste observable
        waitingRoomListView.setItems(observableCreatures);
        waitingRoomListView.setCellFactory(param -> new CreatureCellView(hospital));
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
        loadWaitingRoomCreatures();
    }

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


    public void updateWaitingRoom() {
        loadWaitingRoomCreatures();
    }

    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> Platform.exit());
    }
}