package com.fantasyhospital.controller;

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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Controller for managing the list of doctors in the hospital.
 * Provides functionality to add, display, and update the list of doctors.
 */
@Component
public class ListDoctorsController {

    /**
     * Adds a doctor to the observable list and refreshes the ListView.
     *
     * @param doctor the doctor to be added
     */
    @FXML
    private ListView<Doctor> doctorsListView;

    /**
     * The DoomController instance for handling doom-related actions.
     */
    @Setter
    private DoomController doomController;


    private ObservableList<Doctor> observableDoctors = FXCollections.observableArrayList();

    private final StageManager stageManager;
    private Hospital hospital;

    private ScheduledExecutorService scheduler;

    /**
     * Constructor for ListDoctorsController.
     * Uses Spring's @Lazy annotation to delay initialization until the controller is needed.
     *
     * @param stageManager   the StageManager instance for managing stages
     * @param doomController the DoomController instance for handling doom-related actions
     */
    @Lazy
    public ListDoctorsController(StageManager stageManager, DoomController doomController) {
        this.stageManager = stageManager;
        this.doomController = doomController;
    }

    /**
     * Adds a doctor to the observable list and refreshes the ListView.
     *
     * @param doctor the doctor to be added
     */
    @FXML
    public void addDoctor(Doctor doctor) {
        observableDoctors.add(doctor);
        doctorsListView.refresh();
    }

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Sets up the ListView and binds it to the observable list of doctors.
     */
    @FXML
    public void initialize() {
        doctorsListView.setItems(observableDoctors);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        loadDoctors();
    }

    /**
     * Updates the list of doctors by reloading from the hospital model.
     */
    public void updateDoctorsList() {
        Platform.runLater(this::loadDoctors);
    }

    /**
     * Loads the list of doctors from the hospital model and updates the ListView.
     * This method is called to initialize the list of doctors when the controller is set.
     */
    private void loadDoctors() {
        if (hospital != null) {
            List<Doctor> doctors = hospital.getDoctorsList();
            observableDoctors.setAll(doctors);
        }
    }

    /**
     * Sets the hospital and updates the ListView to display doctors from the hospital.
     * This method is called to initialize the controller with the hospital context.
     *
     * @param hospital the hospital containing the doctors
     */
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
