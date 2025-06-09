package com.fantasyhospital.controller;

import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class CryptDetailsController {

    @FXML
    private Label temperature;

    @FXML
    private Label airflow;

    public void setCrypt(Crypt crypt) {
        temperature.setText("Température: " + crypt.getTemperature() + "°C");
        airflow.setText("Climatisation: " + (crypt.isAirflow() ? "ON" : "OFF"));
    }
}