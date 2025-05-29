package com.fantasyhospital.controller;

import java.util.List;

import javafx.scene.layout.Pane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.model.Hospital;
<<<<<<< HEAD
import com.fantasyhospital.rooms.medicalservice.MedicalService;
=======
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
>>>>>>> 5711fe2 (feat: squash branch before MR)
import com.fantasyhospital.view.MedicalServiceCellView;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
<<<<<<< HEAD
import javafx.scene.layout.VBox;
=======
>>>>>>> 5711fe2 (feat: squash branch before MR)

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
	}

	public void setServices(List<MedicalService> medicalServices) {
		this.services = medicalServices;
		displayServices();
	}

	private void displayServices() {
		gridPane.getChildren().clear();

		int columns = 3;
		int row = 0;
		int col = 0;

		for (MedicalService service : services) {
			Pane pane = MedicalServiceCellView.createView(service, hospital);
			gridPane.add(pane, col, row);

			col++;
			if (col >= columns) {
				col = 0;
				row++;
			}
		}
	}

	public void updateServicesList() {
		this.services = hospital.getMedicalServices();
		Platform.runLater(() -> displayServices());
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
		this.services = hospital.getMedicalServices();

		Platform.runLater(() -> displayServices());
	}
}