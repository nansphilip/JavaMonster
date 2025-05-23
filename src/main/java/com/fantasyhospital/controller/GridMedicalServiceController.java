package com.fantasyhospital.controller;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.rooms.medicalservice.MedicalService;
import com.fantasyhospital.view.MedicalServiceCellView;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

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
			VBox box = MedicalServiceCellView.createView(service);
			gridPane.add(box, col, row);

			col++;
			if (col >= columns) {
				col = 0;
				row++;
			}
		}
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
		this.services = hospital.getMedicalServices();

		Platform.runLater(() -> displayServices());
	}
}