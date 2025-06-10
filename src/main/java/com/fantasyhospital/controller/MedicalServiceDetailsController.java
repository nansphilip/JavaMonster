package com.fantasyhospital.controller;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.model.rooms.medicalservice.Quarantine;
import com.fantasyhospital.view.CreatureCellView;
import javafx.application.Platform;
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

@Component
public class MedicalServiceDetailsController {

    @FXML
    private Label title;

    @FXML
    private Label budget;

    @FXML
    private ListView<Creature> medicalServiceListView;

    @FXML
    private VBox doctorsContainer;

    private final ObservableList<Creature> observableCreatures = FXCollections.observableArrayList();

    @Setter
    private Hospital hospital;
    private MedicalService service;
    private Quarantine quarantine;
    private Crypt crypt;

    @FXML
    public void initialize() {
        medicalServiceListView.setItems(observableCreatures);
        medicalServiceListView.setCellFactory(listView -> new CreatureCellView(hospital));
    }

    public void setService(MedicalService service) {
        this.service = service;
        this.quarantine = null;
        this.crypt = null;
        updateView();
    }

    public void setQuarantine(Quarantine quarantine) {
        this.quarantine = quarantine;
        this.service = null;
        this.crypt = null;
        updateView();
    }

    public void setCrypt(Crypt crypt) {
        this.quarantine = null;
        this.service = null;
        this.crypt = crypt;
        updateView();
    }

    public void setCreatures(List<Creature> creatures) {
        medicalServiceListView.setItems(FXCollections.observableArrayList(creatures));
    }

    public void loadCreaturesFromService(MedicalService service) {
        if (service != null) {
            List<Creature> creatures = service.getCreatures();
            Platform.runLater(() -> observableCreatures.setAll(creatures));
        }
    }

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
