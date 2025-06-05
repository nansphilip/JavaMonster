package com.fantasyhospital.controller;

import java.util.List;

import com.fantasyhospital.model.rooms.Room;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import javafx.scene.layout.Pane;
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
			int col = 0;
			int row = 0;
			int maxColsPerRow = 3; // 4 services par ligne maximum

			for (Room room : hospital.getServices()) {
				if (room instanceof MedicalService service) {
					// Ignorer la crypte et la salle d'attente qui sont affichés séparément
					if (service.getName().equals("Crypte") || service.getName().equals("Room d'attente") ||
							room instanceof Crypt || room instanceof Quarantine) {
						continue;
					}

					Pane serviceView = MedicalServiceCellView.createView(service, hospital, stageManager);
					gridPane.add(serviceView, col, row);

					col++;
					if (col >= maxColsPerRow) {
						col = 0;
						row++;
					}
				}
			}
		}
	}



	public void updateServicesList() {
		if (hospital != null) {
			this.services = hospital.getMedicalServices();
			Platform.runLater(() -> updateMedicalServices());
		}
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
		if (hospital != null) {
			this.services = hospital.getMedicalServices();
			Platform.runLater(() -> updateMedicalServices());
		}
	}
}