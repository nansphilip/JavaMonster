package com.fantasyhospital.controller;

import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.view.CreatureCellView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Controller for managing the list of creatures in the hospital.
 * Provides functionality to add, display, and update the list of creatures.
 */
@Component
public class ListCreatureController {

    private final StageManager stageManager;
    private Hospital hospital;

    /**
     * Constructor for ListCreatureController.
     * Uses Spring's @Lazy annotation to delay initialization until the controller is needed.
     *
     * @param stageManager the StageManager instance for managing stages
     */
    @Lazy
    public ListCreatureController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    /**
     * Adds a creature to the observable list and refreshes the ListView.
     *
     * @param creature the creature to be added
     */
    @FXML
    public void addCreature(Creature creature) {
        observableCreatures.add(creature);
        creatureListView.refresh();
    }

    /**
     * FXML annotation to link the ListView defined in the FXML file to this controller.
     */
    @FXML
    private ListView<Creature> creatureListView;

    private ObservableList<Creature> observableCreatures = FXCollections.observableArrayList();
    private ScheduledExecutorService scheduler;

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Sets up the ListView and binds it to the observable list of creatures.
     */
    @FXML
    public void initialize() {
        creatureListView.setItems(observableCreatures);
        creatureListView.setCellFactory(listView -> new CreatureCellView(hospital));
        scheduler = Executors.newSingleThreadScheduledExecutor();
        loadCreatures();
    }

    /**
     * Stops the application and closes the JavaFX platform.
     * This method is called when the application is closed.
     */
    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        Platform.exit();
    }

    /**
     * Sets the stage for this controller and defines the action on close request.
     * This method is called to initialize the stage with the controller.
     *
     * @param stage the Stage instance to be set
     */
    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> stop());

    }

    /**
     * Loads the list of creatures from the hospital and updates the observable list.
     * This method is called to refresh the creature list view.
     */
    private void loadCreatures() {
        if (hospital != null) {
            List<Creature> creatures = hospital.displayCreaturesList();
            observableCreatures.setAll(creatures);
        }
    }

    /**
     * Updates the creatures list view by reloading the creatures from the hospital.
     * This method is called to refresh the display of creatures.
     */
    public void updateCreaturesList() {
        Platform.runLater(this::loadCreatures);
    }

    /**
     * Sets the hospital for this controller and updates the creature list view.
     * This method is called to initialize the controller with the hospital context.
     *
     * @param hospital the hospital containing the creatures
     */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
        creatureListView.setCellFactory(listView -> new CreatureCellView(hospital));
        loadCreatures();
    }

    /**
     * Clear all creatures from the list for restart
     */
    public void clearCreatures() {
        Platform.runLater(() -> {
            observableCreatures.clear();
            creatureListView.refresh();
        });
    }
}
