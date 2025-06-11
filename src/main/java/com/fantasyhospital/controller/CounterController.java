package com.fantasyhospital.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.util.Singleton;
import com.fantasyhospital.view.EndGameCellView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import lombok.Setter;
import org.springframework.stereotype.Component;
import javafx.scene.control.ListView;
import java.util.Stack;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import javafx.scene.Scene;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

/**
 * Controller for managing the display and update of the game's statistical counters.
 * <ul>
 *   <li>Turn counter</li>
 *   <li>Healed creatures counter</li>
 *   <li>Deaths counter</li>
 * </ul>
 */
@Component
public class CounterController implements Initializable {

    /**
     * Label displaying the hospital's global budget.
     */
    @FXML
    public Label globalBudget;

    /**
     * Label displaying the number of turns.
     */
    @FXML
    private Label turnCounter;

    /**
     * Label displaying the number of healed creatures.
     */
    @FXML
    private Label healedCounter;

    /**
     * Label displaying the number of creature deaths.
     */
    @FXML
    private Label deathCounter;

    /**
     * Label displaying the number of doctor deaths.
     */
    @FXML
    private Label doctorsDeathCounter;

    private int turns = 0;


    /**
     * Reference to the current hospital.
     */
    @Setter
    @FXML
    private Hospital hospital;

    /**
     * Initializes all counters to zero when the controller is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des compteurs à 0
        resetCounters();
    }

    /**
     * Resets all counters to zero.
     */
    public void resetCounters() {
        setTurnCounter(0);
        updateHealedCounter();
        updateDeathCounter();
        updateDeathDoctorsCounter();
    }

    /**
     * Increments the turn counter and updates the display.
     */
    public void incrementTurnCounter() {
        turns++;
        javafx.application.Platform.runLater(() -> turnCounter.setText(String.valueOf(turns)));
    }

    /**
     * Updates the healed creatures counter.
     */
    public void updateHealedCounter() {
        int healedCount = Singleton.getInstance().getCreatureHealStack().size();
        javafx.application.Platform.runLater(() -> healedCounter.setText(String.valueOf(healedCount)));
    }

    /**
     * Updates the hospital's global budget counter.
     */
    public void updateGlobalBudget(Hospital hospital) {
        int budget = hospital.getGlobalBudget();
        javafx.application.Platform.runLater(() -> globalBudget.setText(String.valueOf(budget)));
    }

    /**
     * Updates the creature deaths counter.
     */
    public void updateDeathCounter() {
        int deathCount = Singleton.getInstance().getCreatureDieStack().size();
        javafx.application.Platform.runLater(() -> deathCounter.setText(String.valueOf(deathCount)));
    }

    /**
     * Updates the doctor deaths counter.
     */
    public void updateDeathDoctorsCounter() {
        int deathDoctorCount = Singleton.getInstance().getDoctorStack().size();
        javafx.application.Platform.runLater(() -> doctorsDeathCounter.setText(String.valueOf(deathDoctorCount)));
    }

    /**
     * Sets the turn counter to a specific value.
     */
    public void setTurnCounter(int value) {
        turns = value;
        javafx.application.Platform.runLater(() -> turnCounter.setText(String.valueOf(value)));
    }

    /**
     * Returns the current turn count.
     */
    public int getTurnCount() {
        return turns;
    }

    /**
     * Returns the current healed creatures count.
     */
    public int getHealedCount() {
        return Singleton.getInstance().getCreatureHealStack().size();
    }

    /**
     * Returns the current creature deaths count.
     */
    public int getDeathCount() {
        return Singleton.getInstance().getCreatureDieStack().size();
    }

    /**
     * Displays the list of healed creatures in a dialog.
     */
    @FXML
    private  void onHealedCounterClicked() {
        showListDialog("Liste des créatures soignées", Singleton.getInstance().getCreatureHealStack());
    }


    /**
     * Displays the list of dead creatures in a dialog.
     */
    @FXML
    private  void onDeathCounterClicked() {
        showListDialog("Liste des créatures décédées", Singleton.getInstance().getCreatureDieStack());
    }


    /**
     * Displays the list of dead doctors in a dialog.
     */
    @FXML
    private  void onDeathDoctorCounterClicked() {
        showListDialog("Liste des docteurs décédés", Singleton.getInstance().getDoctorStack());
    }

    // TODO : REVOIR L'AFFICHAGE !

    private <T> void showListDialog(String title, Stack<T> stack) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        ListView<T> listView = new ListView<>();
        listView.getItems().addAll(stack);

        listView.setCellFactory(param -> new ListCell<T>() {
            private final EndGameCellView cellView = new EndGameCellView();

            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    if (item instanceof Creature) {
                        Creature creature = (Creature) item;
                        String imagePath = "/images/races/" + creature.getRace().toLowerCase() + ".png";
                        Image image = new Image(getClass().getResourceAsStream(imagePath));
                        Image transparent = removePngBackground(image);
                        Image cropped = cropImage(transparent);
                        cellView.getCreatureImageView().setImage(cropped);
                        cellView.getName().setText(creature.getFullName());
                        cellView.getAgeLabel().setText("Age: " + creature.getAge());
                        setGraphic(cellView);
                    } else if (item instanceof Doctor) {
                        Doctor doctor = (Doctor) item;
                        Image image = new Image(getClass().getResourceAsStream("/images/races/doctor.png"));
                        Image transparent = removePngBackground(image);
                        Image cropped = cropImage(transparent);
                        cellView.getCreatureImageView().setImage(cropped);
                        cellView.getName().setText(doctor.getFullName());
                        cellView.getAgeLabel().setText("Age: " + doctor.getAge());
                        setGraphic(cellView);
                    }
                }
            }
        });

        listView.setStyle("-fx-control-inner-background: #2a2a2a;");

        Scene scene = new Scene(listView, 400, 300);
        scene.getStylesheets().add("/css/styles.css");

        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
}
