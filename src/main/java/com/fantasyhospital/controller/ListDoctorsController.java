package com.fantasyhospital.controller;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

@Component
public class ListDoctorsController {

	@FXML
	private ListView<Doctor> doctorListView;  // Liste des docteurs

	private ObservableList<Doctor> observableDoctors = FXCollections.observableArrayList();

	private final StageManager stageManager;
	private Hospital hospital;

	@Lazy
	public ListDoctorsController(StageManager stageManager) {
		this.stageManager = stageManager;
	}

	@FXML
	public void initialize() {
		doctorListView.setItems(observableDoctors);
		loadDoctors();
	}

	public void updateDoctorsList() {
		loadDoctors();
	}

	private void loadDoctors() {
		if (hospital != null) {
			List<Doctor> doctors = hospital.getDoctorsList();
			observableDoctors.setAll(doctors);
		}
	}

	@FXML
	public void addDoctor(Doctor doctor) {
		observableDoctors.add(doctor);
		doctorListView.refresh();
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
		loadDoctors();
	}
}
