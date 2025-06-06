package com.fantasyhospital.controller;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
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

    @FXML
    public void initialize() {
        medicalServiceListView.setItems(observableCreatures);
        medicalServiceListView.setCellFactory(listView -> new CreatureCellView(hospital));
    }

    public void setService(MedicalService service) {
        this.service = service;
        updateView();
    }

    public void setCreatures(List<Creature> creatures) {
        medicalServiceListView.setItems(FXCollections.observableArrayList(creatures));
    }

    private void updateView() {
        if (service != null) {
            title.setText("Détails du service : " + service.getName());
            budget.setText("Budget : " + BudgetType.fromRatio(service.getBudget()) + " (" + service.getBudget() + ")");


            observableCreatures.setAll(service.getCreatures());

            doctorsContainer.getChildren().clear();

            if (service.getDoctors() == null || service.getDoctors().isEmpty()) {
                doctorsContainer.getChildren().add(new Label("Aucun médecin disponible"));
            } else {
                for (Doctor doctor : service.getDoctors()) {
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
}
