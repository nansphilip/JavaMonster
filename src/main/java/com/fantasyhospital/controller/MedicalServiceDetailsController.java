package com.fantasyhospital.controller;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.view.MedicalServiceCellView;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.view.CreatureCellView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MedicalServiceDetailsController {

    @FXML
    private Label title;

    @FXML
    private Label info;

    @FXML
    private FlowPane bedsView;

    @FXML
    private ListView<Creature> medicalServiceListView;

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

    public void loadCreaturesFromService(MedicalService service) {
        if (service != null) {
            List<Creature> creatures = service.getCreatures();
            Platform.runLater(() -> observableCreatures.setAll(creatures));
        }
    }

    private void updateView() {
        if (service != null) {
            title.setText("🔍 Détails du service : " + service.getName());
            info.setText("Type : " + service.getRoomType() +
                    "\nBudget : " + service.getBudgetType() +
                    "\nCréatures : " + (service.getCreatures() != null ? service.getCreatures().size() : 0));

            bedsView.getChildren().clear();
            bedsView.getChildren().addAll(MedicalServiceCellView.createBedsView(
                    service.getMAX_CREATURE(), service.getBudgetType(), service));

            observableCreatures.setAll(service.getCreatures());

        }
    }
}
