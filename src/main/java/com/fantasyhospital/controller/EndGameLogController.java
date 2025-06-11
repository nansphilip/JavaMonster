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
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

/**
 * Controller for displaying the end game log, including dead and healed creatures,
 * dead doctors, and closed medical services.
 */
@Component
public class EndGameLogController {

    /**
     * ListView displaying dead creatures.
     */
    @FXML
    private ListView<Creature> listViewCreaturesDead;

    /**
     * ListView displaying healed creatures.
     */
    @FXML
    private ListView<Creature> listViewCreaturesHealed;

    /**
     * ListView displaying dead doctors.
     */
    @FXML
    private ListView<Doctor> listViewDoctorsDead;

    /**
     * ListView displaying closed medical services.
     */
    @FXML
    private ListView<MedicalService> listMedicalServicesClosed;

    /**
     * Label displaying the total budget at the end of the game.
     */
    @Setter
    private Stage dialogStage;

    /**
     * Summary of the end game statistics.
     */
    private EndGameSummary summary;

    /**
     * Initializes the controller by setting up cell factories for the ListViews.
     * This method is called automatically by JavaFX after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        listViewCreaturesDead.setCellFactory(listView -> createCreatureCell());
        listViewCreaturesHealed.setCellFactory(listView -> createCreatureCell());
        listViewDoctorsDead.setCellFactory(listView -> createDoctorCell());
    }

    /**
     * Creates a ListCell for displaying creatures in the ListView.
     * The cell shows the creature's image, name, and age.
     *
     * @return a ListCell configured for displaying Creature objects
     */
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

    /**
     * Creates a ListCell for displaying doctors in the ListView.
     * The cell shows the doctor's image, name, and age.
     *
     * @return a ListCell configured for displaying Doctor objects
     */
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

    /**
     * Sets the closed medical services in the ListView.
     * Each service is displayed with its name in a bold label.
     *
     * @param closedServices the list of closed medical services
     */
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

    /**
     * Sets the end game summary and updates the UI with the data.
     *
     * @param summary the EndGameSummary containing the end game statistics
     */
    public void setSummary(EndGameSummary summary) {
        this.summary = summary;
        updateUI();
    }

    /**
     * Updates the UI components with the data from the end game summary.
     * This method populates the ListViews with dead and healed creatures,
     * dead doctors, and closed medical services.
     */
    private void updateUI() {
        listViewCreaturesDead.getItems().setAll(summary.getCreaturesDead());
        listViewCreaturesHealed.getItems().setAll(summary.getCreaturesHealed());
        listViewDoctorsDead.getItems().setAll(summary.getDoctorsDead());
        setClosedServices(summary.getMedicalServicesClosed());
    }

    /**
     * Handles the close action for the dialog.
     * This method is called when the user clicks the close button.
     */
    @FXML
    private void handleClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}
