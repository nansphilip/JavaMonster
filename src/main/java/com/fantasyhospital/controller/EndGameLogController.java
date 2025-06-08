package com.fantasyhospital.controller;

import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import com.fantasyhospital.util.EndGameSummary;
import com.fantasyhospital.view.EndGameCellView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

@Component
public class EndGameLogController {

    @FXML
    private ListView<Creature> listViewCreaturesDead;

    @FXML
    private ListView<Creature> listViewCreaturesHealed;

    @FXML
    private ListView<Doctor> listViewDoctorsDead;

    @FXML
    private ListView<MedicalService> listMedicalServicesClosed;

    @Setter
    private Stage dialogStage;

    private EndGameSummary summary;

    @FXML
    public void initialize() {
        listViewCreaturesDead.setCellFactory(listView -> createCreatureCell());
        listViewCreaturesHealed.setCellFactory(listView -> createCreatureCell());
        listViewDoctorsDead.setCellFactory(listView -> createDoctorCell());
    }

    private ListCell<Creature> createCreatureCell() {
        return new ListCell<>() {
            private final EndGameCellView cellView = new EndGameCellView();

            @Override
            protected void updateItem(Creature creature, boolean empty) {
                super.updateItem(creature, empty);
                if (empty || creature == null) {
                    setGraphic(null);
                } else {
                    setCreatureData(creature, "/images/races/" + creature.getRace().toLowerCase() + ".png");
                }
            }

            private void setCreatureData(Creature creature, String imagePath) {
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                Image transparent = removePngBackground(image);
                Image cropped = cropImage(transparent);
                cellView.getCreatureImageView().setImage(cropped);
                cellView.getName().setText(creature.getFullName());
                cellView.getAgeLabel().setText("Age: " + creature.getAge());
                setGraphic(cellView);
            }
        };
    }

    private ListCell<Doctor> createDoctorCell() {
        return new ListCell<>() {
            private final EndGameCellView cellView = new EndGameCellView();

            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);
                if (empty || doctor == null) {
                    setGraphic(null);
                } else {
                    Image image = new Image(getClass().getResourceAsStream("/images/races/doctor.png"));
                    Image transparent = removePngBackground(image);
                    Image cropped = cropImage(transparent);
                    cellView.getCreatureImageView().setImage(cropped);
                    cellView.getName().setText(doctor.getFullName());
                    cellView.getAgeLabel().setText("Age: " + doctor.getAge());
                    setGraphic(cellView);
                }
            }
        };
    }

    private void setClosedServices(List<MedicalService> closedServices) {
        listMedicalServicesClosed.setCellFactory(listView -> new ListCell<>() {
            private final Label nameLabel = new Label();

            {
                nameLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 5 10;");
            }

            @Override
            protected void updateItem(MedicalService service, boolean empty) {
                super.updateItem(service, empty);
                if (empty || service == null) {
                    setGraphic(null);
                } else {
                    nameLabel.setText(service.getName());
                    setGraphic(nameLabel);
                }
            }
        });

        listMedicalServicesClosed.getItems().setAll(closedServices);
    }

    public void setSummary(EndGameSummary summary) {
        this.summary = summary;
        updateUI();
    }

    private void updateUI() {
        listViewCreaturesDead.getItems().setAll(summary.getCreaturesDead());
        listViewCreaturesHealed.getItems().setAll(summary.getCreaturesHealed());
        listViewDoctorsDead.getItems().setAll(summary.getDoctorsDead());
        setClosedServices(summary.getMedicalServicesClosed());
    }

    @FXML
    private void handleClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}
