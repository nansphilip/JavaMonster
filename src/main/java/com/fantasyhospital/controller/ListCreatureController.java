package com.fantasyhospital.controller;

import com.fantasyhospital.Simulation;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.view.CreatureCellView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 */
public class ListCreatureController {



	@FXML
	private ImageView creatureImage;

	@FXML
	private ListView<Creature> creatureListView;

	private ObservableList<Creature> observableCreatures = FXCollections.observableArrayList();
	private ScheduledExecutorService scheduler;

	@FXML
	public void initialize() {
		creatureListView.setItems(observableCreatures);
		creatureListView.setCellFactory(listView -> new CreatureCellView());
		scheduler = Executors.newSingleThreadScheduledExecutor();
	}


	public void stop() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdown();
		}
		Platform.exit();
	}

	public void setStage(Stage stage) {
		stage.setOnCloseRequest(event -> stop());

	}

	public void addCreature(Creature creature) {
		Platform.runLater(() -> {
			observableCreatures.add(creature);
			creatureListView.setItems(observableCreatures);
		});
	}

	@FXML
	private void startSimulation() {
		Simulation simulation = new Simulation(this);
		simulation.startSimulation();
	}
}
