package com.fantasyhospital.controller;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fantasyhospital.config.FxmlView;
import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.view.CreatureCellView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 */

@Component
public class ListCreatureController {

	private final StageManager stageManager;
	private Hospital hospital;

	@Lazy
	public ListCreatureController(StageManager stageManager) {
		this.stageManager = stageManager;
	}

	@FXML
	public void addCreature(Creature creature) {
		observableCreatures.add(creature);
		creatureListView.refresh();
	}

	@FXML
	private ImageView creatureImage;

	@FXML
	private ListView<Creature> creatureListView;

	private ObservableList<Creature> observableCreatures = FXCollections.observableArrayList();
	private ScheduledExecutorService scheduler;

	@FXML
	public void initialize() {
		creatureListView.setItems(observableCreatures);
		creatureListView.setCellFactory(listView -> new CreatureCellView(hospital));
		scheduler = Executors.newSingleThreadScheduledExecutor();
		loadCreatures();
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

	private void loadCreatures() {
		if (hospital != null) {
			List<Creature> creatures = hospital.displayCreaturesList();
			observableCreatures.setAll(creatures);
		}
	}

	public void updateCreaturesList() {
		Platform.runLater(this::loadCreatures);
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
		loadCreatures();
	}
}
