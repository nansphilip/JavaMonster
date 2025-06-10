package com.fantasyhospital.controller;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.view.DoctorsCellView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import lombok.Setter;

@Component
public class ListDoctorsController {

	@FXML
	private ListView<Doctor> doctorsListView;

	@Setter
    private DoomController doomController;


	private ObservableList<Doctor> observableDoctors = FXCollections.observableArrayList();

	private final StageManager stageManager;
	private Hospital hospital;

	private ScheduledExecutorService scheduler;

	@Lazy
	public ListDoctorsController(StageManager stageManager, DoomController doomController) {
		this.stageManager = stageManager;
		this.doomController = doomController;
	}

	@FXML
	public void addDoctor(Doctor doctor) {
		observableDoctors.add(doctor);
		doctorsListView.refresh();
	}

	@FXML
	public void initialize() {
		doctorsListView.setItems(observableDoctors);
		scheduler = Executors.newSingleThreadScheduledExecutor();
		loadDoctors();
	}

	public void updateDoctorsList() {
		Platform.runLater(this::loadDoctors);
	}

    private void loadDoctors() {
		if (hospital != null) {
			List<Doctor> doctors = hospital.getDoctorsList();
			observableDoctors.setAll(doctors);
		}
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
		doctorsListView.setCellFactory(listView -> new DoctorsCellView(hospital, doomController));
		loadDoctors();
	}

	/**
	 * Clear all doctors from the list for restart
	 */
	public void clearDoctors() {
		Platform.runLater(() -> {
			observableDoctors.clear();
			doctorsListView.refresh();
		});
	}
}
