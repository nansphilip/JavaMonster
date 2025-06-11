package com.fantasyhospital.controller;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.view.CreatureCellView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controller for displaying details of a medical service, quarantine, or crypt in the hospital.
 * It shows the title, budget, list of creatures, and doctors associated with the selected service.
 */
@Component
public class MedicalServiceDetailsController {

    /**
     * Label displaying the title of the medical service.
     */
    @FXML
    private Label title;

    /**
     * Label displaying the budget of the medical service.
     */
    @FXML
    private Label budget;

    /**
     * ListView displaying the creatures associated with the medical service.
     */
    @FXML
    private ListView<Creature> medicalServiceListView;

    /**
     * VBox container for displaying the doctors associated with the medical service.
     */
    @FXML
    private VBox doctorsContainer;

    private final ObservableList<Creature> observableCreatures = FXCollections.observableArrayList();

    /**
     * The hospital instance associated with this controller.
     */
    @Setter
    private Hospital hospital;
    private MedicalService service;
    private Quarantine quarantine;
    private Crypt crypt;

    /**
     * Initializes the controller and sets up the ListView with a custom cell factory.
     */
    @FXML
    public void initialize() {
        medicalServiceListView.setItems(observableCreatures);
        medicalServiceListView.setCellFactory(listView -> new CreatureCellView(hospital));
    }

    /**
     * Sets the medical service to be displayed in this controller.
     *
     * @param service the medical service to display
     */
    public void setService(MedicalService service) {
        this.service = service;
        this.quarantine = null;
        this.crypt = null;
        updateView();
    }

    /**
     * Sets the quarantine to be displayed in this controller.
     *
     * @param quarantine the quarantine to display
     */
    public void setQuarantine(Quarantine quarantine) {
        this.quarantine = quarantine;
        this.service = null;
        this.crypt = null;
        updateView();
    }

    /**
     * Sets the crypt to be displayed in this controller.
     *
     * @param crypt the crypt to display
     */
    public void setCrypt(Crypt crypt) {
        this.quarantine = null;
        this.service = null;
        this.crypt = crypt;
        updateView();
    }

    /**
     * Sets the list of creatures to be displayed in the ListView.
     *
     * @param creatures the list of creatures to display
     */
    public void setCreatures(List<Creature> creatures) {
        medicalServiceListView.setItems(FXCollections.observableArrayList(creatures));
    }

    /**
     * Updates the view with the current service, quarantine, or crypt details.
     * This method updates the title, budget, list of creatures, and doctors.
     */
    private void updateView() {
        if (service != null) {
            title.setText("Détails du service : " + service.getName());
            budget.setText("Budget : " + BudgetType.fromRatio(service.getBudget()) + " (" + service.getBudget() + ")");
            observableCreatures.setAll(service.getCreatures());
            updateDoctors(service.getDoctors());
        } else if (quarantine != null) {
            title.setText("Détails de la quarantaine");
            budget.setText("Budget : " + BudgetType.fromRatio(quarantine.getBudget()) + " (" + quarantine.getBudget() + ")");
            observableCreatures.setAll(quarantine.getCreatures());
            updateDoctors(quarantine.getDoctors());
        } else {
            title.setText("Détails de la crypte");
            budget.setText("Budget : " + BudgetType.fromRatio(crypt.getBudget()) + " (" + crypt.getBudget() + ")");
            observableCreatures.setAll(crypt.getCreatures());
            updateDoctors(crypt.getDoctors());
        }
    }

    /**
     * Updates the doctors container with the list of doctors.
     * If there are no doctors, it displays a message indicating that no doctors are available.
     *
     * @param doctors the list of doctors to display
     */
    private void updateDoctors(List<Doctor> doctors) {
        doctorsContainer.getChildren().clear();
        if (doctors == null || doctors.isEmpty()) {
            doctorsContainer.getChildren().add(new Label("Aucun médecin disponible"));
        } else {
            for (Doctor doctor : doctors) {
                HBox doctorBox = new HBox(5);
                ImageView doctorIcon = new ImageView(
                        new Image(getClass().getResourceAsStream("/images/room/DoctorInRoom.png"))
                );
                doctorIcon.setFitWidth(30);
                doctorIcon.setFitHeight(60);

                Label nameLabel = new Label(doctor.getFullName());
                nameLabel.setStyle("-fx-font-weight: bold;");

                doctorBox.getChildren().addAll(doctorIcon, nameLabel);
                doctorsContainer.getChildren().add(doctorBox);
            }
        }
    }
}
