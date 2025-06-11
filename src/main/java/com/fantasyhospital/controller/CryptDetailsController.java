package com.fantasyhospital.controller;

import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;


/**
 * Controller for displaying details of a Crypt medical service room.
 */
@Component
public class CryptDetailsController {

    /**
     * Label displaying the crypt's temperature.
     */
    @FXML
    private Label temperature;

    /**
     * Label displaying the crypt's airflow status.
     */
    @FXML
    private Label airflow;

    /**
     * Sets the crypt details in the UI.
     *
     * @param crypt the Crypt instance
     */
    public void setCrypt(Crypt crypt) {
        temperature.setText("Température: " + crypt.getTemperature() + "°C");
        airflow.setText("Climatisation: " + (crypt.isAirflow() ? "ON" : "OFF"));
    }
}