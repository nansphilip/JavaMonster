package com.fantasyhospital.controller;

import java.util.List;

import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.view.CloseDoorCellView;
import com.fantasyhospital.view.HarakiriCellView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.view.MedicalServiceCellView;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

@Component
public class GridMedicalServiceController {

	private final StageManager stageManager;
	private Hospital hospital;
	private List<MedicalService> services;

	@Lazy
	public GridMedicalServiceController(StageManager stageManager) {
		this.stageManager = stageManager;
	}

	@FXML
	private GridPane gridPane;

	@FXML
	public void initialize() {
		gridPane.getChildren().clear();
		gridPane.setHgap(10);
		gridPane.setVgap(15);
		gridPane.setAlignment(Pos.CENTER);

		// Plutôt que d'accéder au parent ici, attendez que le composant soit affiché
		gridPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				// Le composant est maintenant attaché à une scène
				Platform.runLater(() -> {
					if (gridPane.getParent() != null) {
						// Maintenant on peut accéder au parent en toute sécurité
						gridPane.prefWidthProperty().bind(((Pane) gridPane.getParent()).widthProperty().multiply(0.6));
						gridPane.prefHeightProperty().bind(((Pane) gridPane.getParent()).heightProperty().multiply(0.5));
					}
				});
			}
		});
	}

	public void setServices(List<MedicalService> medicalServices) {
		this.services = medicalServices;
		updateMedicalServices();
	}

	public void updateMedicalServices() {
		gridPane.getChildren().clear();

		// Définir des propriétés de base pour le GridPane
		gridPane.setHgap(10);
		gridPane.setVgap(15);
		gridPane.setAlignment(Pos.CENTER);

		if (hospital != null && hospital.getServices() != null) {
			// Ajout d'un listener pour recalculer la disposition lors du redimensionnement
			gridPane.widthProperty().addListener((obs, oldVal, newVal) -> {
				Platform.runLater(this::refreshLayout);
			});

			refreshLayout();
		}
	}

	private void refreshLayout() {
		gridPane.getChildren().clear();

		// Forcer exactement 3 services par rangée
		final int SERVICES_PER_ROW = 3;

		// Calcul de l'espace disponible
		double availableWidth = gridPane.getWidth();
		double spacing = 10; // Espacement entre les services

		// Calcul de la largeur pour chaque cellule avec l'espacement
		double cellWidth = (availableWidth - (spacing * (SERVICES_PER_ROW - 1))) / SERVICES_PER_ROW;

		// Limite la largeur maximale pour éviter des services trop larges sur grands écrans
		cellWidth = Math.min(cellWidth, 210);

		// Hauteur augmentée pour les services médicaux
		double cellHeight = 250;

		int col = 0;
		int row = 0;

		for (Room room : hospital.getServices()) {
			if (room instanceof MedicalService service) {
				// Ignorer la crypte et la salle d'attente qui sont affichés séparément
				if (service.getName().equals("Crypte") || service.getName().equals("Room d'attente") ||
						room instanceof Crypt || room instanceof Quarantine) {
					continue;
				}

				// Créer la vue du service médical
				Pane serviceView = MedicalServiceCellView.createView(service, hospital, stageManager, this);

				// Définir des dimensions fixes pour assurer 3 services par ligne
				serviceView.setPrefWidth(cellWidth);
				serviceView.setMaxWidth(cellWidth);
				serviceView.setMinWidth(cellWidth);

				// Augmenter la hauteur des services
				serviceView.setPrefHeight(cellHeight);
				serviceView.setMaxHeight(cellHeight);
				serviceView.setMinHeight(cellHeight);

				gridPane.add(serviceView, col, row);

				col++;
				if (col >= SERVICES_PER_ROW) { // Passer à la ligne suivante après 3 services
					col = 0;
					row++;
				}
			}
		}
	}



	public void updateServicesList() {
		if (hospital != null) {
			this.services = hospital.getMedicalServices();
			Platform.runLater(this::updateMedicalServices);
		}
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
		if (hospital != null) {
			this.services = hospital.getMedicalServices();
			Platform.runLater(this::updateMedicalServices);
		}
	}

	public Pane getServiceView() {
		return gridPane;
	}

	public void showCloseDoor() {
		CloseDoorCellView.show(this);
	}

}