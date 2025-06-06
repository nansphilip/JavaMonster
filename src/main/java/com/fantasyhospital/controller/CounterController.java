package com.fantasyhospital.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.util.Singleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.Setter;
import org.springframework.stereotype.Component;

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
}
