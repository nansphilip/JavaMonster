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
 * Contrôleur pour gérer l'affichage et la mise à jour des compteurs statistiques du jeu.
 * - Compteur de tours de jeu
 * - Compteur de créatures soignées
 * - Compteur de décès
 */
@Component
public class CounterController implements Initializable {

    @FXML
    public Label globalBudget;

    @FXML
    private Label turnCounter;

    @FXML
    private Label healedCounter;

    @FXML
    private Label deathCounter;

    @FXML
    private Label doctorsDeathCounter;

    private int turns = 0;

    @Setter
    @FXML
    private Hospital hospital;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des compteurs à 0
        resetCounters();
    }

    /**
     * Réinitialise tous les compteurs à zéro
     */
    public void resetCounters() {
        setTurnCounter(0);
        updateHealedCounter();
        updateDeathCounter();
        updateDeathDoctorsCounter();
    }

    /**
     * Incrémente le compteur de tours et met à jour l'affichage
     */
    public void incrementTurnCounter() {
        turns++;
        javafx.application.Platform.runLater(() -> turnCounter.setText(String.valueOf(turns)));
    }

    /**
     * Met à jour le compteur de créatures soignées à partir du Singleton
     */
    public void updateHealedCounter() {
        int healedCount = Singleton.getInstance().getCreatureHealStack().size();
        javafx.application.Platform.runLater(() -> healedCounter.setText(String.valueOf(healedCount)));
    }

    /**
     * Met à jour le budget global de l'hopital
     */
    public void updateGlobalBudget(Hospital hospital) {
        int budget = hospital.getGlobalBudget();
        javafx.application.Platform.runLater(() -> globalBudget.setText(String.valueOf(budget)));
    }

    /**
     * Met à jour le compteur de décès à partir du Singleton
     */
    public void updateDeathCounter() {
        int deathCount = Singleton.getInstance().getCreatureDieStack().size();
        javafx.application.Platform.runLater(() -> deathCounter.setText(String.valueOf(deathCount)));
    }

    /**
     * Met à jour le compteur de décès à partir du Singleton
     *
     */
    public void updateDeathDoctorsCounter() {
        int deathDoctorCount = Singleton.getInstance().getDoctorStack().size();
        javafx.application.Platform.runLater(() -> doctorsDeathCounter.setText(String.valueOf(deathDoctorCount)));
    }

    /**
     * Définit directement la valeur du compteur de tours
     */
    public void setTurnCounter(int value) {
        turns = value;
        javafx.application.Platform.runLater(() -> turnCounter.setText(String.valueOf(value)));
    }

    /**
     * Retourne la valeur actuelle du compteur de tours
     */
    public int getTurnCount() {
        return turns;
    }

    /**
     * Retourne le nombre actuel de créatures soignées depuis le Singleton
     */
    public int getHealedCount() {
        return Singleton.getInstance().getCreatureHealStack().size();
    }

    /**
     * Retourne le nombre actuel de décès depuis le Singleton
     */
    public int getDeathCount() {
        return Singleton.getInstance().getCreatureDieStack().size();
    }

    @FXML
    private  void onHealedCounterClicked() {
        showListDialog("Liste des créatures soignées", Singleton.getInstance().getCreatureHealStack());
    }

    @FXML
    private  void onDeathCounterClicked() {
        showListDialog("Liste des créatures décédées", Singleton.getInstance().getCreatureDieStack());
    }

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
